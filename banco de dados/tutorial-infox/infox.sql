create database  dbinfox;

-- a linha abaixo escolhhe o banco de dados a ser utilizado	
use dbinfox;
-- o bloco de instruções abaixo cria uma tabela
create table tbesuarios(
iduser int primary key,
usuario varchar(50) not null,
fone varchar(15),
login varchar(20) not null unique,
senha varchar(15) not null
);
 -- o comando abaixo descreve a tabela 
 describe  dbinfox.tbesuarios;
 
 -- insere dados na tabela
 insert into dbinfox.tbesuarios(iduser,usuario,fone,login,senha)
 values(1,'José de Assis','9999-9999','joseassis','12345');
 
 -- exibe dados da tabela
 select * from dbinfox.tbesuarios;
 
 
 insert into dbinfox.tbesuarios(iduser,usuario,fone,login,senha)
 values(2,'Administrador','9999-9999','admin','admin');
 insert into dbinfox.tbesuarios(iduser,usuario,fone,login,senha)
 values(3,'Samuel','9999-9999','Samuellauro','12345678');
 
 
-- modifica dados na tabela 
update dbinfox.tbesuarios set fone='8888-8888' where iduser=2;

-- apaga um registro  tabela

delete from dbinfox.tbesuarios where iduser=3; 


create table tbclientes (
idcli int primary key auto_increment,
nomecli varchar(50) not null,
endcli varchar(100),
fonecli varchar(50) not null,
email varchar(50)
);

describe dbinfox.tbclientes;
 
 insert into dbinfox.tbclientes(nomecli,endcli,fonecli,email)
 values('Andre Carvalho','Rua Tux,2015','2222-2222','andre@gmail.com');
 
 select * from tbclientes;
 
 
 create table tbos(
 os int primary key auto_increment,
 data_os timestamp default current_timestamp,
 equipamento varchar(150) not null,
 defeito varchar(150) not null,
 servico varchar(150),
 tecnico varchar(150),
 valor decimal(10,2),
 idcli int not null,
 foreign key(idcli) references dbinfox.tbclientes(idcli)
 );
 
 describe tbos;
 
 insert into dbinfox.tbos(equipamento,defeito,servico,tecnico,valor,idcli)
 values ('Iphone10','Tela trincada','Troca de tela', 'Emerson', 189.90,1);
 
 delete from dbinfox.tbos where os=2;
 
 select * from dbinfox.tbos;
 
 -- traz informações de duas tabelas
 select 
 O.os,equipamento,defeito,servico,valor,
 C.nomecli,fonecli
 from tbos as O
 inner join tbclientes as C 
 on (O.idcli  = C.idcli);
 