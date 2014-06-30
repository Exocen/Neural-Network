package fr.cristal.emeraude.n2s3.models.timemodel

import akka.actor._
import fr.cristal.emeraude.n2s3.features.time.NeuronTime

/**
 * TODO
 * @author wgouzer & qbailleul
 */
class NeuronTimeModel(
  layer: Int,
  threshold: Double,
  forwardPQL: ActorRef,
  backwardPQL: ActorRef) extends NeuronTime(layer, threshold, forwardPQL, backwardPQL) {

}
