import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectandoBDSQL{
    
  public static void main(String[] args) {
    String driver = "jdbc:postgresql://127.0.0.1:5432/DadosGerais";
    try (Connection conn = DriverManager.getConnection(driver, "postgres", "75716123")) {
      if (conn != null) {
        System.out.println("Conectado ao banco de dados!");
      } else {
        System.out.println("Falhou em fazer a conexão");
      }
    }catch (SQLException e){
      System.err.format("SQL State: %s\n%s",e.getSQLState(), e.getMessage());
    }
  }
}
