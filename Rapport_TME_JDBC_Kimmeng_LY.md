# SAM : TME JDBC 2020  

##  Préparation  

### 1. Outils utilisés  

Il y a 2 outils distincts : un SGBD et une interface d’accès au SGBD.  
On commence par télécharger les *.jar* nécessaires : 
* h2-1.4.199.jar (pour SGBD H2)
* sqlworkbench.jar (pour l'interface graphique SQLWorkbench)

### 2. Démarrage du SGBD et de l’interface d’accès  

On démarre les deux SGBD H2 en mode “serveur” seul.  
On utilise 2 ports différents, 9093 et 9094 un pour chaque serveur.

Pour le premier serveur : 

    kimmeng@shelby:~/SAM/TME_JDBC$ java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9093 &
    [1] 6073
    TCP server running at tcp://localhost:9093 (only local connections)

Et ensuite, le deuxème serveur :  

    kimmeng@shelby:~/SAM/TME_JDBC$ java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9094 &
    [2] 6119
    TCP server running at tcp://localhost:9094 (only local connections)  

On vérifie que les deux serveurs H2 sont bien démarrés :

    kimmeng@shelby:~/SAM/TME_JDBC$ ps -u | grep h2 | grep Server
    kimmeng     6073  0.2  0.5 4608924 45476 pts/1   Sl   21:32   0:00 java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9093
    kimmeng     6119  0.3  0.5 4608924 44704 pts/1   Sl   21:34   0:00 java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9094  

On démarre SQLWorkbench pour accéder au SGBD H2 :  

    java -jar sqlworkbench.jar

On complète les champs suivants dans le profil de connexion :  
**Driver** : H2 Database Engine -> h2-1-4.199.jar.  
**URL** : jdbc:h2:tcp://localhost:9093/~/base1  
**Username** : moi  
**Password reste vide**.  

### 3. Utilisation  

Dans **SQL Workbench**, il y a 2 parties : 
* Partie supérieure : pour éditer les instructions SQL.
* Partie inférieure : pour afficher les résultats des instructions.  

## Exercice 1 : Requêtes centralisées en JDBC  

### 1. Création d’une table  

Dans **SQL Workbench**, on commence par créer 2 tables dont l'une contient une clé étrangère qui fait référence à la clé primaire de l’autre table.

``` sql
CREATE TABLE GARAGE (
    idgarage INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(255),
    ville VARCHAR(255),
    jourdefermeure VARCHAR(255),
    PRIMARY KEY (idgarage)
);

CREATE TABLE MECANICIEN (
    idpers INT NOT NULL AUTO_INCREMENT,
    idgarage INT NOT NULL,
    niveau INT,
    PRIMARY KEY (idpers),
    FOREIGN KEY (idgarage)         -- Colonne sur laquelle on crée la clé
    REFERENCES GARAGE(idgarage)    -- Colonne de référence
);
```

### 2. Remplissage des données
On remplit ensuite nos deux tables : 

```sql
INSERT INTO GARAGE (nom, ville, jourdefermeure) VALUES ('Speedy', 'Paris', 'Dimanche'),
                                                       ('AutoService', 'Tours', 'Lundi'),
                                                       ('Feu Vert', 'Paris', 'Dimanche'),
                                                       ('iDGARAGE', 'Lyon', 'Samedi'),
                                                       ('Modern Garage', 'Bordeaux', 'Lundi'),
                                                       ('Garage Auto', 'Paris', 'Dimanche'),
                                                       ('La Centrale', 'Marseille', 'Lundi'),
                                                       ('AD Garage', 'Paris', 'Dimanche'),
                                                       ('Euromaster', 'Lyon', 'Samedi'),
                                                       ('Nauroto', 'La Rochelle', 'Dimanche');
                                                
INSERT INTO MECANICIEN (idpers, idgarage, niveau) VALUES (1, 1, 2),
                                                         (2, 1, 3),
                                                         (4, 10, 2),
                                                         (5, 10, 1),
                                                         (6, 3, 2),
                                                         (8, 6, 2),
                                                         (10, 3, 1),
                                                         (11, 4, 2),
                                                         (12, 4, 2),
                                                         (13, 1, 2);
```

### 3. Premier test
Pour tester notre base de données, on va créer une classe Java puis utiliser l'API **JDBC**, on prend bien soin d'initialiser les connexions dans la clause *try* afin de profiter de l'*autoCloseable* ainsi on n'aura pas besoin de **close()** les connexions(Connection, Statement, ResultSet etc.).

**Requête** : afficher tous les garages.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercice1 {
    public static void main(String[] args) {
    	
    	String url = "jdbc:h2:tcp://localhost:9093/~/base1";
        String usr = "moi";
        String pwd = "";
        
        try(Connection connection = DriverManager.getConnection(url, usr, pwd);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM GARAGE");) { 
            
            while(resultSet.next()) {
            	System.out.print("ID: " + resultSet.getString("idgarage")+"    ");
                System.out.print("Nom: " + resultSet.getString("nom")+"    ");
                System.out.print("Ville: " + resultSet.getString("ville")+"    ");
                System.out.println("Ferme: " + resultSet.getString("jourdefermeture")+".");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

L'affichage dans la console : 

    ID: 1    Nom: Speedy    Ville: Paris    Ferme: Dimanche.
    ID: 2    Nom: AutoService    Ville: Tours    Ferme: Lundi.
    ID: 3    Nom: Feu Vert    Ville: Paris    Ferme: Dimanche.
    ID: 4    Nom: iDGARAGE    Ville: Lyon    Ferme: Samedi.
    ID: 5    Nom: Modern Garage    Ville: Bordeaux    Ferme: Lundi.
    ID: 6    Nom: Garage Auto    Ville: Paris    Ferme: Dimanche.
    ID: 7    Nom: La Centrale    Ville: Marseille    Ferme: Lundi.
    ID: 8    Nom: AD Garage    Ville: Paris    Ferme: Dimanche.
    ID: 9    Nom: Euromaster    Ville: Lyon    Ferme: Samedi.
    ID: 10    Nom: Nauroto    Ville: La Rochelle    Ferme: Dimanche.



## Exercice 2 : Jointure centralisée  

On va écrire un programme java qui implémente une jointure par boucles imbriquées.  
Une première requête sert pour l’itération principale.   
Une requête paramétrée est utilisée pour l’itération imbriquée. Pour créer une requête paramétrée on va utiliser **PreparedStatement** .

**Requête** : afficher *nom*, *prenom* et *age* de tous les mécaniciens. 

Pour cela, on va créer une table **PERSONNE** qui contient les informations personnelles des mécaniciens (les personnes dans cette table ne sont pas tous mécaniciens). 

``` sql
CREATE TABLE PERSONNE (
    idpers INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255),
    age INT,
    PRIMARY KEY (idpers)
);

INSERT INTO PERSONNE (nom, prenom, age) VALUES ('Armand', 'Rébecca', 24),
                                             ('Hebert', 'Aimée', 35),
                                             ('Ribeiro', 'Marielle', 18),
                                             ('Savary', 'Hilaire', 27),
                                             ('Dupont', 'Jean', 64),
                                             ('Dubois', 'Léo', 23),
                                             ('Mbappé', 'Kylian', 21),
                                             ('Georges', 'Antoine', 25),
                                             ('Cavani', 'Edinson', 32),
                                             ('Messi', 'Lionel', 33),
                                             ('Precieuse', 'Pierre', 45),
                                             ('Durand','Jeanne', 18),
                                             ('Zoulou','Zoé', 18),
                                             ('Pitaut','Enzo', 18),
                                             ('Hunter','Paulo', 18);
```
Notre programme Java ressemblera à ça : 

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercice2 {
    public static void main(String[] args) {
    	
    	String url = "jdbc:h2:tcp://localhost:9093/~/base1";
        String usr = "moi";
        String pwd = "";
        
        try(Connection connection = DriverManager.getConnection(url, usr, pwd);
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PERSONNE WHERE idpers = ?");
            ResultSet rsMeca = statement.executeQuery("SELECT * FROM MECANICIEN");) {
        	
            while(rsMeca.next()) {
                preparedStatement.setInt(1, rsMeca.getInt("idpers"));
                ResultSet rsPers =  preparedStatement.executeQuery();
                
                while(rsPers.next()) {
                	System.out.print("ID: " + rsPers.getString("idpers")+"   ");
                	System.out.print("Nom: " + rsPers.getString("nom")+"   ");
                	System.out.print("Prenom: " + rsPers.getString("prenom")+"   ");
                	System.out.println("Age: "+ rsPers.getString("age")+".");
                }
                rsPers.close();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

L'affichage dans la console :

    ID: 1   Nom: Armand   Prenom: Rébecca   Age: 24.
    ID: 2   Nom: Hebert   Prenom: Aimée   Age: 35.
    ID: 4   Nom: Savary   Prenom: Hilaire   Age: 27.
    ID: 5   Nom: Dupont   Prenom: Jean   Age: 64.
    ID: 6   Nom: Dubois   Prenom: Léo   Age: 23.
    ID: 8   Nom: Georges   Prenom: Antoine   Age: 25.
    ID: 10   Nom: Messi   Prenom: Lionel   Age: 33.
    ID: 11   Nom: Precieuse   Prenom: Pierre   Age: 45.
    ID: 12   Nom: Durand   Prenom: Jeanne   Age: 65.
    ID: 13   Nom: Zoulou   Prenom: Zoé   Age: 32.

**Complexité** : 

## Exercice 3 : Jointure par tri-fusion
Avec l’outil SQLWorkbench, on va créer une table dans une deuxième base: **base2**.  
Dans **base1**, la table **GARAGE** est déjà présente. Maintenant dans la **base2**, on ajoute une table **HABILITE** qui contient les spécificités des garages(quels marques les garages sont habiles de réparer).

```sql
CREATE TABLE HABILITE (
    idgarage INT NOT NULL AUTO_INCREMENT,
    marque VARCHAR(255),
    PRIMARY KEY (idgarage, marque)
);

INSERT INTO HABILITE (idgarage, marque) VALUES (1, 'Citröen'),
                                               (1, 'Peugeot'),
                                               (1, 'Nissan'),
                                               (2, 'Nissan'),
                                               (2, 'Peugeot'),
                                               (3, 'BMW'),
                                               (4, 'Mercedes'),
                                               (5, 'Peugeot'),
                                               (6, 'Citröen'),
                                               (7, 'Renault'),
                                               (7, 'Peugeot'),
                                               (8, 'Renault'),
                                               (9, 'Renault'),
                                               (10, 'Renault'),
                                               (10, 'Nissan');
```
On va maintenant écrire un programme java qui utilise 2 connections (à base1 et base2) pour implémenter une jointure réparties entre les 2 bases par une méthode de tri-fusion. La jointure se fera sur l'attribut **idgarage** entre **GARAGE** et **HABILITE**.

**Requête** : pour chaque *garage*, afficher les *marques* que le garage est habile de réparer.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercice3 {
	
	/*
	 * Fonction d'affichage les garages
	 */
	public static void affiche(String id, String nom, String ville, String marque) {
		System.out.print("ID: " + id +"    ");
		System.out.print("Nom: " + nom +"    ");
        System.out.print("Ville: " + ville +"    ");
        System.out.println("Marque: " + marque +".");
	}
	
    public static void main(String[] args) {
    	
    	String base1 = "jdbc:h2:tcp://localhost:9093/~/base1";
    	String base2 = "jdbc:h2:tcp://localhost:9093/~/base2";
        String usr = "moi";
        String pwd = "";
        
        try(Connection connection1 = DriverManager.getConnection(base1, usr, pwd);
        	Connection connection2 = DriverManager.getConnection(base2, usr, pwd);
            Statement statement1 = connection1.createStatement();
        	Statement statement2 = connection2.createStatement();	
        	/* 
        	 * On récupère directement les tables GARAGE et HABILITE triées par idgarage (par ordre croissant)
        	 * en utilisant ORDER BY
        	 */
            ResultSet rsGara = statement1.executeQuery("SELECT * FROM GARAGE ORDER BY idgarage");
        	ResultSet rsHabi =  statement2.executeQuery("SELECT * FROM HABILITE ORDER BY idgarage");) { 
        	
        	/* On récupère le premier tuple de chauque table */
        	boolean garage = rsGara.next();
        	boolean habilite = rsHabi.next();
        	
        	/* Tant que les deux tables ont au moins un tuple */
        	while(garage && habilite) {
                if(rsGara.getInt("idgarage") == rsHabi.getInt("idgarage")) {
                    affiche(rsGara.getString("idgarage"), rsGara.getString("nom"), rsGara.getString("ville"), rsHabi.getString("marque"));
                    /* On avance le curseur */
                    habilite = rsHabi.next();
                }else {
                	/* On avance le curseur */
                	garage = rsGara.next();
                }
            }
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
``` 
L'affichage dans la console : 

    ID: 1    Nom: Speedy    Ville: Paris    Marque: Citröen.
    ID: 1    Nom: Speedy    Ville: Paris    Marque: Nissan.
    ID: 1    Nom: Speedy    Ville: Paris    Marque: Peugeot.
    ID: 2    Nom: AutoService    Ville: Tours    Marque: Nissan.
    ID: 2    Nom: AutoService    Ville: Tours    Marque: Peugeot.
    ID: 3    Nom: Feu Vert    Ville: Paris    Marque: BMW.
    ID: 4    Nom: iDGARAGE    Ville: Lyon    Marque: Mercedes.
    ID: 5    Nom: Modern Garage    Ville: Bordeaux    Marque: Peugeot.
    ID: 6    Nom: Garage Auto    Ville: Paris    Marque: Citröen.
    ID: 7    Nom: La Centrale    Ville: Marseille    Marque: Peugeot.
    ID: 7    Nom: La Centrale    Ville: Marseille    Marque: Renault.
    ID: 8    Nom: AD Garage    Ville: Paris    Marque: Renault.
    ID: 9    Nom: Euromaster    Ville: Lyon    Marque: Renault.
    ID: 10    Nom: Nauroto    Ville: La Rochelle    Marque: Nissan.
    ID: 10    Nom: Nauroto    Ville: La Rochelle    Marque: Renault.

**Complexité** : 

## Exercice 4 facultatif : Jointure sur des attributs non uniques 

On veut étudier le cas d'une requête d'équi-jointure entre deux attributs qui ne sont pas uniques, par exemple entre deux clés étrangères. La différence avec l'exercice précédent est qu'il peut y avoir des valeurs qui se répètent dans les 2 attributs de jointure.  
Dans notre cas, on va faire une jointure sur l'attribut **idgarage** entre **MECANICIEN** et **HABILITE**.  
Pour simplifier, on va ajouter les attributs *nom* et *prenom* à la table **MECANICIEN**.

``` java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercice4 {
	
	/*
	 * Fonction d'affichage
	 */
	public static void affiche(String idgarage, String nom, String prenom, String niveau, String marque) {
		System.out.print("IDgarage: " + idgarage +"    ");
		System.out.print("Nom: " + nom +"    ");
		System.out.print("Prenom: " + prenom +"    ");
		System.out.print("Niveau: " + niveau +"    ");
        System.out.println("Marque: " + marque +".");
        
	}
	
    public static void main(String[] args) {
    	
    	String url1 = "jdbc:h2:tcp://localhost:9093/~/base1";
    	String url2 = "jdbc:h2:tcp://localhost:9093/~/base2";
        String usr = "moi";
        String pwd = "";
        
        try(Connection connection1 = DriverManager.getConnection(url1, usr, pwd);
        	Connection connection2 = DriverManager.getConnection(url2, usr, pwd);
        		
        	/* 
        	 * Par défaut, le type de ResultSet est TYPE_FORWARD_ONLY, donc afin de pouvoir utiliser la méthode previous() 
        	 * on doit mettre le type à TYPE_SCROLL_INSENSITIVE et la concurrence à CONCUR_READ_ONLY (qui est déjà par défaut) 
        	 */
            Statement statement1 = connection1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	Statement statement2 = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	/* 
        	 * On récupère directement les tables MECANICIEN et HABILITE triées par idgarage (par ordre croissant)
        	 * en utilisant ORDER BY
        	 */
            ResultSet rsMeca = statement1.executeQuery("SELECT * FROM MECANICIEN ORDER BY idgarage");
        	ResultSet rsHabi = statement2.executeQuery("SELECT * FROM HABILITE ORDER BY idgarage");) { 
        	
        	/* On récupère le premier tuple de chaque table */
        	boolean mecano = rsMeca.next();
        	boolean habilite = rsHabi.next();
        	/* Compteur pour stocker le nombre de next() */
        	int counter = 0;
        	
        	/* Tant que les deux tables ont au moins un tuple */
        	while(mecano && habilite) {
                if(rsMeca.getInt("idgarage") == rsHabi.getInt("idgarage")) {
                    affiche(rsMeca.getString("idgarage"), rsMeca.getString("nom"), rsMeca.getString("prenom"), rsMeca.getString("niveau"), rsHabi.getString("marque"));
                    /* On avance le curseur */
                    habilite = rsHabi.next();
                    counter++;
                    
                    /* Ici on traite le cas où on arrive à la fin de la table HABILITE */
                    if(rsHabi.isAfterLast()) {
                    	int mecanoPre = rsMeca.getInt("idgarage");
                    	/* On avance le curseur */
                    	mecano = rsMeca.next();
                    	
                    	if(mecano && mecanoPre == rsMeca.getInt("idgarage")) {
                    		while (counter > 0) {
                                /* On repositionne le curseur */
                            	habilite = rsHabi.previous();
                    			counter--;
    						}
                    	}
                    }
                }
                else if(rsMeca.getInt("idgarage") > rsHabi.getInt("idgarage")) {
                	/* On avance le curseur */
                	habilite = rsHabi.next();
                }
                else {
                	int mecanoPre = rsMeca.getInt("idgarage");
                	/* On avance le curseur */
                	mecano = rsMeca.next();
                	
                	if(mecano && mecanoPre == rsMeca.getInt("idgarage")) {
                		while (counter > 0) {
                            /* On repositionne le curseur */
                			rsHabi.previous();
                			counter--;
						}
                	}else {
                		counter = 0;
                	}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
**Complexité** : 

Ainsi dans la console, on a l'affichage suivant : 

    IDgarage: 1    Nom: Armand    Prenom: Rébecca    Niveau: 2    Marque: Citröen.
    IDgarage: 1    Nom: Armand    Prenom: Rébecca    Niveau: 2    Marque: Nissan.
    IDgarage: 1    Nom: Armand    Prenom: Rébecca    Niveau: 2    Marque: Peugeot.
    IDgarage: 1    Nom: Hebert    Prenom: Aimée    Niveau: 3    Marque: Citröen.
    IDgarage: 1    Nom: Hebert    Prenom: Aimée    Niveau: 3    Marque: Nissan.
    IDgarage: 1    Nom: Hebert    Prenom: Aimée    Niveau: 3    Marque: Peugeot.
    IDgarage: 1    Nom: Mbappé    Prenom: Kylian    Niveau: 2    Marque: Citröen.
    IDgarage: 1    Nom: Mbappé    Prenom: Kylian    Niveau: 2    Marque: Nissan.
    IDgarage: 1    Nom: Mbappé    Prenom: Kylian    Niveau: 2    Marque: Peugeot.
    IDgarage: 3    Nom: Dupont    Prenom: Jean    Niveau: 2    Marque: BMW.
    IDgarage: 3    Nom: Precieuse    Prenom: Pierre    Niveau: 1    Marque: BMW.
    IDgarage: 4    Nom: Pitaut    Prenom: Enzo    Niveau: 2    Marque: Mercedes.
    IDgarage: 4    Nom: Hunter    Prenom: Paulo    Niveau: 2    Marque: Mercedes.
    IDgarage: 6    Nom: Georges    Prenom: Antoine    Niveau: 2    Marque: Citröen.
    IDgarage: 10    Nom: Ribeiro    Prenom: Marielle    Niveau: 2    Marque: Nissan.
    IDgarage: 10    Nom: Ribeiro    Prenom: Marielle    Niveau: 2    Marque: Renault.
    IDgarage: 10    Nom: Savary    Prenom: Hilaire    Niveau: 1    Marque: Nissan.
    IDgarage: 10    Nom: Savary    Prenom: Hilaire    Niveau: 1    Marque: Renault.


## Diverses 

Pour éteindre le SGDB H2 : 

    kimmeng@shelby:~/SAM/TME_JDBC$ java -cp h2-1.4.199.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9093
    Shutting down TCP Server at tcp://localhost:9093
    [1]-  Fini                    java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9093

    kimmeng@shelby:~/SAM/TME_JDBC$ java -cp h2-1.4.199.jar org.h2.tools.Server -tcpShutdown tcp://localhost:9094
    Shutting down TCP Server at tcp://localhost:9094
    [2]+  Fini                    java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9094

Pour vérifier que tout est éteind : 

    kimmeng@shelby:~/SAM/TME_JDBC$ ps -u | grep h2 | grep Server
