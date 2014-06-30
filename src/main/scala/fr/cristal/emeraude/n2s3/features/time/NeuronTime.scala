package fr.cristal.emeraude.n2s3.features.time

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import fr.cristal.emeraude.n2s3.corenetwork.Input
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Spike
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageNeuronException

/**
 * TODO THIS SCALADOC
 * @author wgouzer & qbailleul
 */
abstract class NeuronTime(
  layer: Int,
  threshold: Double,
  forwardPQL: ActorRef,
  backwardPQL: ActorRef) extends Neuron(layer) {

  /**
   * TODO: Scaladoc
   */
  override def receive = super.receive orElse {

    case SpikeForward(v: Double, timestamp: Long) =>
      println("Neuron Reception SF")

      sender ! done()

      output.foreach{o => forwardPQL ! ForwardPQL(v, System.nanoTime, o)}

      input.foreach{i => backwardPQL ! BackwardPQL(v, System.nanoTime, i)}


    //TODO case input rajouter le modele avec un double
    case Input(v: Double) =>
      println("Neuron Reception Input")
      output.foreach { o => forwardPQL ! ForwardPQL(v, System.nanoTime, o) }

    //TODO à vérifier puis mettre partout
    case _ => throw UnknownMessageNeuronException(
      "NeuronTime received an unknown message, don't know what to do")
  }

}
