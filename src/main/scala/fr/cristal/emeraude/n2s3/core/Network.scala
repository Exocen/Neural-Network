package fr.cristal.emeraude.n2s3.core

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import fr.cristal.emeraude.n2s3.models.Model

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Create the neural network with a threshold, the number of neuron per layer  <br>
 * and the edges (synapses) between neurons
 *
 * @constructor Network in a form of actors inside a tree/graph.
 * @author wgouzer & qbailleul
 */
trait Network extends Model {

  /**
   * Represents the number of neurons into each layer. For example, nbNeuronPerLayer(0).length will give you the
   * number of neurons into the first layer
   */
  val nbNeuronPerLayer: Seq[Int]

  /**
   * In the case of a manual connection, this sequence will contains 4-tuples in this form :
   * (x1, y1, x2, y2). It will then connect the neuron [layer: x1][index: y1] to the neuron
   * [layer: x2][index: y2]
   */
  val edges: Option[Seq[(Int, Int, Int, Int)]] = None

  /** Quick way (i.e. alias) to retrieve the length of the sequence nbNeuronPerLayer */
  lazy val nbLayer: Int = nbNeuronPerLayer.length

  /** Array of Array [layer][index] storing all the neurons' references and their associated layers */
  var neuronsActorRef = Array[Array[ActorRef]]()

  /** Sequence storing all the synapses' references */
  var synapsesActorRef = Seq[ActorRef]()

  var synchronizer: ActorRef = _ //TODO type Option

  /**
   * Total number of neurons in this network, it's global for avoiding the re-computing
   * everytime a function needs it (at least 2-3 times in this code).
   */
  lazy val nbNeuron = nbNeuronPerLayer.sum(implicitly[Numeric[Int]])

  /** New actor system with the specific name "Neural-network" */
  implicit val system = ActorSystem("Neural-network")

  /**
   * Creates all the needed neurons(actors) for this network.
   * @return an array(layer) of array(index) of those neurons' references. For example:
   *         {{{
   *                     ArrayOfArray(0)(1)
   *         }}}
   *         will try to access the neuron of the first layer, at the second position
   */
  def createNeurons(): Array[Array[ActorRef]] = {
    var neuroRef = new Array[Array[ActorRef]](nbNeuronPerLayer.length)

    for (i <- 0 until nbNeuronPerLayer.length) {
      neuroRef(i) = new Array[ActorRef](nbNeuronPerLayer(i))
      for (j <- 0 until nbNeuronPerLayer(i)) {
        neuroRef(i)(j) = system.actorOf(Props(createNeuron(i, synchronizer, synchronizer)), name = i + "." + j)
      }
    }
    neuroRef
  }

  /**
   * Create the links (synapses) between neurons.
   * @param neuroRefs the array(layer) of array(index), used for linking the synapse to the neurons.
   * @return the references of all the synapses created.
   */
  def createSynapses(neuroRefs: Array[Array[ActorRef]]): Seq[ActorRef] = {
    // # maximum of edges in a graph =>  n * (n-1) / 2
    edges match {
      case None ⇒ createFullConnection(neuroRefs)
      case Some(t) ⇒ createManualConnection(neuroRefs, t)
    }
  }

  /**
   * Connects each neuron on a higher layer to every neuron in its inferior layer.
   * @param neuroRefs the array(layer) of array(index), used for linking the synapse to the neurons.
   * @return the references of all the synapses created.
   */
  def createFullConnection(neuroRefs: Array[Array[ActorRef]]): Seq[ActorRef] = {
    for {
      i ← 0 until nbNeuronPerLayer.length - 1
      j ← 0 until nbNeuronPerLayer(i)
      k ← 0 until nbNeuronPerLayer(i + 1)
    } yield {
      system.actorOf(
        Props(createSynapse(neuroRefs(i)(j), neuroRefs(i + 1)(k), synchronizer)
        ), name = i + "." + j + "_" + (i + 1) + "." + k
      )
    }
  }

  /**
   * Connects manually the neurons with synapses.
   * @param neuroRefs the array(layer) of array(index), this is used for linking the synapses to
   *                  the neurons.
   * @param edges @see [[fr.cristal.emeraude.n2s3.core.Network# e d g e s]]
   * @return the references of all the synapses created.
   */
  def createManualConnection(neuroRefs: Array[Array[ActorRef]], edges: Seq[(Int, Int, Int, Int)]): Seq[ActorRef] = {
    edges map { w ⇒
      system.actorOf(
        Props(createSynapse(neuroRefs(w._1)(w._2), neuroRefs(w._3)(w._4), synchronizer)
        ), name = w._1 + "." + w._2 + "_" + w._3 + "." + w._4
      )
    }
  }

  /**
   * Makes all the synapses send messages to their neurons
   * in order to inform them about the topology of the network.
   * @param synapRefs contains all the synapses' references.
   */
  def reportAndInformNeurons(synapRefs: Seq[ActorRef]): Unit = {
    implicit val timeout = Timeout(Duration(60, "seconds"))
    synapRefs map (s =>
      Await.result(s ? InformNeurons, Duration(300, "millis")))
      }

  def createSynchronizer() = system.actorOf(Props[Synchronizer])

  /**
   * Creates all the neurons, synapses and makes the link between the two entities (through
   * spikes exchange)
   */
  def initiateNetwork() {
    println("Creating synchronizer...")
    synchronizer = createSynchronizer()

    println("Creating neurons...")
    neuronsActorRef = createNeurons()

    println("Creating synapses...")
    synapsesActorRef = createSynapses(neuronsActorRef)

    /** tells the neurons about their new friends : synapses (edges of the graph) */
    println("Connecting neurons & synapses...")
    reportAndInformNeurons(synapsesActorRef)
  }

  /**
   * Firing spike needs to be override in each model. It will send input spikes into the
   * first neurons
   */
  def firingSpikes()

}

