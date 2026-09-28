package com.techcelladm.dal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Teste de integração: precisa de um MySQL com o banco de dados/schema.sql aplicado e das
 * variáveis DB_URL, DB_USER e DB_PASSWORD (o GitHub Actions prepara tudo). Sem elas, é ignorado.
 */
public class UsuarioDAOTest {

    @Before
    public void precisaDoBanco() {
        assumeNotNull(System.getenv("DB_USER"));
    }

    @Test
    public void adminDeDemonstracaoEntra() throws Exception {
        assertTrue(new UsuarioDAO().autenticar("admin", "admin123".toCharArray()));
    }

    @Test
    public void senhaErradaNaoEntra() throws Exception {
        // era a senha fixa no código antigo da TelaLogin
        assertFalse(new UsuarioDAO().autenticar("admin", "senha123".toCharArray()));
    }

    @Test
    public void usuarioInexistenteNaoEntra() throws Exception {
        assertFalse(new UsuarioDAO().autenticar("ninguem", "admin123".toCharArray()));
    }

    @Test
    public void sqlInjectionNoLoginNaoEntra() throws Exception {
        assertFalse(new UsuarioDAO().autenticar("admin' OR '1'='1", "qualquer".toCharArray()));
    }
}
