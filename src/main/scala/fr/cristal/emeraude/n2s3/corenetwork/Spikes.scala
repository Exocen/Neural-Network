package fr.cristal.emeraude.n2s3.corenetwork

import akka.actor._

/**
 * Represents the messages that Neurons and Synapses will send to each other.
 * @author wgouzer & qbailleul
 */
trait Spike

/**
 * Typical hello world type message, it isn't useful for real world application,
 * it's here for debug and test purposes
 */
case class Welcome() extends Spike

/**
 * Used for discovering the network, it'll allow the synapses to send messages
 * to the neurons they're connected to in order to let him update its
 * sequences (parents & childs).
 */
case class InformNeurons() extends Spike

/**
 * Used for discovering the network, it'll allow the neurons to acknowledge
 * the reception of the message (from the synapses)
 */
case class InformNeurons_ack() extends Spike

/**
 * Tell the neuron connected to this synapse : "this synapse is your father"
 * @param neuron for which the synapse is the father
 */
case class ImYourFather(
  neuron: ActorRef) extends Spike

/**
 * Tell the neuron connected to this synapse : "this synapse is your child"
 * @param neuron for which the synapse is the child
 */
case class ImYourChild(
  neuron: ActorRef) extends Spike

/**
 * TODO: Scaladoc + change to Input()
 */
case class Input(v: Double) extends Spike

/**
 * TODO: Scaladoc
 */
case class GetLayer() extends Spike
