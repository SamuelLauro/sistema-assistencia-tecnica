package com.techcelladm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hash de senhas com PBKDF2-HMAC-SHA256 e salt aleatório.
 *
 * O banco guarda só o texto "pbkdf2_sha256$iteracoes$salt$hash" (salt e hash em Base64),
 * nunca a senha em si.
 */
public final class Senha {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIXO = "pbkdf2_sha256";
    private static final int ITERACOES = 600_000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_HASH_BITS = 256;

    private Senha() {
    }

    public static String gerarHash(char[] senha) {
        byte[] salt = new byte[TAMANHO_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(senha, salt, ITERACOES, TAMANHO_HASH_BITS);
        Base64.Encoder base64 = Base64.getEncoder();
        return PREFIXO + "$" + ITERACOES + "$" + base64.encodeToString(salt) + "$" + base64.encodeToString(hash);
    }

    public static boolean verificar(char[] senha, String hashGuardado) {
        if (senha == null || hashGuardado == null) {
            return false;
        }
        String[] partes = hashGuardado.split("\\$");
        if (partes.length != 4 || !PREFIXO.equals(partes[0])) {
            return false;
        }
        try {
            int iteracoes = Integer.parseInt(partes[1]);
            Base64.Decoder base64 = Base64.getDecoder();
            byte[] salt = base64.decode(partes[2]);
            byte[] esperado = base64.decode(partes[3]);
            byte[] calculado = pbkdf2(senha, salt, iteracoes, esperado.length * 8);
            // comparação em tempo constante, para não vazar informação pelo tempo de resposta
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException formatoInvalido) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] senha, byte[] salt, int iteracoes, int tamanhoBits) {
        PBEKeySpec spec = new PBEKeySpec(senha, salt, iteracoes, tamanhoBits);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException erro) {
            throw new IllegalStateException("PBKDF2 indisponível nesta JVM", erro);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Gera o hash de uma senha para gravar no banco.
     * Uso: mvn -q compile exec:java -Dexec.mainClass=com.techcelladm.util.Senha -Dexec.args="nova-senha"
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: informe a senha como único argumento.");
            System.exit(1);
        }
        char[] senha = args[0].toCharArray();
        System.out.println(gerarHash(senha));
        Arrays.fill(senha, '\0');
    }
}
