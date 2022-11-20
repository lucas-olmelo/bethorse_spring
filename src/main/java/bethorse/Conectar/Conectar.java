package bethorse.Conectar;
import java.sql.*;
import java.text.DecimalFormat;
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
                    String saldo = confereSaldo(connection, rs.getInt("idCredito"));

                    atributos.add(rs.getString("nome"));
                    atributos.add(rs.getString("email"));
                    atributos.add(rs.getString("cpf"));
                    atributos.add(rs.getString("senha"));
                    atributos.add(rs.getString("telefone"));
                    atributos.add(rs.getString("tipo"));
                    atributos.add(saldo);
                } 
                
                return atributos;
            }
            closeConnectionMySql(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atributos;
    }

    public String confereSaldo(Connection con, int idCred){
        String saldo = "";

        String sql = "SELECT saldo FROM creditos WHERE idCredito = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCred);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                saldo = df.format(rs.getDouble("saldo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saldo;
    }

    public List<String> retornaUltimasApostas(String cpf){
        Connection connection = connectionMySql();

        String sql = "SELECT * FROM aposta WHERE cpfApostador = ? ORDER BY idCc DESC LIMIT 5";
        PreparedStatement preparedStmt;

        List<String> apostas = new ArrayList<>();

        try {
            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, cpf);

            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                int idCc = rs.getInt("idCc");
                float valor = rs.getFloat("valor");

                String query = "SELECT * FROM cavalo_corrida WHERE idCavalo_Corrida = ?";
                preparedStmt = connection.prepareStatement(query);
                preparedStmt.setInt(1, idCc);

                ResultSet res = preparedStmt.executeQuery();

                while (res.next()) {
                    int idCorrida = res.getInt("idCorrida");
                    int idCavalo = res.getInt("idCavalo");

                    String sqlCavalo = "SELECT nomeCavalo FROM cavalo WHERE idCavalo = ?";
                    preparedStmt = connection.prepareStatement(sqlCavalo);
                    preparedStmt.setInt(1, idCavalo);
                    ResultSet result = preparedStmt.executeQuery();

                    while (result.next()) {
                        String cavalo = result.getString("nomeCavalo");

                        String sqlCorrida = "SELECT nomeCorrida FROM corrida WHERE idCorrida = ?";
                        preparedStmt = connection.prepareStatement(sqlCorrida);
                        preparedStmt.setInt(1, idCorrida);
                        ResultSet rset = preparedStmt.executeQuery();

                        while (rset.next()) {
                            String corrida = rset.getString("nomeCorrida");

                            DecimalFormat df = new DecimalFormat("R$#,##0.00");
                            apostas.add(cavalo + " - " + corrida + " (" + df.format(valor) + ")");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        closeConnectionMySql(connection);
        return apostas;
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
