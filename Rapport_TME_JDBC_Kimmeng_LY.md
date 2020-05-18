# TME JDBC 2020  

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
<<<<<<< HEAD
    TCP server running at tcp://localhost:9093 (only local connections)
=======
    kimmeng@shelby:~/SAM/TME_JDBC$ TCP server running at tcp://localhost:9093 (only local connections)
>>>>>>> f2b8cef7939f4b90ea2c7e11c81b31da06e78e2a

Et ensuite, le deuxème serveur :  

    kimmeng@shelby:~/SAM/TME_JDBC$ java -cp h2-1.4.199.jar org.h2.tools.Server -ifNotExists -tcp -tcpPort 9094 &
    [2] 6119
<<<<<<< HEAD
    TCP server running at tcp://localhost:9094 (only local connections)  
=======
    kimmeng@shelby:~/SAM/TME_JDBC$ TCP server running at tcp://localhost:9094 (only local connections)  
>>>>>>> f2b8cef7939f4b90ea2c7e11c81b31da06e78e2a

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

Les instructions pour consulter la base sont :  
* show tables; (affiche le nom des tables.)  
* show columns from matable; (affiche le schéma relationnel de matable.)  


## Exercice 1 : Requêtes centralisées en JDBC  

### 1. Création d’une table  

Dans **SQL Workbench**, on commence par créer 2 tables dont l'une contient une clé étrangère qui fait référence à la clé de l’autre table.

<<<<<<< HEAD
    CREATE TABLE CLIENT (
        id INT NOT NULL AUTO_INCREMENT,
        nom VARCHAR(100) NOT NULL,
        prenom VARCHAR(100),
        age INT,
        PRIMARY KEY (id)
    );

    CREATE TABLE ACHAT (
        id INT PRIMARY KEY AUTO_INCREMENT,
        client INT NOT NULL,
        produit VARCHAR(40),
        quantite SMALLINT DEFAULT 1,
        CONSTRAINT fk_client_id          -- On donne un nom à notre clé
            FOREIGN KEY (client)         -- Colonne sur laquelle on crée la clé
            REFERENCES CLIENT(id)        -- Colonne de référence
    );

### 2. Remplissage des données
On remplit ensuite nos deux tables : 

    INSERT INTO CLIENT (nom, prenom, age) VALUES ('Armand', 'Rébecca', 24),
                                                ('Hebert', 'Aimée', 35),
                                                ('Ribeiro', 'Marielle', 18),
                                                ('Savary', 'Hilaire', 27),
                                                ('Jean', 'Dupont', 64);
                                                
    INSERT INTO COMMANDE (client, produit, quantite) VALUES (1, 'Baguette', 2),
                                                            (3, 'Croissant', 1),
                                                            (4, 'Pain chocolat', 4) ;

### 3. Premier test
=======
    CREATE TABLE Client (
        numero INT NOT NULL AUTO_INCREMENT,
        nom VARCHAR(100) NOT NULL,
        prenom VARCHAR(100),
        PRIMARY KEY (numero)
    );

    CREATE TABLE Commande (
        numero INT PRIMARY KEY AUTO_INCREMENT,
        client INT NOT NULL,
        produit VARCHAR(40),
        quantite SMALLINT DEFAULT 1,
        CONSTRAINT fk_client_numero          -- On donne un nom à notre clé
            FOREIGN KEY (client)             -- Colonne sur laquelle on crée la clé
            REFERENCES Client(numero)        -- Colonne de référence
    );

>>>>>>> f2b8cef7939f4b90ea2c7e11c81b31da06e78e2a
Pour tester notre base de données, on va créer une classe Java puis utiliser l'API **JDBC** : 

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

    public class Exercice1 {
        public static void main(String[] args) {
            try {
                Connection conn = null;
                Statement stmt = null;
<<<<<<< HEAD
                
                String url = "jdbc:h2:tcp://localhost:9093/~/base1";
                String usr = "moi";
                String pwd = "";
                
                conn = DriverManager.getConnection(url, usr, pwd);
                stmt = conn.createStatement();
                
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM CLIENT"); 
                
                while(resultSet.next()) {
                    System.out.print("nom: " + resultSet.getString("nom")+", ");
                    System.out.print("prenom: " + resultSet.getString("prenom")+", ");
                    System.out.println("age: " + resultSet.getString("age"));
=======
                String url = "jdbc:h2:tcp://localhost:9093/~/base1";
            
                conn = DriverManager.getConnection(url, "moi", "");
                
                stmt = conn.createStatement();
                
                String query = "INSERT INTO Client (prenom, nom)" + 
                        " VALUES" + 
                        " ('Rébecca', 'Armand')," + 
                        " ('Aimée', 'Hebert')," + 
                        " ('Marielle', 'Ribeiro')," + 
                        " ('Hilaire', 'Savary');";
                stmt.execute(query);
                
                ResultSet resultSet = stmt.executeQuery("select * from Client"); 
                while(resultSet.next()) {
                    System.out.print("nom : " + resultSet.getString("nom")+", ");
                    System.out.println("prenom : " + resultSet.getString("prenom"));
>>>>>>> f2b8cef7939f4b90ea2c7e11c81b31da06e78e2a
                }
                
                resultSet.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

<<<<<<< HEAD

L'affichage dans la console : 

    nom: Armand, prenom: Rébecca, age: 24
    nom: Hebert, prenom: Aimée, age: 35
    nom: Ribeiro, prenom: Marielle, age: 18
    nom: Savary, prenom: Hilaire, age: 27
    nom: JEAN, prenom: DUPONT, age: 64


## Exercice 2 : Jointure centralisée  
### 1. 


## Exercice 3 : Jointure par fusion


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
=======
L'affichage dans la console : 

    nom : Armand, prenom : Rébecca
    nom : Hebert, prenom : Aimée
    nom : Ribeiro, prenom : Marielle
    nom : Savary, prenom : Hilaire


## Exercice 2 : Jointure centralisée  

>>>>>>> f2b8cef7939f4b90ea2c7e11c81b31da06e78e2a
