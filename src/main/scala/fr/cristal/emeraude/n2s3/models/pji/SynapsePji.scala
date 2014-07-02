package fr.cristal.emeraude.n2s3.models.pji

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageSynapseException

/**
 * Synapses are the link between two Neurons.
 *
 * @constructor create a new Synapse in a form of an actor with a weight, a parent
 * and a child
 * @param weight mechanism for reinforcing or deprecating a link (synapse)
 * @param parent the "parent-neuron" connected with this synapse
 * @param child the "child-neuron" connected with this synapse
 *
 * @author wgouzer & qbailleul
 */
class SynapsePji(
  var weight: Double,
  pre: ActorRef,
  post: ActorRef) extends Synapse(pre, post) {

  val log = Logging(context.system, this)

  /**
   * a formula to test the network
   */
  def pji_formula(v: Double): Double = {
    val oldw = weight
    weight = weight + (v / 10)
    if (weight < 0) weight = 0
    log.info("Weight: " + oldw + " => " + weight)
    v - 1
  }

  override def receive = super.receive orElse {
    case SpikeForward(v: Double) =>
      post ! SpikeForward(pji_formula(v))

    case SpikeBackward(v: Double) =>
      weight = pji_formula(v)

    case _ => throw UnknownMessageSynapseException(
      "Synapse received an unknown message, don't know what to do")
  }

}
