-- Esquema usado pelo sistema TechCell (MySQL 8).
-- Pode ser executado de novo sem perder dados: só cria o que ainda não existe.
--   mysql -u root -p < "banco de dados/schema.sql"

CREATE DATABASE IF NOT EXISTS dbinfox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dbinfox;

CREATE TABLE IF NOT EXISTS tabela_usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    login      VARCHAR(50)  NOT NULL UNIQUE,
    -- formato pbkdf2_sha256$iteracoes$salt$hash (nunca a senha em texto puro)
    senha_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tabela_clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    telefone   VARCHAR(20),
    endereco   VARCHAR(150),
    email      VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tabela_produtos (
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100)  NOT NULL,
    preco      DECIMAL(10,2) NOT NULL,
    quantidade INT           NOT NULL DEFAULT 0,
    marca      VARCHAR(50),
    modelo     VARCHAR(50),
    descricao  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tabela_vendas (
    id_venda INT AUTO_INCREMENT PRIMARY KEY,
    produto  VARCHAR(100)  NOT NULL,
    preco    DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS tabela_gastos (
    id_gasto  INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(150)  NOT NULL,
    valor     DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS servicos (
    id_servico INT AUTO_INCREMENT PRIMARY KEY,
    descricao  VARCHAR(255)  NOT NULL,
    valor      DECIMAL(10,2) NOT NULL
);

-- Usuário de demonstração: login "admin", senha "admin123".
-- Antes de usar de verdade, gere um hash novo e troque:
--   mvn -q compile exec:java -Dexec.mainClass=com.techcelladm.util.Senha -Dexec.args="nova-senha"
--   UPDATE tabela_usuarios SET senha_hash = '<hash gerado>' WHERE login = 'admin';
INSERT IGNORE INTO tabela_usuarios (nome, login, senha_hash)
VALUES ('Administrador', 'admin', 'pbkdf2_sha256$600000$EUAV6s7b/6q51f7Jw/LTxw==$h0rvU90GSIM05E2MuN9k9TDmGbkmhu7bN0DNotzt1is=');
