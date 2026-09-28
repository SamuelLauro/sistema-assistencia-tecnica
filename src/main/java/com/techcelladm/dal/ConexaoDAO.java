package com.techcelladm.dal;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;


/**
 * Abre a conexão com o MySQL. As credenciais não ficam no código: vêm das variáveis de
 * ambiente DB_URL, DB_USER e DB_PASSWORD ou, se elas não existirem, do arquivo
 * db.properties (fora do Git; copie o db.properties.exemplo).
 */
public class ConexaoDAO {

    private static final String ARQUIVO_CONFIG = "db.properties";
    private static final String URL_PADRAO = "jdbc:mysql://localhost:3306/dbinfox";

    public Connection conectaBD () {
        Connection conn = null;
        try {
            Properties config = carregarConfig();
            String url = valor("DB_URL", "db.url", config, URL_PADRAO);
            String usuario = valor("DB_USER", "db.user", config, null);
            String senha = valor("DB_PASSWORD", "db.password", config, null);
            if (usuario == null || senha == null) {
                JOptionPane.showMessageDialog(null, "Configure o acesso ao banco: defina DB_USER e DB_PASSWORD "
                        + "ou crie o arquivo " + ARQUIVO_CONFIG + " (veja " + ARQUIVO_CONFIG + ".exemplo).");
                return null;
            }
            conn = DriverManager.getConnection(url, usuario, senha);

        } catch (SQLException | IOException erro) {
            JOptionPane.showMessageDialog(null, "ConexaoDAO: " + erro.getMessage());
        }
        return conn;
    }

    private static Properties carregarConfig() throws IOException {
        Properties config = new Properties();
        File arquivo = new File(ARQUIVO_CONFIG);
        if (arquivo.isFile()) {
            try (InputStream entrada = new FileInputStream(arquivo)) {
                config.load(entrada);
            }
        }
        return config;
    }

    private static String valor(String variavelAmbiente, String chaveArquivo, Properties config, String padrao) {
        String valor = System.getenv(variavelAmbiente);
        return valor != null ? valor : config.getProperty(chaveArquivo, padrao);
    }
}
