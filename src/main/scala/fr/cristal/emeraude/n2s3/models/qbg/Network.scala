package fr.cristal.emeraude.n2s3.models.qbg

import akka.actor._
import fr.cristal.emeraude.n2s3.core
import fr.cristal.emeraude.n2s3.features.logging.Logging

/**
 * @see [[fr.cristal.emeraude.n2s3.core.Network]]
 * @author wgouzer & qbailleul
 */
class Network(nbNPL: Seq[Int]) extends core.Network {

  /** @see [[fr.cristal.emeraude.n2s3.core.Network.nbNeuronPerLayer]] */
  override val nbNeuronPerLayer = nbNPL
  val logNeuron = Logging.log("Neuron.log", system)
  val logSynapse = Logging.log("Synapse.log", system)
  val logSync = Logging.log("Synchronizer.log", system)

  //TODO Should be changed
  override def firingSpikes() = {
    neuronsActorRef(0)(0) ! PostSynapticForwardSpike(0, neuronsActorRef(0)(0))
    PostSynapticForwardSpike(3, neuronsActorRef(0)(0))
    PostSynapticForwardSpike(7, neuronsActorRef(0)(0))
  }

  /**
   * Instanciate a neuron with a layer and a threshold
   * @see fr.cristal.emeraude.n2s3.models.Model#createNeuron(int)
   */
  override def createNeuron(layer: Int, synchronizerIn: ActorRef, synchronizerOut: ActorRef): Neuron = {
    new Neuron(layer, synchronizer, synchronizer, logNeuron)
  }

  /**
   * Instanciate a synapse with a weight, pre (parent) and post (child)
   * @see fr.cristal.emeraude.n2s3.models.Model#createSynapse(akka.actor.ActorRef, akka.actor.ActorRef)
   */
  override def createSynapse(pre: ActorRef, post: ActorRef, synchronizer: ActorRef) = {
    new Synapse(pre, post, synchronizer, logSynapse)
  }

  override def createSynchronizer() = {
    system.actorOf(Props(new Synchronizer(logSync)))
  }
}
