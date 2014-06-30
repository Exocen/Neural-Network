package fr.cristal.emeraude.n2s3.corenetwork

import akka.actor._
import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.exceptions._

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
abstract class Neuron(
  layer: Int,
  var input: Seq[ActorRef] = Seq(),
  var output: Seq[ActorRef] = Seq()) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case ImYourFather(father: ActorRef) =>
      input = input :+ father

    case ImYourChild(child: ActorRef) =>
      output = output :+ child

    case GetLayer =>
      sender ! layer

    case Welcome => output foreach { w => println(w.toString) }

//    case _ => throw UnknownMessageNeuronException("Neuron received an unknown message, don't know what to do")
  }

}
