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