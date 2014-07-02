package fr.cristal.emeraude.n2s3.features.monitoring

import akka.actor.ActorRef
import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageSynapseException

/**
 * TODO THIS SCALADOC
 * @author wgouzer & qbailleul
 */
abstract class SynapseMonitoring(
  pre: ActorRef,
  post: ActorRef) extends Synapse(pre, post) {

  val log = Logging(context.system, this)

  //  def processUnknown(s: String) = {
  // log.info(s)
  //    throw new UnknownMessageSynapseException(
  //      "Synapse received an unknown message, don't know what to do")
  //  }

  override def receive = {
    case _ => println("monitoring synapse")
    // override def receive = ({
    //   case _ =>
    // }) orElse super.receive
  }

}