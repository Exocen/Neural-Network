package fr.cristal.emeraude.n2s3

import fr.cristal.emeraude.n2s3.models.timemodel.NetworkTimeModel
import fr.cristal.emeraude.n2s3.models.pji.NetworkPji
import fr.cristal.emeraude.n2s3.models.simplisticwotime.NetworkSimplisticWoTime

/**
 * Instanciate the neuronal network with akka's actors.
 * @author wgouzer & qbailleul
 */
object Main {

  def usage() {
    println("\nUsage: [VM] [PROGRAM] [OPTION]")
    println("\t e.g. java -jar n2s3.jar")
    println("\nAvailable options :")
    println("\t--debug")
    println("\t--verbose\n")
  }

  def main(args: Array[String]) = {
    // if (args.length < 0) {
    //   usage
    //   sys.error("Missing arguments")
    // }

    // instanciate the graph
    val nbNeuronPerLayer = Seq(1, 1)

    //        val net = new NetworkPji(nbNeuronPerLayer)
    //        val net = new NetworkSimplisticWoTime(nbNeuronPerLayer)
    val net = new NetworkTimeModel(nbNeuronPerLayer)

    net.initiateNetwork()
    net.firingSpikes()
  }

}
