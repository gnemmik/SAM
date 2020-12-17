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