package fr.cristal.emeraude.n2s3.models.pji

import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageNeuronException
import fr.cristal.emeraude.n2s3.corenetwork.Input

/**
 * Neurons are the "brain" of the simulator, they will compute and send spikes.
 *
 * @constructor create a new Neuron in a form of an actor with a layer, its parents,
 * its childs and a threshold.
 * @param layer in which the neuron is
 * @param parents sequence of synapses' references, for all "parent-synapse".
 * @param childs sequence of synapses' references, for all "child-synapse".
 * @param threshold the trigger value for the neurons.
 *
 * @author wgouzer & qbailleul
 */
class NeuronPji(
  layer: Int,
  threshold: Double) extends Neuron(layer) {

  override val log = Logging(context.system, this)

  override def receive = super.receive orElse {
    case SpikeForward(v: Double) =>
      log.info("[Neuron] SpikeForward")
      if (v > threshold) {
        log.info("[Neuron] SpikeBackward (Pass)")
        input map { _ ! SpikeBackward(v) }
        output map { _ ! SpikeForward(v) }
      } else {
        log.info("[Neuron] SpikeBackward (Block)")
      }

    case Input(v: Double) =>
      log.info("Neuron receives an Input")
      output map { _ ! SpikeForward(v) }

    case _ => throw UnknownMessageNeuronException(
      "Neuron received an unknown message, don't know what to do")
  }

}
