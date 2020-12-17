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