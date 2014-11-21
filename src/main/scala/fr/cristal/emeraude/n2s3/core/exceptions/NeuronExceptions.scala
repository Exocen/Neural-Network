package fr.cristal.emeraude.n2s3.core.exceptions

/**
  * Exceptions related to the Neuron class
  * @author wgouzer & qbailleul
  */

class NeuronException extends Exception

/**
  * Exception thrown when a neuron receives an unknown message
  * @param msg the message you want to associate with the exception
  */
case class UnknownMessageNeuronException(msg: String) extends NeuronException
