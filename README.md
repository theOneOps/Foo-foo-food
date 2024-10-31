# FoufouFood - Client Android

## Description

FoufouFood est une application mobile Android qui permet aux clients de commander des repas en
ligne et de suivre en temps réel l'évolution de leur commande jusqu'à la livraison. L'application
propose une interface utilisateur simple et intuitive pour faciliter la navigation et l'expérience
de commande.

Côté restaurateur, l'application permet de gérer les menus proposés.
Côté livreur, l'application permet de visualiser les commandes à livrer et de mettre à jour leur
statut.

Enfin une partie dédiée aux membres administrateurs permet de gérer les utilisateurs et les
restaurants.

## Contexte universitaire

La réalisation de ce projet s'inscrit dans le cadre du module IFT717 - Applications mobiles et
Internet, proposé à l'Université de Sherbrooke.
L'objectif de cette première partie du projet est de développer le client Android natif, qui
communique avec un serveur backend (Node.js) pour traiter les commandes et gérer les statuts de
livraison.

## Fonctionnalités

- Authentification des utilisateurs
- Affichage des restaurants et des menus
- Ajout de plats au panier
- Suivi de l'évolution de la commande
- Gestion des commandes pour les livreurs
- Gestion des menus pour les restaurateurs
- Gestion des utilisateurs et des restaurants pour les administrateurs
- Notifications push pour les mises à jour de statut des commandes
- Gestion des profils utilisateurs

## Installation et lancement du client Android

1. Cloner le dépôt Git
2. Ouvrir le projet dans Android Studio
3. Build le projet et synchroniser les dépendances Gradle (Normalement, Anndroid Studio le fait automatiquement, cela demande quelques minutes)
4. Modifiez le fichier `utils/Constants.kt` pour mettre à jour l'adresse IP utilisée pour les requêtes
   HTTP (L'addresse mise par défaut est celle de l'emulateur Android. Pas besoin de changer si vous utilisez l'émulateur d'Android Studio)
5. Ajouter cette même adresse IP dans le fichier `res/xml/network_security_config.xml` pour autoriser les
   requêtes HTTP (Pas besoin de changer si vous utilisez l'émulateur d'Android Studio)
6. Clonez le dépôt du serveur backend et suivez les instructions pour le lancer
7. Lancez l'application sur un émulateur ou un appareil Android (l'adresse pour lancer le projet sur
   un émulateur est : http://10.0.2.2:3000)

## Tests 

Des utilisateurs, des restaurants et des menus sont présentes côté serveur pour tester l'application.
Au lancement de l'application, vous pouvez vous connecter avec les identifiants suivants :
- Client1 : 
  - Email : johndoe@example.com 
  - Mot de passe : client123
- Client2 : 
  - Email : janedoe@example.com
  - Mot de passe : client456
- Livreur1 :
  - Email : paulsmith@example.com
  - Mot de passe : livreur123
- Livreur2 :
  - Email : alicejohnson@example.com
  - Mot de passe : livreur456
- Admin :
  - Email : admin@example.com
  - Mot de passe : admin123
- Restaurateur1 :
  - Email : restoone@example.com
  - Mot de passe : resto123
- Restaurateur2 :
  - Email : restotwo@example.com
  - Mot de passe : resto456

## Démonstration

Une vidéo de démonstration de l'application est disponible sur YouTube :
https://www.youtube.com/watch?v=IMZLieybK1g

## Remarque

Une implémentation de l'authentification avec un compte google est disponible. 
Cependant, afin de la tester, il est nécessaire de générer une clé SHA-1 pour l'application et de
l'ajouter dans la console de développement Google Cloud Platform.
Si vous voulez tester cette fonctionnalité, veuillez écrire à l'adresse suivante :
foufoufood604717@gmail.com

## Auteurs

- **Andy**
- **Selly**
- **Lili**
- **Aymeric**