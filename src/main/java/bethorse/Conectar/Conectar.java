package bethorse.Conectar;
import java.sql.*;

public class Conectar {

    private static Connection conexao_MySql = null;
    private static String localBD = "localhost";
    private static String LINK = "jdbc:mysql://" + localBD + ":3306/springmongo";
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

    public void consulta(Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dados");
            System.out.println("Consulta ao banco:");
            while (rs.next()) {
                System.out.println("cod: " + rs.getInt(1) + " - Nome: " + rs.getString(2) + " - Email: " + rs.getString(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String dataBaseSelect(int code) {
        Connection connection = connectionMySql();
        String x = "";
        String sql = "Select codigo, nome, email "
                + "from dados "
                + "where codigo=?";

        PreparedStatement preparedStmt;
        try {
            preparedStmt = connection.prepareStatement(sql);

            //Efetua a troca do '?' pelos valores na query
            preparedStmt.setInt(1, code);
            ResultSet result = preparedStmt.executeQuery();

            //valida resultado
            while (result.next()) {
                int cod = result.getInt("codigo");
                String name = result.getString("nome");
                String name2 = result.getString("email");
                System.out.println("cod: " + cod);
                System.out.println("name: " + name);
                System.out.println("email : " + name2);
                x = name;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return x;
    }

    public void dataBaseInsert(String Nome, String Email) {

        Connection connection = connectionMySql();
        String sql = "INSERT INTO dados (nome, email) VALUES (?,?)";
        PreparedStatement preparedStmt;
        try {
            preparedStmt = connection.prepareStatement(sql);

            //Efetua a troca do '?' pelos valores na query             
            preparedStmt.setString(1, Nome);
            preparedStmt.setString(2, Email);
            preparedStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
