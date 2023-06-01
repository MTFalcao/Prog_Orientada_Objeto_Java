import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ConsultandoDadosSQL {
    public static void main(String[] args) {
        String SQLconsultarDados = "Select * from pessoa";
        String driver = "jdbc:postgresql://127.0.0.1:5432/DadosGerais";
        Statement st;
        ResultSet result;
        
        try(Connection conn = DriverManager.getConnection(driver, "postgres","75716123")) {
            if(conn != null){
                System.out.println("Connected to the database!");
            }else{
                System.out.println("Failed to make connection!");
            }
            
            System.out.println("Consultando dados na Tabela...");
            st = conn.createStatement();
            result = st.executeQuery(SQLconsultarDados);
            
            while (result.next()){
            System.out.println("--------------------------");
            System.out.println("CPF: " + result.getString(1));
            System.out.println("Nome: " + result.getString(2));
        }
            
        result.close();
        st.close();
        conn.close();
        
        }catch(SQLException error){
            System.err.format("SQL State: %s\n%s", error.getSQLState(), error.getMessage());       
        }  
    }
}
