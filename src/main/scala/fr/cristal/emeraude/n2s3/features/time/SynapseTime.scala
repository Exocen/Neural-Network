package fr.cristal.emeraude.n2s3.features.time

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration.Duration
import scala.concurrent.Await
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

  def processFW (v: Double) = {
    implicit val timeout = Timeout(Duration(60, "seconds"))
    sender ! done()
    //forwardPQL ! ForwardPQL(v, System.nanoTime, child)
    Await.result(
      forwardPQL ? ForwardPQL(v, System.nanoTime, child), Duration(300, "millis")
    )

  }

  def processBW = {
    sender ! done()
    //TODO : faire le changement de poids synaptique
  }
  /**
    * TODO: Scaladoc + rm println ?
    */
  override def receive = super.receive orElse {
    case SpikeForward(v: Double, timestamp: Long) =>
      println("Synapse : Reception SF")
      processFW(v)

    case SpikeBackward(v: Double, timestamp: Long) =>
      println("Synapse : Changing synaptic's weigth")
      processBW

    //TODO mettre en relation avec le process onfail/onsuccess
    case ack() =>

    case _ => throw UnknownMessageSynapseException(
      "Synapse received an unknown message, don't know what to do")
  }
}
