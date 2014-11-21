package fr.cristal.emeraude.n2s3.core.exceptions

/**
  * Exceptions related to the Network class
  * @author wgouzer & qbailleul
  */

class NetworkException extends Exception

/**
  * future documentation
  * @param msg the message you want to associate with the exception
  */
case class UnknownNetworkException(msg: String) extends NetworkException
