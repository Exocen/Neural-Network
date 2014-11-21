package fr.cristal.emeraude.n2s3.models

import _root_.fr.cristal.emeraude.n2s3.core.{Neuron, Synapse}
import akka.actor._

/**
  * Force the user to redefine how and what neuron/synapse he'll instantiate
  * @author wgouzer & qbailleul
  */
trait Model {

  /**
    * Create a neuron at a specific layer
    * @param layer of the neuron
    */
  def createNeuron(layer: Int, synchronizerIn: ActorRef, synchronizerOut: ActorRef): Neuron

  /**
    * Takes two neurons and create a synapse between them
    * @param pre the neuron located in the higher layer
    * @param post the neuron located in the lower layer
    */
  def createSynapse(pre: ActorRef, post: ActorRef, synchronizer: ActorRef): Synapse
}
