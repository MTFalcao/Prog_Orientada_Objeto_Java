import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class FruitSync {
    
    //------ Método para conectar no banco de dados ------------
    public static void conectarDrive(){
        String driverJDBC = "org.postgresql.Driver";
        try{
            
            System.out.println("Carregando driverJDBC no projeto, aguarde um momento...");
            
            Thread.sleep(3000);
            
            Class.forName(driverJDBC);
            System.out.println("Driver Carregado com sucesso!");
            
        }catch(Exception error){
            System.out.printf("Falha no carregamento! Erro: %s ",error);
        }
    }
    
    //------ Método para conectar no banco de dados ------------
    public static Connection conectarBD() {
        String driver = "jdbc:postgresql://127.0.0.1:5432/FruitSync";
        Connection conn = null;

        try {
            System.out.println("\nConectando ao banco de dados, aguarde um momento....");
            Thread.sleep(3000);

            conn = DriverManager.getConnection(driver, "postgres", "75716123");

            if (conn != null) {
                System.out.println("Conectado ao banco de dados!");
            } else {
                System.out.println("Falha na conexão, tente novamente...");
            }
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
        } catch (InterruptedException error) {
            System.err.format("Atraso interrompido: %s ", error.getMessage());
        }

        return conn;
    }
     
    //------ Método para criar tabelas------------
    public static void criarTabela(Connection conn){
    Scanner ler = new Scanner(System.in);
    
    System.out.println("Digite o nome da nova tabela:");
    System.out.print("-> ");
    String nomeTabela = ler.nextLine();
    
    //------ Verificando se ja existe a tabela
    try (Statement statement = conn.createStatement()) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        ResultSet resultSet = statement.executeQuery(sql);
    
        while (resultSet.next()) {
            String existingTableName = resultSet.getString("table_name");
            if (existingTableName.equalsIgnoreCase(nomeTabela)) {
                System.out.println("Nome da tabela ja existente... Tente novamente com outro nome.");
                return;
            }
        }
        
        //------ Inserindo as colunas na tabela
        List<String> colunas = new ArrayList<>();

        colunas.add("codigo INTEGER");
        System.out.println("Coluna Codigo do produto, criada com sucesso!");
        
        colunas.add("nome VARCHAR(60)");
        System.out.println("Coluna Nome do produto, criada com sucesso!");
        
        colunas.add("estoque INTEGER");
        System.out.println("Coluna Quantidade no estoque, criada com sucesso!");
        
        sql = "CREATE TABLE " + nomeTabela + " (" + String.join(", ", colunas) + ")";
        
        statement.executeUpdate(sql);
        System.out.println("\nTabela criada com sucesso!");
             
    } catch (SQLException error) {
        System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
        return;
    }
     
}   
    
    //------ Método para inserir os dados------------
    public static void inserirDados(Connection conn) {
    Scanner ler = new Scanner(System.in);

    System.out.println("Digite o nome da tabela em que deseja inserir os dados:");
    System.out.print("-> ");
    String nomeTabela = ler.nextLine().toLowerCase();

    //------ Verificando se a tabela existe
    try (Statement statement = conn.createStatement()) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
        ResultSet resultSet = statement.executeQuery(sql);

        if (!resultSet.next()) {
            System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de inserir os dados.");
            return;
        }
    
    String codigo;
        while (true) {
        System.out.print("Digite o codigo do item: ");
        codigo = ler.nextLine();

        //------ Verifica se o campo está vazio e se o numero é igual a 5 digitos até ser preenchido
        while (codigo.isEmpty() || codigo.length() != 5) {
            System.out.println("O codigo deve ter exatamente 5 digitos. Digite novamente:");
            codigo = ler.nextLine();
        }

        //------ Verificar se o código já existe na tabela.
        String selectSql = "SELECT codigo FROM " + nomeTabela + " WHERE codigo = '" + codigo + "'";
        ResultSet selectResultSet = statement.executeQuery(selectSql);

        if (!selectResultSet.next()) {
            //------ Quando codigo for valido, o loop fecha.
            break;
        }

        System.out.println("O codigo informado ja existe na tabela. Digite um codigo diferente.");
    }
    
        
    String nome;
    while (true){
    System.out.print("Digite o nome do item: ");
    nome = ler.nextLine();
    
    while (nome.isEmpty()) {
        nome = ler.nextLine();
    }
    
     //------ Verificar se o nome já existe na tabela.
    String selectSql = "SELECT nome FROM " + nomeTabela + " WHERE nome = '" + nome + "'";
    ResultSet selectResultSet = statement.executeQuery(selectSql);

    if (!selectResultSet.next()) {
        //------ Quando nome for valido, o loop fecha.
        break;
        }
        System.out.println("O nome informado ja existe na tabela. Digite um nome diferente.");
    }
    
    System.out.print("Digite a quantidade de estoque: ");
    String estoque = ler.nextLine();
  
    while (estoque.isEmpty()) {
        estoque = ler.nextLine();
    }
    
    //------ Inserindo os dados na tabela
    sql = "INSERT INTO " + nomeTabela + " (codigo, nome, estoque) VALUES ('" + codigo + "', '" + nome + "', '" + estoque + "')";
    statement.executeUpdate(sql);
    System.out.println("Item inserido com sucesso!");
        
        
    } catch (SQLException error) {
        System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
        return;
    }
    System.out.println("Insercao de dados concluida!");
}
    
    //------ Método para atualizar uma tabela ja criada
    public static void atualizarTabela(Connection conn){
     Scanner ler = new Scanner(System.in);

        System.out.println("Digite o nome da tabela em que deseja atualizar os dados:");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

         //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar atualizar os dados!");
                return;
            }
        
        
            //------ Imprimir os codigos existente     
           sql = "SELECT codigo FROM " + nomeTabela;
           resultSet = statement.executeQuery(sql);

            System.out.println("\nCodigos que existem na tabela:");

            while (resultSet.next()) {
                String codigo = resultSet.getString("codigo");
                System.out.println("> "+ codigo);
            }

            System.out.println();
            
            //------ Solicitando o código do produto
            System.out.print("Digite o codigo do produto que deseja atualizar: ");
            String codigoProduto = ler.nextLine();
            
            
            //------ Verificando se o produto existe
            sql = "SELECT * FROM " + nomeTabela + " WHERE codigo = '" + codigoProduto + "'";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("O produto com esse codigo nao existe na tabela...");
                return;
            }
            
            
            System.out.println("Qual informação deseja atualizar?");
            System.out.println("[1] - Codigo do produto");
            System.out.println("[2] - Nome do produto");
            System.out.println("[3] - Quantidade em estoque");
            System.out.println("[0] - Sair");

            System.out.print("\n-> ");
            int opcao = ler.nextInt();
            ler.nextLine();

        String coluna;
        String novoValor;

        switch (opcao) {
            
            case 1:
                
                coluna = "codigo";
                System.out.print("Digite o novo codigo do produto: ");
                novoValor = ler.nextLine();
                
                  while (novoValor.isEmpty() || novoValor.length() != 5) {
                    System.out.println("O codigo deve ter exatamente 5 digitos. Digite novamente:");
                    novoValor = ler.nextLine();
                }
                    
                break;
            case 2:
                
                coluna = "nome";
                System.out.print("Digite o novo nome do produto: ");
                novoValor = ler.nextLine();

                
                  while (novoValor.isEmpty()) {

                    novoValor = ler.nextLine();
                }
                break;
            case 3:
                
                coluna = "estoque";
                System.out.print("Digite a nova quantidade no estoque: ");
                novoValor = ler.nextLine();
                
                  while (novoValor.isEmpty()) {
                    novoValor = ler.nextLine();
                }
                break;
            case 0:
                
                System.out.println("Operacao cancelada.");
                System.out.println("Retornando ao menu...\n");
                return;
            default:
                
                System.out.println("Opcao invalida. Tente novamente... ");
                return;
        }
        
         //------ Atualizando os dados na tabela
         sql = "UPDATE " + nomeTabela + " SET " + coluna + " = '" + novoValor + "' WHERE codigo = '" + codigoProduto + "'";
         int atualizarDados = statement.executeUpdate(sql);
         
            if (atualizarDados == 1) {
                System.out.println("Dados atualizados com sucesso!");
            } else {
                System.out.println("Nenhum registro foi atualizado. Verifique se digitou código do produto corretamente");
            }
            
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }
    }
    
    //------ Método para remover uma item da tabela
    public static void removerItem(Connection conn){
        Scanner ler = new Scanner(System.in);
        
        System.out.println("Em qual tabela deseja remover o item? ");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

        //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
        ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar remove-la!");
                return;
            }
        
            //------ Imprimir os codigos existente 
            sql = "SELECT codigo FROM " + nomeTabela;
           resultSet = statement.executeQuery(sql);

            System.out.println("\nCodigos que existem na tabela:");

            while (resultSet.next()) {
                String codigo = resultSet.getString("codigo");
                System.out.println("> "+ codigo);
            }

            System.out.println();

        System.out.println("Digite o codigo do item que deseja remover:");
        System.out.print("-> ");
        String codigo = ler.nextLine();
        
        System.out.println("Tem certeza que deseja apagar o item ?");
        System.out.println("[1] - Sim");
        System.out.println("[0] - Nao");

        System.out.print("-> ");
            int opcaoRemover = ler.nextInt();
            ler.nextLine();

            switch (opcaoRemover) {
                case 1: //------ Apagar um item
                    sql = "DELETE FROM " + nomeTabela + " WHERE codigo = '" + codigo + "'";

                int deletarLinha = statement.executeUpdate(sql);
                    if (deletarLinha == 1) {
                        System.out.println("Item removido com sucesso!");
                    } else {
                        System.out.println("Codigo invalido. Verifique se o codigo digitado esta correto.");
                    }
                break;
                
                case 0:
                    System.out.println("\nOperacao cancelada. O item nao sera removido.");
                    System.out.println("Retornando ao menu...\n");
                    break;
                
                default:
                    System.out.println("Opcao invalida: " + opcaoRemover);
                    break;
            }
            
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }
    }
    
    //------ Método para remover uma tabela
    public static void removerTabela(Connection conn){
        Scanner ler = new Scanner(System.in);
        
        System.out.println("Digite qual tabela deseja remover: ");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

        //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
        ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar remove-la!");
                return;
            }
       
        System.out.println("Tem certeza que deseja apagar a tabela " + nomeTabela + "?");
        System.out.println("[1] - Sim");
        System.out.println("[0] - Não");

        System.out.print("-> ");
            int opcaoRemover = ler.nextInt();
            ler.nextLine();

            switch (opcaoRemover) {
                case 1: //------ Apagar um item

                    sql = "DROP TABLE " + nomeTabela;
                    statement.executeUpdate(sql);
                    System.out.println("Tabela removida com sucesso!");
                    break;
                
                case 0:
                    System.out.println("Operacao cancelada. A tabela nao sera removida.");
                    System.out.println("Retornando ao menu...\n");
                    break;
                
                default:
                    System.out.println("Opcao invalida: " + opcaoRemover);
                    break;
            }
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }

    }
    
    //------ Método para imprimir uma tabela
    public static void imprimirTabela(Connection conn){
        Scanner ler = new Scanner(System.in);
        
        System.out.println("Digite o nome da tabela que deseja imprimir:");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

        //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
        ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar imprimi-la!");
                return;
            }
        

        //------ Imprindo tabela
        sql = "SELECT * FROM " + nomeTabela;
        resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            do {
                System.out.println("\n--------------------------");
                System.out.println("Codigo: " + resultSet.getString("codigo"));
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Estoque: " + resultSet.getInt("estoque"));
                System.out.print("--------------------------\n");
            } while (resultSet.next());
        } else {
            System.out.println("A tabela esta vazia. Nao foi possivel imprimir nenhum dado.");
        }
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }
}
    
    //------ Método para imprimir uma tabela crescente
    public static void imprimirCrescente(Connection conn){
        Scanner ler = new Scanner(System.in);

        System.out.println("Digite o nome da tabela que deseja imprimir:");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

        //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar imprimi-la!");
                return;
            }
            
            //------ Imprimindo os dados da tabela em ordem crescente de estoque    
            sql = "SELECT * FROM " + nomeTabela + " ORDER BY estoque ASC";
            resultSet = statement.executeQuery(sql);

       if (resultSet.next()) {
            do {
                System.out.println("\n--------------------------");
                System.out.println("Codigo: " + resultSet.getString("codigo"));
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Estoque: " + resultSet.getInt("estoque"));
                System.out.print("--------------------------\n");
            } while (resultSet.next());
        } else {
            System.out.println("A tabela esta vazia. Nao foi possivel imprimir nenhum dado.");
        }
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }

    
  }
    
    //------ Método para imprimir uma tabela decrescente
    public static void imprimirDecrescente(Connection conn){
        Scanner ler = new Scanner(System.in);

        System.out.println("Digite o nome da tabela que deseja imprimir:");
        System.out.print("-> ");
        String nomeTabela = ler.nextLine().toLowerCase();

        //------ Verificando se a tabela existe
        try (Statement statement = conn.createStatement()) {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + nomeTabela + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                System.out.println("A tabela nao existe. Crie uma tabela com esse nome antes de tentar imprimi-la!");
                return;
            }
            
            //------ Imprimindo os dados da tabela em ordem Decrescente de estoque 
            sql = "SELECT * FROM " + nomeTabela + " ORDER BY estoque DESC";
            resultSet = statement.executeQuery(sql);

         if (resultSet.next()) {
            do {
                System.out.println("\n--------------------------");
                System.out.println("Codigo: " + resultSet.getString("codigo"));
                System.out.println("Nome: " + resultSet.getString("nome"));
                System.out.println("Estoque: " + resultSet.getInt("estoque"));
                System.out.print("--------------------------\n");
            } while (resultSet.next());
        } else {
            System.out.println("A tabela esta vazia. Nao foi possivel imprimir nenhum dado.");
        }
            
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
            return;
        }
    }
    
    //------ Método para imprimir lista de tabelas
    public static void listarTabelas(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("\nTabelas existentes:\n");
            while (resultSet.next()) {
                String nomeTabela = resultSet.getString("table_name");
                System.out.println("> " + nomeTabela);
            }
        } catch (SQLException error) {
            System.err.format("Ocorreu um erro durante a Operação SQL %s\n Mensagem: %s", error.getSQLState(),error.getMessage());
        }
    }

    
    public static void main(String[] args) {
        Connection conn = null;
        Scanner ler = new Scanner(System.in);
        
        //-------------- Conectando driverJDBC -------------
        conectarDrive();
        //-------------- Conectando Banco de dados -------------
        conn = conectarBD();
        
        boolean sair = false;
        while (!sair) {
            System.out.println("\n===== Bem-Vindo ao sistema FruitSync ! =====\n");
    
            System.out.println("> Qual opcao deseja selecionar ?\n");
            System.out.println("[1]- Criar tabela");
            System.out.println("[2]- Consultar tabela");
            System.out.println("[3]- Editar tabela");
            System.out.println("[0]- Sair");
            System.out.print("-> ");
            
            int opcao = ler.nextInt();
            ler.nextLine();

                switch (opcao) {
        case 1:
            boolean sairCriarTabela = false;
            while (!sairCriarTabela) {
                System.out.println("\n=== Criar tabela ===");
                                
                listarTabelas(conn);
                
                System.out.println("\n[1]- Nova tabela");
                System.out.println("[2]- Inserir dados");
                System.out.println("[0]- Sair");
                System.out.print("-> ");
                int opcaoCriarTabela = ler.nextInt();
                ler.nextLine();

                switch (opcaoCriarTabela) {
                    case 1:
                        criarTabela(conn);
                        break;
                    case 2:
                        inserirDados(conn);
                        break;
                    case 0:
                        System.out.println("Operacao cancelada.");
                        System.out.println("Retornando ao menu...\n");
                        sairCriarTabela = true;
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente... ");
                        break;
                }
            }
            break;
        case 2:
            boolean sairConsultarTabela = false;
            while (!sairConsultarTabela) {

                System.out.println("\n=== Consultar tabela ===");
                
                listarTabelas(conn);
                
                System.out.println("\n[1]- Imprimir tabela");
                System.out.println("[2]- Imprimir tabela (Estoque crescente)");
                System.out.println("[3]- Imprimir tabela (Estoque decrescente)");
                System.out.println("[0]- Sair");
                System.out.print("-> ");
                int opcaoConsultarTabela = ler.nextInt();
                ler.nextLine();

                switch (opcaoConsultarTabela) {
                    case 1:
                        imprimirTabela(conn);
                        break;
                    case 2:
                        imprimirCrescente(conn);
                        break;
                    case 3:
                        imprimirDecrescente(conn);
                        break;
                    case 0:
                        System.out.println("Operacao cancelada.");
                        System.out.println("Retornando ao menu...\n");
                        sairConsultarTabela = true;
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente... ");
                        break;
                }
            }
            break;
        case 3:
            
            boolean sairEditarTabela = false;
            while (!sairEditarTabela) {
              
                System.out.println("\n=== Editar tabela ===\n");
                                
                listarTabelas(conn);
                
                System.out.println("\n[1]- Alterar dados");
                System.out.println("[2]- Remover item");
                System.out.println("[3]- Remover tabela");
                System.out.println("[0]- Sair");
                System.out.print("-> ");
                int opcaoEditarTabela = ler.nextInt();
                ler.nextLine();

                switch (opcaoEditarTabela) {
                    case 1:
                        atualizarTabela(conn);
                        break;
                    case 2:
                        removerItem(conn);
                        break;
                    case 3:
                        removerTabela(conn);
                        break;
                    case 0:
                        System.out.println("Operacao cancelada.");
                        System.out.println("Retornando ao menu...\n");
                        sairEditarTabela = true;
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente... ");
                        break;
                }
            }
            break;
        case 0:
            System.out.println("Finalizando o programa, aguarde um momento...");
            try {
                Thread.sleep(3500);
                System.out.println("Programa finalizado com sucesso!");
                sair = true;
            } catch (InterruptedException e) {
                System.err.println("Erro ao finalizar a execucao do programa: " + e.getMessage());
            }
            break;
        default:
            System.out.println("Opcao invalida. Tente novamente... ");
            break;
    }
}        
        try {
            System.out.println("\nEncerrando conexao com o banco de dados...");
            conn.close();
            Thread.sleep(3000);
            System.out.println("Conexao com o banco de dados terminada !");           
        } catch (SQLException error) {
            System.err.println("Erro ao fechar a conexão com o banco de dados: " + error.getMessage());
        } catch (InterruptedException error) {
            System.err.format("Atraso interrompido: %s ", error.getMessage());
        }
        ler.close();
    }
    
}
