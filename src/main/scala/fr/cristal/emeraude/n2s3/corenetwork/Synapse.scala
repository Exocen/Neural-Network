package fr.cristal.emeraude.n2s3.corenetwork

import akka.actor.Actor
import akka.actor.ActorRef
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
abstract class Synapse(
  pre: ActorRef,
  post: ActorRef) extends Actor {

  def receive = {
    case InformNeurons =>
      post ! ImYourFather(self)
      pre ! ImYourChild(self)
      sender ! InformNeurons_ack // TODO wait for child & parent

    case Welcome => println("welcome")

    //    case _ => throw UnknownMessageSynapseException("Synapse received an unknown message, don't know what to do")
  }

}
