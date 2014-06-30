package fr.cristal.emeraude.n2s3.models.timemodel

import akka.actor._
import fr.cristal.emeraude.n2s3.features.time.SynapseTime

/**
 * TODO
 * @author wgouzer & qbailleul
 */
class SynapseTimeModel(
  var weight: Double,
  pre: ActorRef,
  post: ActorRef,
  forwardPQL: ActorRef) extends SynapseTime(pre, post, forwardPQL) {

}
