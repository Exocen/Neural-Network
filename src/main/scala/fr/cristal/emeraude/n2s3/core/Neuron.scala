package fr.cristal.emeraude.n2s3.core

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import fr.cristal.emeraude.n2s3.core.exceptions.UnknownMessageNeuronException

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Neurons are the "brain" of the simulator, they will compute and send spikes.
  *
  * @constructor create a new Neuron in a form of an actor with a layer, its input(s),
  * its ouput(s) and a threshold.
  * @param layer in which the neuron is
  * @author wgouzer & qbailleul
  */
abstract class Neuron(
  layer: Int,
  synchronizerIn: ActorRef,
  synchronizerOut: ActorRef) extends Actor {
  var input: Seq[ActorRef] = Seq()
  var output: Seq[ActorRef] = Seq()
  /**
    * To be extended in the model, should call fire(s: Spike): Unit to send a spike
    * to the post synaptic neuron.
    */
  def processSpike(s: Spike) :Unit = s match{
    case _ ⇒ throw UnknownMessageNeuronException(      "Neuron received message " + s + ", don't know what to do with it.")
  }

  implicit val timeout = Timeout(5 seconds)
  import context.dispatcher

  /**
    * Method to fire.g
    * @param sIns: Seq[Spike] = the spikes to send to the input synapses
    * @param sOuts: Seq[Spike] = the spikes to send to the output synapses
    */
  def fire(sIns: Seq[Spike], sOuts: Seq[Spike]): Unit = {
    val ackIns = sIns map (s => (synchronizerIn ? s).mapTo[SyncMessage])
    val ackOuts = sOuts map (s => (synchronizerOut ? s).mapTo[SyncMessage])

    val fIn = Future.fold(ackIns)(SpikeAck)((r: SyncMessage, l: SyncMessage) => (r, l) match {
      case (SpikeAck, SpikeAck) => SpikeAck
    })
    val fOut = Future.fold(ackOuts)(SpikeAck)((r: SyncMessage, l: SyncMessage) => (r, l) match {
      case (SpikeAck, SpikeAck) => SpikeAck
    })

    val f = fIn zip fOut
    f onSuccess {
      case (SpikeAck, SpikeAck) => done()
    }
  }

  def done(): Unit = sender ! Done

  /**
    * Match the basic messages a neuron will receive when setting up the network
    */
  def receive = {
    case s: Spike => processSpike(s)
    case ImYourFather(father: ActorRef) ⇒ input = input :+ father
    case ImYourChild(child: ActorRef) ⇒ output = output :+ child
    case GetLayer() ⇒ sender ! layer
    case _ => throw UnknownMessageNeuronException("Neuron received an unknown message, don't know what to do")
  }

}
