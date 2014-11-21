package fr.cristal.emeraude.n2s3.models.qbg

import akka.actor.ActorRef
import fr.cristal.emeraude.n2s3.core
import fr.cristal.emeraude.n2s3.core._
import fr.cristal.emeraude.n2s3.core.exceptions.UnknownMessageNeuronException

/**
 * Neurons are the "brain" of the simulator, they will compute and send spikes.
 *
 * @constructor create a new Neuron in a form of an actor with a layer, its parents,
 *              its children and a threshold.
 * @param layer in which the neuron is
 *
 * @author wgouzer & qbailleul
 */
class Neuron(
              layer: Int,
              synchronizerIn: ActorRef,
              synchronizerOut: ActorRef,
              logger: ActorRef) extends core.Neuron(layer, synchronizerIn, synchronizerOut) {

  val xth = 1.0
  val delay = 2
  var x = 0.0

  /**
   * @see fr.cristal.emeraude.n2s3.core.Neuron#receive
   */
  override def processSpike(s: Spike) = s match {
    case PostSynapticForwardSpike(timestamp, destination) =>
      logger ! WriteToLogFile("%s %s %s %s".format(self, timestamp + delay, x, xth))
      if (x >= xth) {
        fire(input map (d => PostSynapticBackwardSpike(timestamp + delay, d, self)),
          output map (d => PreSynapticSpike(timestamp + delay, d)))
      } else {
        done()
      }

    case m => throw UnknownMessageNeuronException(
      "Neuron received message " + m + ", don't know what to do with it.")
  }
}
