public class ConectandoDriverSQL {
    static String driverJDBC = "org.postgresql.Driver";
    public static void main(String[] args){
        try{
            
            System.out.println("Carregando driver JDBC...");
            Class.forName(driverJDBC);
            System.out.println("Driver Carregado!");
            
        }catch(Exception error){
            System.out.printf("Falha no carregamento! %c",error);
        }
    }   
}
