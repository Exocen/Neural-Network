package fr.cristal.emeraude.n2s3.features.monitoring

import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageNeuronException

/**
  * TODO THIS SCALADOC
  * @author wgouzer & qbailleul
  */
abstract class NeuronMonitoring(
  layer: Int
) extends Neuron(layer) {

  override val log = Logging(context.system, this)

//  def processUnknown(s: String) = {
//    log.info(s)
//    throw new UnknownMessageNeuronException(
//      "Neuron received an unknown message, don't know what to do")
//  }

  override def receive = {
    case _ => super.receive
  }
}
