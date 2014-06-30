package fr.cristal.emeraude.n2s3.models.simplisticwotime

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
class SynapseSimplisticWoTime(
  var weight: Double,
  pre: ActorRef,
  post: ActorRef) extends Synapse(pre, post) {

  val log = Logging(context.system, this)
  
  /**
   * a formula to test the network
   */
  def weight_modificator(v: Double): Double = {
    val oldw = weight
    weight = weight + (v / 10)
    if (weight < 0) weight = 0
    //log.info("Weight: " + (oldw - (oldw % 0.01)) + " => " + (weight - (weight % 0.01)))
    log.info("Weight: " + (math floor oldw * 100 / 100) + " => " + (math floor weight * 100 / 100))
    -v - 1
  }

  override def receive = super.receive orElse {
    case SpikeBackward(v: Double) => weight_modificator(v)

    case SpikeForward(v: Double) => post ! SpikeForward(weight_modificator(-v))

    // case takeWeight(w: Double) => weight = w + weight

    case _ => throw UnknownMessageSynapseException(
      "Synapse received an unknown message, don't know what to do")
  }

}
