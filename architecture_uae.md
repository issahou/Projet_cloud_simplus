Pour cette partie de projet, tu dois faire comme si tu étais l'architecte cloud de l'Université Abdelmalek Essaâdi (UAE) puis modéliser cette architecture dans CloudSim Plus.

1. Documentation sur l'UAE

L'Université Abdelmalek Essaâdi :

est une université publique marocaine créée en 1989 ;
dispose de 17 établissements universitaires ;
est répartie sur 6 villes de la région Tanger-Tétouan-Al Hoceïma ;
compte environ 123 000 étudiants ;
compte environ 1 218 enseignants-chercheurs ;


Références bibliographiques
Université Abdelmalek Essaâdi, Chronologie. Disponible sur : https://www.uae.ac.ma/chronologie
Wikipédia, Université Abdelmalek Essaâdi. Disponible sur : https://fr.wikipedia.org/wiki/Universit%C3%A9_Abdelmalek_Essa%C3%A2di


Les principales villes concernées sont :

Tanger
Tétouan
Martil
Larache
Ksar El Kébir
Al Hoceïma
Services numériques existants ou envisageables

D'après les plateformes mentionnées par l'université :

ENT (Espace Numérique de Travail)
Préinscription en ligne
Plateformes de candidature Master
E-mail institutionnel
Campus connecté
E-learning / enseignement à distance

On peut compléter avec :

Bibliothèque numérique
Examens en ligne
Gestion des notes
Gestion RH
Gestion de la recherche scientifique
2. Proposition d'architecture Cloud
Choix du modèle
Déploiement

Cloud Hybride

Pourquoi ?

Les données sensibles (notes, dossiers étudiants) restent sur les serveurs de l'université.
Les pics de charge (examens et inscriptions) peuvent utiliser un cloud public.
Modèle de service
Service	Modèle
Machines virtuelles	IaaS
Plateforme e-learning	PaaS
ENT	SaaS
Messagerie	SaaS
Bibliothèque numérique	SaaS
Architecture proposée

Comme les deux plus grands pôles universitaires sont Tanger et Tétouan, on peut placer deux datacenters principaux.

Plain Text
Internet
|
Load Balancer
|
-----------------------------------------
| |
| |
Datacenter Tanger Datacenter Tétouan
| |
Serveurs VM Serveurs VM
| |
E-learning ENT + Inscriptions
Messagerie Examens en ligne
Bibliothèque Base de données
Datacenter 1 : Tanger
15 hôtes physiques

Configuration d'un hôte :

Plain Text
32 Cœurs CPU
128 Go RAM
4 To Stockage
10 Gbps Réseau

Capacité totale :

Plain Text
480 vCPU
1920 Go RAM
60 To stockage
Datacenter 2 : Tétouan
10 hôtes physiques

Configuration :

Plain Text
32 Cœurs CPU
128 Go RAM
4 To Stockage
10 Gbps

Capacité totale :

Plain Text
320 vCPU
1280 Go RAM
40 To stockage
Répartition des VM
Service	Nombre VM
ENT	8
Inscriptions	8
E-learning Moodle	12
Messagerie	6
Bibliothèque numérique	4
Examens en ligne	10
Base de données	8
Sauvegarde	4

Total :

Plain Text
60 VM

Configuration type d'une VM :

Plain Text
4 vCPU
8 Go RAM
100 Go disque
3. Modélisation des charges (Cloudlets)

Dans CloudSim Plus, les utilisateurs sont représentés par des Cloudlets.

Situation normale

Activités :

consultation des cours
consultation des notes
consultation de la bibliothèque numérique
utilisation de la messagerie

Hypothèse :

Plain Text
15 000 connexions simultanées
 

Cloudlets :

Type	Nombre
Consultation ENT	10 000
E-learning	3 000
Bibliothèque	1 000
Messagerie	1 000

Total :

Plain Text
15 000 cloudlets
Période d'inscription

Pendant les inscriptions :

Plain Text
40 000 connexions simultanées

Cloudlets :

Type	Nombre
Inscription	25 000
Consultation	10 000
Paiement	5 000

Total :

Plain Text
40 000 cloudlets
Période d'examen

Pendant les examens :

Plain Text
50 000 connexions simultanées

Cloudlets :

Type	Nombre
Passage examen	30 000
Sauvegarde réponses	10 000
Consultation sujet	10 000

Total :

Plain Text
50 000 cloudlets
Paramètres CloudSim Plus recommandés
VM
Java
4 PEs
8000 MB RAM
100000 MB Storage
1000 Mbps
Hôte
Java
32 PEs
128000 MB RAM
4000000 MB Storage
10000 Mbps
Cloudlet

Situation normale :

Java
10000 MI

Période d'inscription :

Java
20000 MI

Période d'examen :

Java
30000 MI
Conclusion

La proposition la plus crédible pour ton rapport est :

Cloud Hybride
2 Datacenters (Tanger + Tétouan)
25 hôtes physiques
60 VM
Services : ENT, Moodle, Examens, Bibliothèque, Messagerie, Inscriptions
15 000 cloudlets en période normale
40 000 cloudlets pendant les inscriptions
50 000 cloudlets pendant les examens

C'est une architecture cohérente, réaliste et directement exploitable dans CloudSim Plus pour la partie simulation.