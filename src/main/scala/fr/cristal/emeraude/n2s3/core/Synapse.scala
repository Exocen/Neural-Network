package fr.cristal.emeraude.n2s3.core

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import fr.cristal.emeraude.n2s3.core.exceptions.UnknownMessageSynapseException

import scala.concurrent.duration._

/**
 * Synapses are the link between two Neurons.
 *
 * @constructor create a new Synapse in a form of an actor with a weight, a pre
 *              and a post
 * @param pre the "parent-neuron" connected with this synapse
 * @param post the "child-neuron" connected with this synapse
 *
 * @author wgouzer & qbailleul
 */
abstract class Synapse(
                        pre: ActorRef,
                        post: ActorRef,
                        synchronizer: ActorRef) extends Actor {

  /**
   * Inform the connection of the synapse (neuron post & pre) that we're connected to it
   */
  def processInformNeurons(): Unit = {
    post ! ImYourFather(self)
    pre ! ImYourChild(self)
    sender ! InformNeurons_ack // TODO future or wait for child & parent ?
  }

  /**
   * To be extended in the model, should call fire(s: Spike): Unit to send a spike
   * to the post synaptic neuron.
   */
  def processSpike(s: Spike): Unit = s match{
    case _ ⇒ throw UnknownMessageSynapseException("Synapse received an unknown message, don't know what to do")
  }

  implicit val timeout = Timeout(5 seconds)

  import context.dispatcher

  /**
   * Methode to fire a spike to the post neuron
   */
  def fire(s: Spike): Unit = {
    val ack = synchronizer ? s
    ack onSuccess {
      case SpikeAck => done()
    }
  }

  def done() {
    sender ! Done
  }

  /**
   * Match the basic messages a synapse will receive when setting up the network
   */
  def receive = {
    case s: Spike =>processSpike(s)
    case InformNeurons => processInformNeurons()
    case _ ⇒ throw UnknownMessageSynapseException("Synapse received an unknown message, don't know what to do")
  }

}
