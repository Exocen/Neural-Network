package fr.cristal.emeraude.n2s3.features.time

import akka.actor._
import fr.cristal.emeraude.n2s3.corenetwork.Spike

/**
  * Simulate a post-synaptic spike, a simple spike before all the computing
  * done on it
  */
case class SpikeBackward( // start
  v: Double,
  timestamp: Long) extends Spike

/**
  * Simulate a pre-synaptic spike, a simple spike altered by the computing
  * done on it
  */
case class SpikeForward( // start
  v: Double,
  timestamp: Long) extends Spike

/**
  * TODO
  */
case class BackwardPQL(
  v: Double,
  timestamp: Long,
  dest: ActorRef) extends Spike

/**
  * TODO
  */
case class ForwardPQL(
  v: Double,
  timestamp: Long,
  dest: ActorRef ) extends Spike

case class done()
