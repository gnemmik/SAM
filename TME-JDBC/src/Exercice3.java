import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercice3 {
	
	/*
	 * Fonction d'affichage
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
        	
        	/* On récupère le premier tuple de chaque table */
        	boolean garage = rsGara.next();
        	boolean habilite = rsHabi.next();
        	
        	/* Tant que les deux tables ont au moins un tuple */
        	while(garage && habilite) {
                if(rsGara.getInt("idgarage") == rsHabi.getInt("idgarage")) {
                    affiche(rsGara.getString("idgarage"), rsGara.getString("nom"), rsGara.getString("ville"), rsHabi.getString("marque"));
                    /* On avance le curseur */
                    habilite = rsHabi.next();
                }
                else if(rsGara.getInt("idgarage") > rsHabi.getInt("idgarage")) {
                	/* On avance le curseur */
                	habilite = rsHabi.next();
                }
                else {
                	/* On avance le curseur */
                	garage = rsGara.next();
                }
            }
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}