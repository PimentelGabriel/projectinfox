package br.com.infox.dal;

import java.sql.*;

public class ModuloConexao{

    // metodo responsavel pela conexao com o bd
    // Connection - conjunto de funcionalidades (framework)
    // trazido do pacote SQL
    public static Connection conector() {

        // variavel que vai conter as informaÃ§Ãµes da conexÃ£o
        java.sql.Connection conexao = null;

        // variavel com um conector que faz referencia
        // ao drive carregado na biblioteca
        String driver = "com.mysql.jdbc.Driver";

        // variavel com o caminho de acesso ao BD = local
        String url = "jdbc:mysql://localhost:3306/dbinfox";

        // variavel com o usuario e senha do BD
        String user = "root", password = "";

        // estabelecendo a conexao com o BD
        try{
            //Executa o drive do BD que está na variavel
            Class.forName(driver);
            
            // realiza a conexão com os valores de url, user, password
            // conecta o sistema em Java com o BD MySQL
            // Salva a informação da conexão na variavel conexão
            conexao = DriverManager.getConnection(url, user, password);
            
            //Retorna o valor booleano da conexão
            return conexao;
        } catch (ClassNotFoundException | SQLException e){
            
            return null;
        }
    }
}