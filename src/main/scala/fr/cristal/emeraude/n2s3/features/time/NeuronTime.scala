package fr.cristal.emeraude.n2s3.features.time

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import fr.cristal.emeraude.n2s3.corenetwork.Input
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Spike
import fr.cristal.emeraude.n2s3.corenetwork.exceptions.UnknownMessageNeuronException

/**
  * TODO THIS SCALADOC
  * @author wgouzer & qbailleul
  */
abstract class NeuronTime(
  layer: Int,
  threshold: Double,
  forwardPQL: ActorRef,
  backwardPQL: ActorRef) extends Neuron(layer) {

  def processFW (v: Double) = {
    implicit val timeout = Timeout(Duration(60, "seconds"))
    sender ! done()

    output map { o =>
      Await.result(forwardPQL ? ForwardPQL(v, System.nanoTime, o), Duration(300, "millis"))
    }

    output map { i =>
      Await.result(backwardPQL ? BackwardPQL(v, System.nanoTime, i), Duration(300, "millis"))
    }

  }

  def  processInput (v: Double) = {
    output.foreach { o => forwardPQL ! ForwardPQL(v, System.nanoTime, o) }
  }

  /**
    * TODO: Scaladoc+rm println ?
    */
  override def receive = super.receive orElse {

    case SpikeForward(v: Double, timestamp: Long) =>
      println("Neuron : Reception SF")
      processFW(v)

    case Input(v: Double) =>
      println("Neuron : Reception Input")
      processInput(v)

    //TODO mettre en relation avec le process onfail/onsuccess
    case ack() =>

    case _ => throw UnknownMessageNeuronException(
      "NeuronTime received an unknown message, don't know what to do")
  }

}
