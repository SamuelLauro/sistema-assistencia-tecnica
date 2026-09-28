package com.techcelladm.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class SenhaTest {

    @Test
    public void hashGeradoConfereComASenhaCerta() {
        String hash = Senha.gerarHash("segredo".toCharArray());
        assertTrue(Senha.verificar("segredo".toCharArray(), hash));
    }

    @Test
    public void senhaErradaNaoConfere() {
        String hash = Senha.gerarHash("segredo".toCharArray());
        assertFalse(Senha.verificar("Segredo".toCharArray(), hash));
    }

    @Test
    public void mesmaSenhaGeraHashesDiferentes() {
        // o salt é aleatório, então dois hashes da mesma senha nunca se repetem
        assertNotEquals(Senha.gerarHash("segredo".toCharArray()), Senha.gerarHash("segredo".toCharArray()));
    }

    @Test
    public void formatoInvalidoNaoConfere() {
        char[] senha = "segredo".toCharArray();
        assertFalse(Senha.verificar(senha, null));
        assertFalse(Senha.verificar(senha, "segredo"));  // senha em texto puro no banco
        assertFalse(Senha.verificar(senha, "pbkdf2_sha256$abc$salt$hash"));
        assertFalse(Senha.verificar(senha, "md5$1$c2FsdA==$aGFzaA=="));
    }

    @Test
    public void usuarioDeDemonstracaoDoSchemaConfere() throws Exception {
        // o hash do schema.sql foi gerado fora do Java; garante que os dois lados são compatíveis
        String schema = new String(Files.readAllBytes(Paths.get("banco de dados", "schema.sql")), StandardCharsets.UTF_8);
        Matcher hash = Pattern.compile("'(pbkdf2_sha256\\$[^']+)'").matcher(schema);
        assertTrue("hash do admin não encontrado no schema.sql", hash.find());
        assertTrue(Senha.verificar("admin123".toCharArray(), hash.group(1)));
        assertFalse(Senha.verificar("admin".toCharArray(), hash.group(1)));
    }
}
