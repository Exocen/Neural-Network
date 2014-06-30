package fr.cristal.emeraude.n2s3.models.timemodel

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Random

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import fr.cristal.emeraude.n2s3.corenetwork.GetLayer
import fr.cristal.emeraude.n2s3.corenetwork.Input
import fr.cristal.emeraude.n2s3.corenetwork.Network
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.features.time.PriorityQueueLayer

/**
  * TODO
  * @author wgouzer & qbailleul
  */
class NetworkTimeModel(
  nbNPL: Seq[Int],
  threshold: Double = 8) extends Network {

  override val nbNeuronPerLayer = nbNPL
  var PQL_per_layer = collection.immutable.IndexedSeq[ActorRef]()

  /**
    * TODO + enlever println
    */
  override def firingSpikes() {
    println("Fire !")
    val v: Double = 10.0
    neuronsActorRef(0) map { _ ! Input(v) }
  }

  /**
    * TODO : this scaladoc
    */
  def createPQL(layer: Int) = {
    PQL_per_layer = for (i <- 0 until layer - 1)
    yield system.actorOf(Props(new PriorityQueueLayer(i)))
  }

  /**
    * TODO : this scaladoc
    */
  def createNeuron(layer: Int): Neuron = {
    if (layer == 0) {
      new NeuronTimeModel(
        layer,
        this.threshold,
        PQL_per_layer(layer), // forwardpql
        PQL_per_layer(layer) // backwardpql
      )
    } else if (layer == nbLayer - 1) {
      new NeuronTimeModel(
        layer,
        this.threshold,
        PQL_per_layer(layer - 1), // forwardpql
        PQL_per_layer(layer - 1) // backwardpql
      )
    } else {
      new NeuronTimeModel(
        layer,
        this.threshold,
        PQL_per_layer(layer), // forwardpql
        PQL_per_layer(layer - 1) // backwardpql
      )
    }
  }

  /**
    * TODO : this scaladoc + enlever printlns
    */
  def createSynapse(pre: ActorRef, post: ActorRef): Synapse = {
    implicit val timeout = Timeout(Duration(60, "seconds"))

    val res: Int = Await.result(pre ? GetLayer, Duration(300, "millis")).asInstanceOf[Int]

    new SynapseTimeModel(
      new Random().nextDouble() * 10,
      pre,
      post,
      PQL_per_layer(res))
  }

  override def initiateNetwork() {
    println("Creating PQL....")
    createPQL(nbLayer)

    println("Creating neurons...")
    neuronsActorRef = createNeurons()

    println("Creating synapses...")
    synapsesActorRef = createSynapses(neuronsActorRef)

    /** tells the neurons about their new friends : synapses (edges of the graph) */
    println("Connecting neurons & synapses...")
    reportAndInformNeurons(synapsesActorRef)
  }

}
