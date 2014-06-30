package fr.cristal.emeraude.n2s3.models.simplisticwotime

import scala.util.Random
import akka.actor._
import akka.pattern.ask
import fr.cristal.emeraude.n2s3.corenetwork.Network
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.corenetwork.Input

/**
  * TODO this scaladoc
  * @author wgouzer & qbailleul
  */
class NetworkSimplisticWoTime(
  nbNPL: Seq[Int],
  threshold: Double = 8) extends Network {

  override val nbNeuronPerLayer = nbNPL

  /**
    * Function for demoing the application only
    * Send a message to the first Neuron in the first layer
    * @param v spike (that's the message)
    */
  override def firingSpikes() {
    val v: Double = 10.0
    neuronsActorRef(0) map { _ ! Input(v) }
  }

  /**
    * TODO : this scaladoc
    */
  def createNeuron(layer: Int): Neuron = {
    new NeuronSimplisticWoTime(layer, this.threshold)
  }

  /**
    * TODO : this scaladoc
    */
  def createSynapse(parent: ActorRef, child: ActorRef): Synapse = {
    val rand = new Random()
    new SynapseSimplisticWoTime(rand.nextDouble() * 10, parent, child)
  }

}
