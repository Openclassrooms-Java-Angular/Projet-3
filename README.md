# Projet ChâTop

# Pré-requis

- Java
- Maven
- MySQL
- Git
- Node.js et Angular CLI

# Installation & lancement

## 1. Cloner le projet

```
git clone https://github.com/Openclassrooms-Java-Angular/Projet-3
cd Projet-3
```

## 2. Configuration du back-end

### Éditer le fichier :  
```backend/src/main/resources/application.properties```

### Renseigner la configuration :

Dupliquer le fichier `application-secret.properties.example` en `application-secret.properties` et renseigner les paramètres :

### Créer la base de données :
```
CREATE DATABASE VOTRE_BASE;
```

### Importer les données initiales

```mysql -u VOTRE_LOGIN -p VOTRE_BASE < frontend/ressources/sql/script.sql```

### Configurer AWS

- si besoin, créer un utilisateur
- créer un bucket pour le stockage des images
- donner les droits personnalisés suivants à l'utilisateur (écriture, lecture et suppression sur le bucket) :
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowS3ReadWriteSpecificBucket",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:GetObject",
                "s3:DeleteObject"
            ],
            "Resource": "arn:aws:s3:::oc3-rental-uploads/*"
        },
        {
            "Sid": "AllowBucketListing",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucket"
            ],
            "Resource": "arn:aws:s3:::oc3-rental-uploads"
        }
    ]
}
```

## 3. Construire l'application back-end

```
cd backend
mvn clean install
```

## 4. Lancer l'application back-end

```
cd backend
./mvnw spring-boot:run
```

### API
```
http://localhost:3001/
```

### Documentation Swagger
```
http://localhost:3001/docs
```

## 5. Installation des dépendances front-end

```
cd frontend
npm install
```

## 6. Lancer le front-end Angular

```
npm start
```

Le front-end sera accessible sur :
```
http://localhost:4200
```

## 7. Accéder à l'application

### Interface utilisateur

http://localhost:4200

### API

http://localhost:3001/

### Documentation Swagger

http://localhost:3001/docs