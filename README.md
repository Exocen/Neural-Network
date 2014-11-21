# SETUP
* to develop with eclipse
  * install Scala IDE for Eclipse (http://scala-ide.org/)
  * to be able to use SVN from eclipse, install the subversive eclipse plugin (just search svn in the install new software dialog)
  * install sbt (http://scala-sbt.org/)
  * in a terminal in the N2S3 directory, run "sbt eclipse"
  * in eclipse, create an new project located in the N2S3 directory
* to get the code from JSpikeStack
  * get the source code from the SVN repository
    svn checkout svn://svn.code.sf.net/p/jaer/code/trunk/host/java/subprojects/JSpikeStack/ jaer-code
  * create an eclipse project pointing to the JSpikeStack directory
  
  
# PJI

## TODO / PROGRESS
* `[In progress]` Ajouter du monitoring dans le code (modification du poids etc...) et pouvoir
     écrire la sortie dans un fichier
* `[In progress]` Modèle de temps (information de temps sur le spike `[Int]`)
   * Neuron / Synapse, mémoriser les derniers événements (derniers spikes lors d'un intervalle de
  temps)
* DSL pour la création automatique d'un réseau + import/export vers fichier
* Écrire des fichiers .aer
* Visualisations (proposer un projet de M2 IVI ?)
    * états des neurones sur une couche
    * poids des synapses
    * utiliser une architecture où on peut accrocher des "viewers" à des acteurs. 
      Si un viewer est accroché à un acteur, les événements produits par cet acteur
      sont transmits à ce viewer. On doit pouvoir ajouter et enlever des viewers
      à tout moment. Ces viewers peuvent servir au log où à la visu temps-réelle. 
      Proposition : redéfinir la méthode process du Synchronizer.
* Créer des neurones d'entrée et des neurones (ou synapses ?) de sortie 
  * langage textuel
  * fichier .aer
  
## Remarques 
* Doublons synchronizer model
* Besoin de modification du poids
* Besoin terminer la simulation proprement (terminer les acteurs, fermer les fichiers de log, etc ...)
* Refaire l'entrée du simulateur (intégrer Freeway(.aer) dans le main)
