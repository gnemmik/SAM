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

Les instructions pour consulter la base sont :  
* show tables; (affiche le nom des tables.)  
* show columns from matable; (affiche le schéma relationnel de matable.)  


## Exercice 1 : Requêtes centralisées en JDBC  

### 1. Création d’une table  

Dans **SQL Workbench**, on commence par créer 2 tables dont l'une contient une clé étrangère qui fait référence à la clé de l’autre table.

    CREATE TABLE CLIENT (
        id INT NOT NULL AUTO_INCREMENT,
        nom VARCHAR(100) NOT NULL,
        prenom VARCHAR(100),
        age INT,
        PRIMARY KEY (id)
    );

    CREATE TABLE COMMANDE (
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
                                                 ('Dupont', 'Jean', 64),
                                                 ('Dubois', 'Léo', 23),
                                                 ('Mbappé', 'Kylian', 21),
                                                 ('Georges', 'Antoine', 25),
                                                 ('Cavani', 'Edinson', 32),
                                                 ('Messi', 'Lionel', 33),
                                                 ('Precieuse', 'Pierre', 45),
                                                 ('Sensei','Koro', 18);
                                                
    INSERT INTO COMMANDE (client, produit, quantite) VALUES (1, 'Baguette', 2),
                                                            (3, 'Croissant', 1),
                                                            (4, 'Pain chocolat', 4)
                                                            (10, 'Baguette', 5),
                                                            (7, 'Baguette tradition', 1);

### 3. Premier test
Pour tester notre base de données, on va créer une classe Java puis utiliser l'API **JDBC**, on prend bien soin d'initialiser les connexions dans la clausse *try* afin de profiter de l'*autoCloseable* ainsi on aura pas besoin de **close()** les connexions(Connection, Statement, ResultSet etc.).

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
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CLIENT");) { 
                
                while(resultSet.next()) {
                    System.out.print("ID: " + resultSet.getString("id")+"    ");
                    System.out.print("Nom: " + resultSet.getString("nom")+"    ");
                    System.out.print("Prenom: " + resultSet.getString("prenom")+"    ");
                    System.out.println("Age: " + resultSet.getString("age")+".");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

L'affichage dans la console : 

    ID: 1    Nom: Armand    Prenom: Rébecca    Age: 24.
    ID: 2    Nom: Hebert    Prenom: Aimée    Age: 35.
    ID: 3    Nom: Ribeiro    Prenom: Marielle    Age: 18.
    ID: 4    Nom: Savary    Prenom: Hilaire    Age: 27.
    ID: 5    Nom: Dupont    Prenom: Jean    Age: 64.
    ID: 6    Nom: Dubois    Prenom: Léo    Age: 23.
    ID: 7    Nom: Mbappé    Prenom: Kylian    Age: 21.
    ID: 8    Nom: Georges    Prenom: Antoine    Age: 25.
    ID: 9    Nom: Cavani    Prenom: Edinson    Age: 32.
    ID: 10    Nom: Messi    Prenom: Lionel    Age: 33.
    ID: 11    Nom: Precieuse    Prenom: Pierre    Age: 45.
    ID: 12    Nom: Sensei    Prenom: Koro    Age: 18.


## Exercice 2 : Jointure centralisée  

On va écrire un programme java qui implémente une jointure par boucles imbriquées.  
Une première requête sert pour l’itération principale.   
Une requête paramétrée est utilisée pour l’itération imbriquée. Pour créer une requête paramétrée on va utiliser **PreparedStatement** .

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
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM COMMANDE WHERE client = ?");
                ResultSet rsClient = statement.executeQuery("SELECT * FROM CLIENT");) {
                
                while(rsClient.next()) {
                    
                    preparedStatement.setInt(1, rsClient.getInt("id"));
                    ResultSet rsCommande =  preparedStatement.executeQuery();
                    
                    while(rsCommande.next()) {
                        System.out.print("Nom: " + rsClient.getString("nom")+", ");
                        System.out.print("Prenom: " + rsClient.getString("prenom")+", ");
                        System.out.print("Produit: "+ rsCommande.getString("produit")+", ");
                        System.out.println("Quantité: "+ rsCommande.getString("quantite")+".");
                    }
                    rsCommande.close();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

L'affichage dans la console :

    Nom: Armand, Prenom: Rébecca, Produit: Baguette, Quantité: 2.
    Nom: Ribeiro, Prenom: Marielle, Produit: Croissant, Quantité: 1.
    Nom: Savary, Prenom: Hilaire, Produit: Pain chocolat, Quantité: 4.
    Nom: Mbappé, Prenom: Kylian, Produit: Baguette tradition, Quantité: 1.
    Nom: Messi, Prenom: Lionel, Produit: Baguette, Quantité: 5.

## Exercice 3 : Jointure par fusion
Dans cet exercice, on va répartir les données de notre table CLIENT sur deux bases.
Avec l’outil SQLWorkbench, on va créer une table dans chaque base, base1 et base2.  
On crée dans base1, la table CLIENT1 qui contient une partie des données des clients.

    INSERT INTO CLIENT1 (id, nom, prenom, age) VALUES (1, 'Armand', 'Rébecca', 24),
                                                      (3, 'Ribeiro', 'Marielle', 18),
                                                      (5, 'Dupont', 'Jean', 64), 
                                                      (7, 'Mbappé', 'Kylian', 21),
                                                      (9, 'Cavani', 'Edinson', 32);

Et dans la base2, la table CLIENT2 qui contient le reste des données des clients.

    INSERT INTO CLIENT2 (id, nom, prenom, age) VALUES (2, 'Hebert', 'Aimée', 35),
                                                      (4, 'Savary', 'Hilaire', 27),
                                                      (6, 'Dubois', 'Léo', 23),
                                                      (8, 'Georges', 'Antoine', 25),
                                                      (10, 'Messi', 'Lionel', 33),
                                                      (11, 'Precieuse', 'Pierre', 35),
                                                      (12, 'Sensei', 'Koro', 18);

On va maintenat écrire un programme java qui utilise 2 connections (à base1 et base2) pour implémenter une jointure réparties entre les 2 bases par une méthode de tri-fusion. La jointure se fera sur l'attribut **id** qui est unique(il n'y aura pas deux fois le même **id**). 

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

    public class Exercice3 {
        
        /*
        * Fonction d'affichage pour chaque tuple de la table CLIENT
        */
        public static void join(String id, String nom, String prenom, String age) {
            System.out.print("ID: " + id +"    ");
            System.out.print("Nom: " + nom +"    ");
            System.out.print("Prenom: " + prenom +"    ");
            System.out.println("Age: " + age +".");
        }
        
        public static void main(String[] args) {
            
            String url1 = "jdbc:h2:tcp://localhost:9093/~/base1";
            String url2 = "jdbc:h2:tcp://localhost:9093/~/base2";
            String usr = "moi";
            String pwd = "";
            
            try(Connection connection1 = DriverManager.getConnection(url1, usr, pwd);
                Connection connection2 = DriverManager.getConnection(url2, usr, pwd);
                Statement statement1 = connection1.createStatement();
                Statement statement2 = connection2.createStatement();
                
                /* 
                * On récupère directement les deux tables CLIENT1 et CLIENT2 triés par ID (par ordre croissant)
                * en utilisant ORDER BY
                */
                ResultSet rsClient1 = statement1.executeQuery("SELECT * FROM CLIENT1 ORDER BY ID");
                ResultSet rsClient2 = statement2.executeQuery("SELECT * FROM CLIENT2 ORDER BY ID");) { 
                
                while(rsClient1.next() && rsClient2.next()) {
                    /* Si CLIENT1.ID < CLIENT2.ID */
                    if(rsClient1.getInt("id") < rsClient2.getInt("id")) {
                        join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                        join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                    } 
                    /* Si CLIENT1.ID < CLIENT2.ID */
                    else {
                        join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                        join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                    }
                }
                
                /* Arrivé ici, au moins l'une des deux tables est vide 
                * On va donc récupérer tous les éléments qui restent
                */
                
                /* On récupère tous les tuples restant dans CLIENT1 */
                while(rsClient1.next()) {
                    join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                }
                /* On récupère tous les tuples restant dans CLIENT2 */
                while(rsClient2.next()) {
                    join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

L'affichage dans la console : 

    ID: 1    Nom: Armand    Prenom: Rébecca    Age: 24.
    ID: 2    Nom: Hebert    Prenom: Aimée    Age: 35.
    ID: 3    Nom: Ribeiro    Prenom: Marielle    Age: 18.
    ID: 4    Nom: Savary    Prenom: Hilaire    Age: 27.
    ID: 5    Nom: Dupont    Prenom: Jean    Age: 64.
    ID: 6    Nom: Dubois    Prenom: Léo    Age: 23.
    ID: 7    Nom: Mbappé    Prenom: Kylian    Age: 21.
    ID: 8    Nom: Georges    Prenom: Antoine    Age: 25.
    ID: 9    Nom: Cavani    Prenom: Edinson    Age: 32.
    ID: 10    Nom: Messi    Prenom: Lionel    Age: 33.
    ID: 11    Nom: Precieuse    Prenom: Pierre    Age: 35.
    ID: 12    Nom: Sensei    Prenom: Koro    Age: 18.


## Exercice 4 facultatif : Jointure sur des attributs non uniques 

On veut étudier le cas d'une requête d'equi-jointure entre deux attributs qui ne sont pas uniques, par exemple entre deux **ages**. La différence avec l'exercice précédent est qu'il peut y avoir des valeurs qui se répètent dans les 2 attributs de jointure.  
Dans notre cas, on va faire une jointure sur l'attribut **age** et certains *clients* ont le même âge, on devra donc faire attention à tous les traiter.

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

    public class Exercice4 {
        
        /*
        * Fonction d'affichage pour chaque tuple de la table CLIENT
        */
        public static void join(String id, String nom, String prenom, String age) {
            System.out.print("Age: " + age +"    ");
            System.out.print("ID: " + id +"    ");
            System.out.print("Nom: " + nom +"    ");
            System.out.println("Prenom: " + prenom +".");
            
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
                * On récupère directement les deux tables CLIENT1 et CLIENT2 triés par AGE (par ordre croissant)
                * en utilisant ORDER BY
                */
                ResultSet rsClient1 = statement1.executeQuery("SELECT * FROM CLIENT1 ORDER BY AGE");
                ResultSet rsClient2 = statement2.executeQuery("SELECT * FROM CLIENT2 ORDER BY AGE");) { 
                
                while(rsClient1.next() && rsClient2.next()) {
                    /* On regoupe les doublons */
                    if(rsClient1.getInt("age") == rsClient2.getInt("age")) {
                        join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                        join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                    } 
                    /* Replacer le curseur de Client2 */
                    else if(rsClient1.getInt("age") < rsClient2.getInt("age")) {
                        join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                        rsClient2.previous();
                    } 
                    /* Replacer le curseur de Client1 */
                    else {
                        join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                        rsClient1.previous();
                    }
                    
                    /* Test pour savoir si c'était le dernier tuple de CLIENT1 */
                    if(!rsClient1.next()) {
                        /* Si oui, on récupère tous les tuples restant dans CLIENT2 */
                        while(rsClient2.next()) {
                            join(rsClient2.getString("id"), rsClient2.getString("nom"), rsClient2.getString("prenom"), rsClient2.getString("age"));
                        }
                    }else {
                        /* Sinon, replacer le curseur */
                        rsClient1.previous();
                    }
                    
                    /* Test pour savoir si c'était le dernier élément de CLIENT2 */
                    if(!rsClient2.next()) {
                        /* Si oui, on récupère tous les tuples restant dans CLIENT1 */
                        while(rsClient1.next()) {
                            join(rsClient1.getString("id"), rsClient1.getString("nom"), rsClient1.getString("prenom"), rsClient1.getString("age"));
                        }
                    }else {
                        /* Sinon, replacer le curseur */
                        rsClient2.previous();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

Ainsi dans la console, on a l'affichage suivant : 

    Age: 18    ID: 3    Nom: Ribeiro    Prenom: Marielle.
    Age: 18    ID: 12    Nom: Sensei    Prenom: Koro.
    Age: 21    ID: 7    Nom: Mbappé    Prenom: Kylian.
    Age: 23    ID: 6    Nom: Dubois    Prenom: Léo.
    Age: 24    ID: 1    Nom: Armand    Prenom: Rébecca.
    Age: 25    ID: 8    Nom: Georges    Prenom: Antoine.
    Age: 27    ID: 4    Nom: Savary    Prenom: Hilaire.
    Age: 32    ID: 9    Nom: Cavani    Prenom: Edinson.
    Age: 33    ID: 10    Nom: Messi    Prenom: Lionel.
    Age: 35    ID: 2    Nom: Hebert    Prenom: Aimée.
    Age: 35    ID: 11    Nom: Precieuse    Prenom: Pierre.
    Age: 64    ID: 5    Nom: Dupont    Prenom: Jean.

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
