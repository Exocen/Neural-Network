package fr.cristal.emeraude.n2s3

import fr.cristal.emeraude.n2s3.core.CloseLogFile
import fr.cristal.emeraude.n2s3.core.exceptions.CantOpenLogFileException
import fr.cristal.emeraude.n2s3.features.logging.Logging

/**
  * Instantiate the neural network with akka's actors and launch the simulation by sending spikes
  * (messages) to the "input" neuron(s).
  * @author wgouzer & qbailleul
  */
object Main {
  type OptionMap = Map[Symbol, Any]

  /**
    * Usage to help the user run this program
    */
  def usage() {
    println("\nUsage: [VM] [PROGRAM] [OPTION]")
    println("\t e.g. java -jar n2s3.jar")
    println("\nAvailable options :")
    println("\t--debug")
    println("\t--verbose\n")
    sys exit 1984
  }

  def attachShutdownHook(net: models.qbg.Network) {
    val t: Thread = new Thread {
      override def run() {
        println("\nClosing log files...")
        net.logNeuron ! CloseLogFile
        net.logSynapse ! CloseLogFile
        net.logSync ! CloseLogFile
        println("Closing Actor system...")
        net.system.shutdown()
      }
    }

    //t setDaemon true //dont know what that do
    Runtime.getRuntime.addShutdownHook(t)
  }

  def nextOption(map: OptionMap, list: List[String]): OptionMap = {
    def isSwitch(s: String) = (s(0) == '-')

    list match {
      case Nil => map
      case "--logpath" :: value :: tail => nextOption(map ++ Map('logpath -> value), tail)
      case option :: tail =>
        println("Unknown option " + option)
        usage()
        sys.exit(1)
    }
  }

  /**
    * Main of this simulator, takes a sequence of integer (nbNeuronperlayer) and initiate a network
    * (neurons + synapses). If the initialization is complete, we move forward to the simulation
    * with firingSpikes()
    */
  def main(args: Array[String]): Unit = {
    val arglist = args.toList
    val options = nextOption(Map(), arglist)
    Logging.path = (options.get(Symbol.apply("logpath")).getOrElse(throw new CantOpenLogFileException(""))).toString

    val nbNeuronPerLayer = Seq(1, 2, 1)
    val net = new models.qbg.Network(nbNeuronPerLayer)

    net.initiateNetwork()
    attachShutdownHook(net)
    net.firingSpikes()
  }

}
