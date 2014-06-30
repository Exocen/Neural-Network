package fr.cristal.emeraude.n2s3.corenetwork

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.actor.Props
import akka.pattern.ask
import fr.cristal.emeraude.n2s3.models.Model

/**
  * Create the neuronal network with a threshold, the number of neuron per layer  <br>
  * and the edges (synapses) between neurons
  *
  * @constructor Network in a form of actors inside a tree/graph.
  * @param threshold is the neurons's trigger value for propagating spikes.
  * @param nbNeuronPerLayer is the number of neurons in a layer. Conveniently,
  * the number of elements also represents the number of layer. <br>
  * The following example will produce 3 layers, the first and second will have 4
  * neurons and the last one 2 neurons :
  * {{{
  *    Seq(4,4,2)
  * }}} <br>
  * @param edges are the synapses in the network, which is a Seq of a couple of Integers,
  * like this (FromNeuron1, ToNeuron2). <br>
  * The following example will produce three oriented edges 1->2, 1->3 and 2->3 :
  * {{{
  *    Seq((1,2), (1,3), (2,3))
  * }}}
  * @author wgouzer & qbailleul
  */
trait Network extends Model {
  /**
    * TODO : this scaladoc
    */
  val nbNeuronPerLayer: Seq[Int]
  val edges: Option[Seq[(Int, Int, Int, Int)]] = None

  /**  TODO à retirer peut-être */
  lazy val nbLayer: Int = nbNeuronPerLayer.length

  /**
    * Total number of neurons in this network, it's global for avoiding the re-computing
    * everytime a function needs it (at least 2-3 times in this code).
    */
  lazy val nbNeuron = nbNeuronPerLayer.sum(implicitly[Numeric[Int]])

  /** new actor system with the specific name "Neuronal-network" */
  implicit val system = ActorSystem("Neuronal-network")

  /** Array of couple for all the neurons' references and their associated layers */
  var neuronsActorRef = Array[Array[ActorRef]]()

  /** Sequence of all the synapses' references */
  var synapsesActorRef = Seq[ActorRef]()

  /**
    * TODO : update this scaladoc
    * Creates all the needed neurons.
    * @return an array of couple : neurons' references and layer's number.
    */
  def createNeurons(): Array[Array[ActorRef]] = {
    var neuroRef = new Array[Array[ActorRef]](nbNeuronPerLayer.length)

    for (i <- 0 until nbNeuronPerLayer.length) {
      neuroRef(i) = new Array[ActorRef](nbNeuronPerLayer(i))
      for (j <- 0 until nbNeuronPerLayer(i)) {
        neuroRef(i)(j) = system.actorOf(Props(createNeuron(i)), name = i + "." + j)
      }
    }
    neuroRef
  }

  /**
    * Create the links (synapses) between neurons.
    * @param neuroRefs the array that contains the couples of neurons' references and their layer.
    * @return the references of all the synapses created.
    */
  def createSynapses(neuroRefs: Array[Array[ActorRef]]): Seq[ActorRef] = {
    // # maximum of edges in a graph =>  n * (n-1) / 2
    edges match {
      case None => createFullConnection(neuroRefs)
      case Some(t) => createManualConnection(neuroRefs, t)
    }

    //  or this one ? more compact but less visible
    //    edges map (t => createManualConnection(neuroRefs, t)) getOrElse createFullConnection(neuroRefs)
  }

  /**
    * TODO : update this scaladoc
    * Connects each neuron on a higher layer to
    * every neuron in its inferior layer.
    * @param neuroRefs the array that contains the couples of neurons' references and their layer.
    * @return the references of all the synapses created.
    */
  def createFullConnection(neuroRefs: Array[Array[ActorRef]]): Seq[ActorRef] = {
    var arefs = Seq[ActorRef]()

    for (
      i <- 0 until nbNeuronPerLayer.length - 1;
      j <- 0 until nbNeuronPerLayer(i);
      k <- 0 until nbNeuronPerLayer(i + 1)
    ) {
      arefs = arefs :+ system.actorOf(Props(
        createSynapse(neuroRefs(i)(j), neuroRefs(i + 1)(k))),
        name = i + "." + j + "_" + (i + 1) + "." + k)
    }

    arefs
  }

  /**
    * TODO : update this scaladoc
    * Connects manually the neurons with synapses. It is done through the constructor.
    * param edges in [[fr.cristal.emeraude.n2s3.Network]].
    * @param neuroRefs the array that contains all the couples of neurons' references and their layer.
    * @param edges represents the connections between two neurons (synapses).
    * @return the references of all the synapses created.
    */
  def createManualConnection(neuroRefs: Array[Array[ActorRef]], edges: Seq[(Int, Int, Int, Int)]): Seq[ActorRef] = {
    var arefs = Seq.empty[ActorRef]

    edges map {
      w =>
      // TODO in manual mode: do we need to check whether the layers are connected with +/- 1 of
      // difference or we let the user do whatever he wants (and possibly screwed up everything) ?
      arefs = arefs :+ system.actorOf(Props(
        createSynapse(neuroRefs(w._1)(w._2), neuroRefs(w._3)(w._4))),
        name = (w._1) + "."(w._2) + "_" + (w._3) + "." + (w._4))
    }

    arefs
  }

  /**
    * Makes all the synapses send messages to their neurons
    * in order to inform them about the topology of the network.
    * @param synapRefs array that contains all the synapses' references.
    */
  def reportAndInformNeurons(synapRefs: Seq[ActorRef]): Unit = {
    implicit val timeout = Timeout(Duration(60, "seconds"))

    synapRefs map (w =>
      Await.result(w ? InformNeurons, Duration(300, "millis")))
  }

  /**
    * Creates all the neurons, synapses and makes the link between the two entities through
    * spikes exchange
    */
  def initiateNetwork() {
    println("Creating neurons...")
    neuronsActorRef = createNeurons()

    println("Creating synapses...")
    synapsesActorRef = createSynapses(neuronsActorRef)

    /** tells the neurons about their new friends : synapses (edges of the graph) */
    println("Connecting neurons & synapses...")
    reportAndInformNeurons(synapsesActorRef)
  }

  /**
    * TODO scaladoc
    */
  def firingSpikes()

}
