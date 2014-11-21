package fr.cristal.emeraude.n2s3.core.exceptions

/**
  * Exceptions related to the Synapse class
  * @author wgouzer & qbailleul
  */

class SynapseException extends Exception

/**
  * Exception thrown when a synapse receives an unknown message
  * @param msg the message you want to associate with the exception
  */
case class UnknownMessageSynapseException(msg: String) extends SynapseException
