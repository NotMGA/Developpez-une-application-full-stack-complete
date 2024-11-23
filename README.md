# README - Lancer le Frontend et le Backend du Projet

## Prérequis

Avant de lancer le projet, assurez-vous que vous avez installé les éléments suivants :

- **Node.js** (v16+)
- **Angular CLI**
- **Java** (JDK 11+)
- **Maven**
- **MySQL** (ou un autre système de gestion de base de données)

## Lancer le Backend (Spring Boot)

### 1. Configurer la base de données

- Assurez-vous que MySQL est en cours d'exécution.
- Créez une base de données nommée `mddapi` (ou celle définie dans votre fichier de configuration).
- Mettez à jour les informations de connexion dans `src/main/resources/application.properties` si nécessaire (username, password, URL).

### 2. Construire et Lancer le Backend

- Naviguez dans le répertoire du projet backend :
  ```sh
  cd back
  ```
- Utilisez Maven pour compiler le projet et lancer l'application :
  ```sh
  mvn spring-boot:run
  ```
- Le backend sera accessible par défaut sur `http://localhost:8080`.

## Lancer le Frontend (Angular)

### 1. Installer les dépendances

- Naviguez dans le répertoire du projet frontend :
  ```sh
  cd front
  ```
- Installez les dépendances avec npm :
  ```sh
  npm install
  ```

### 2. Lancer l'application Angular

- Pour lancer l'application en mode de développement :
  ```sh
  ng serve
  ```
- Le frontend sera accessible par défaut sur `http://localhost:4200`.

## Accéder à l'application

- Une fois le backend et le frontend lancés, ouvrez votre navigateur et accédez à `http://localhost:4200` pour utiliser l'application.




