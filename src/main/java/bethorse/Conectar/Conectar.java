package bethorse.Conectar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Conectar {

    private static Connection conexao_MySql = null;
    private static String LINK = "jdbc:mysql://localhost:3306/bethorse";
    private static final String usuario = "root";
    private static final String senha = "";

    // Método para fazer a conexão com um banco de dados MySql
    public Connection connectionMySql() {

        try {
            conexao_MySql = DriverManager.getConnection(LINK, usuario, senha);
            System.out.println("conexão OK!");
        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu um problema na conexão com o BD", e);
        }
        return conexao_MySql;
    }

    public void consulta(Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dados");
            System.out.println("Consulta ao banco:");
            while (rs.next()) {
                System.out.println("cpf: " + rs.getString(1) + " - Nome: " + rs.getString(2) + " - Email: " + rs.getString(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void dataBaseInsert(String Nome, String Email, String cpf, String fone, String senha, String tipo) {

        Connection connection = connectionMySql();
        String sql = "INSERT INTO usuario (cpf, nome, email, senha, telefone, tipo) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStmt;
        try {
            preparedStmt = connection.prepareStatement(sql);

            //Efetua a troca do '?' pelos valores na query             
            preparedStmt.setString(1, cpf);
            preparedStmt.setString(2, Nome);
            preparedStmt.setString(3, Email);
            preparedStmt.setString(4, senha);
            preparedStmt.setString(5, fone);
            preparedStmt.setString(6, tipo);
            preparedStmt.execute();

            closeConnectionMySql(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> Logar(String email, String senha){
        Connection connection = connectionMySql();
        String sql = "SELECT * FROM usuario WHERE email = ?";
        PreparedStatement preparedStmt;

        List<String> atributos = new ArrayList<>();

        try {
            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, email);

            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                String senhaSql = rs.getString("senha");

                if (senhaSql.equals(senha)){
                    atributos.add(rs.getString("nome"));
                    atributos.add(rs.getString("email"));
                    atributos.add(rs.getString("cpf"));
                    atributos.add(rs.getString("senha"));
                    atributos.add(rs.getString("telefone"));
                    atributos.add(rs.getString("tipo"));
                } 
                
                return atributos;
            }
            
            closeConnectionMySql(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atributos;
    }

    public void closeConnectionMySql(Connection con) {
        try {
            if (con != null) {
                con.close();
                System.out.println("Fechamento OK");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu um problema para encerrar a conexão com o BD.", e);
        }
    }
}
