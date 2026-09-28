package com.techcelladm.dal;

import com.techcelladm.util.Senha;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Confere login e senha na tabela_usuarios. A senha nunca é comparada em texto puro:
     * o banco guarda só o hash PBKDF2 (veja {@link Senha}).
     */
    public boolean autenticar(String login, char[] senha) throws SQLException {
        Connection conn = new ConexaoDAO().conectaBD();
        if (conn == null) {
            throw new SQLException("Sem conexão com o banco de dados");
        }
        String query = "SELECT senha_hash FROM tabela_usuarios WHERE login = ?";
        try (Connection conexao = conn; PreparedStatement statement = conexao.prepareStatement(query)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && Senha.verificar(senha, resultSet.getString("senha_hash"));
            }
        }
    }
}
