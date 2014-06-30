package fr.cristal.emeraude.n2s3.features.time

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageSynapseException

/**
  * TODO THIS SCALADOC
  * @author wgouzer & qbailleul
  */
abstract class SynapseTime(
  parent: ActorRef,
  child: ActorRef,
  forwardPQL: ActorRef) extends Synapse(parent, child) {

  /**
    * TODO: Scaladoc
    */
  override def receive = super.receive orElse {
    case SpikeForward(v: Double, timestamp: Long) =>
      println("Synapse Reception SF")

      sender ! done()
      forwardPQL ! ForwardPQL(v, System.nanoTime, child)

    case SpikeBackward(v: Double, timestamp: Long) =>
      println("Changement de poids0 synaptique")
      sender ! done()
      //TODO : faire le changement de poids synaptique

    case _ => throw UnknownMessageSynapseException(
      "Synapse received an unknown message, don't know what to do")
  }
}
