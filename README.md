# TechCell — sistema de assistência técnica

[![build](https://github.com/SamuelLauro/sistema-assistencia-tecnica/actions/workflows/build.yml/badge.svg)](https://github.com/SamuelLauro/sistema-assistencia-tecnica/actions/workflows/build.yml)

Aplicativo desktop para o dia a dia de uma assistência técnica de celulares: clientes, ordens de serviço, produtos, vendas e gastos. Feito em **Java Swing + MySQL** (JDBC), com build pelo Maven.

## Funcionalidades

- Login com usuários guardados no banco. A senha é salva como hash PBKDF2 com salt, nunca em texto.
- Cadastro, listagem e exclusão de clientes e de produtos (preço, estoque, marca e modelo).
- Ordem de serviço: busca do cliente pelo nome e registro do serviço com o valor.
- Controle de vendas e de gastos.

## Tecnologias

Java 8+ · Swing (NetBeans GUI Builder) · MySQL 8 · JDBC com `PreparedStatement` · Maven · JUnit 4 · GitHub Actions

## Como rodar

1. Crie o banco (MySQL 8):
   ```bash
   mysql -u root -p < "banco de dados/schema.sql"
   ```
2. Configure o acesso: copie `db.properties.exemplo` para `db.properties` e preencha o usuário e a senha do MySQL, ou defina as variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD`. O `db.properties` fica fora do Git.
3. Rode com `mvn compile exec:java` ou execute `com.techcelladm.App` pela IDE.
4. Entre com o usuário de demonstração **admin / admin123** e troque a senha. Gere o hash da nova senha:
   ```bash
   mvn -q compile exec:java -Dexec.mainClass=com.techcelladm.util.Senha -Dexec.args="nova-senha"
   ```
   e grave com `UPDATE tabela_usuarios SET senha_hash = '<hash gerado>' WHERE login = 'admin';`.

## Segurança

- Nenhuma credencial no código: o acesso ao banco vem de variáveis de ambiente ou de um arquivo local ignorado pelo Git.
- Senhas com PBKDF2-HMAC-SHA256 (600 mil iterações, salt aleatório) e comparação em tempo constante, em [`util/Senha.java`](src/main/java/com/techcelladm/util/Senha.java).
- Todas as consultas usam `PreparedStatement`, inclusive a do login, e há um teste de SQL injection.
- O CI sobe um MySQL, aplica o `schema.sql` e testa o login de ponta a ponta.

## Estrutura

```
src/main/java/com/techcelladm/
├── App.java            # ponto de entrada (abre a tela de login)
├── dal/                # acesso a dados: ConexaoDAO e UsuarioDAO
├── telas/              # telas Swing: login, principal, clientes, produtos, OS, vendas e gastos
└── util/Senha.java     # hash e verificação de senhas
banco de dados/
├── schema.sql          # esquema atual do sistema
└── tutorial-infox/     # scripts e modelo da fase do tutorial (histórico)
```

## Origem

O projeto começou em outubro de 2023 a partir do tutorial **infoX**, do Prof. José de Assis; a pasta `banco de dados/tutorial-infox/` guarda os scripts dessa fase. Depois ganhou telas e tabelas próprias: produtos, vendas, gastos e ordem de serviço. A primeira versão da `ConexaoDAO` veio de uma contribuição de [@ravinoway](https://github.com/ravinoway) no [pull request #1](https://github.com/SamuelLauro/sistema-assistencia-tecnica/pull/1).

## Próximos passos

- Levar o SQL das telas para classes DAO (hoje as consultas ficam dentro das próprias telas).
- Excluir registros pelo id, e não pelo nome ou valor.
- Criar uma tela de gerenciamento de usuários.

Licença MIT.
