package bethorse.Conectar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Conectar {

    private static Connection conexao_MySql = null;
    private static String LINK = "jdbc:mysql://localhost:3306/cavalo";
    private static final String usuario = "root";
    private static final String senha = "Senai123";

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

    public void dataBaseInsert(String Nome, String Email, String cpf, String fone, String senha, String tipo) {

        Connection connection = connectionMySql();

        int idCred = 0;
        String cred = "INSERT INTO creditos (idCredito, saldo) VALUES (null, 0)";
        try{
            PreparedStatement ps = connection.prepareStatement(cred);
            ps.execute();
            
            String query = "select max(idCredito) from creditos;";
            PreparedStatement stat = connection.prepareStatement(query);
            ResultSet rs = stat.executeQuery();

            if (rs.next()){
                idCred = rs.getInt(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        String sql = "INSERT INTO usuario (cpf, nome, email, senha, telefone, tipo, idCredito) VALUES (?,?,?,?,?,?,?)";

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
            preparedStmt.setInt(7, idCred);
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
                    atributos.add(rs.getString("idCredito"));
                } 
                
                return atributos;
            }
            
            closeConnectionMySql(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atributos;
    }

    public Double confereSaldo(Connection con){
        double saldo = 0;

        String sql = "SELECT saldo FROM credito WHERE idCredito = ?";

        return saldo;
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
