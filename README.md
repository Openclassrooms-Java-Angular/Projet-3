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

### Modifier les paramètres MySQL :
```
spring.datasource.url=jdbc:mysql://localhost:3306/oc_java_p3?useSSL=false&serverTimezone=UTC
spring.datasource.username=
spring.datasource.password=
```

### Créer la base de données :
```
CREATE DATABASE oc_java_p3;
```

### Importer les données initiales

```mysql -u VOTRE_LOGIN -p oc_java_p3 < frontend/ressources/sql/script.sql```


## 3. Construire l'application back-end

```
cd backend
mvn clean install
```

## 4. Lancer l'application back-end

```
cd target
java -jar rental-0.0.1-SNAPSHOT.jar
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