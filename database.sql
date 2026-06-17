--É preciso criar o banco de dados: store_application

CREATE SCHEMA vendas AUTHORIZATION postgres;

CREATE TABLE vendas.usuario (
                                id serial4 NOT NULL,
                                nome varchar NULL,
                                login varchar NULL,
                                senha varchar NULL,
                                ativo bool NOT NULL DEFAULT true,
                                CONSTRAINT pk_usuario_id PRIMARY KEY (id),
                                CONSTRAINT unique_login UNIQUE (login)
);

CREATE SEQUENCE vendas.clientes_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

CREATE SEQUENCE vendas.newtable_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

CREATE SEQUENCE vendas.venda_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1;

CREATE TABLE vendas.clientes (
                                 id serial4 NOT NULL,
                                 nome varchar NULL,
                                 telefone2 int4 NULL,
                                 usuario int4 NULL,
                                 data_nascimento date NULL,
                                 cpf varchar(11) NULL,
                                 rg varchar NULL,
                                 cep varchar(8) NULL,
                                 estado varchar(2) NULL,
                                 cidade varchar NULL,
                                 bairro varchar NULL,
                                 logradouro varchar NULL,
                                 numero int4 NULL,
                                 cod_ibge int4 NULL,
                                 telefone1 varchar NULL,
                                 CONSTRAINT pk_planta_id PRIMARY KEY (id),
                                 CONSTRAINT clientes_usuario_fkey FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);
CREATE INDEX clientes_usuario_idx ON vendas.clientes USING btree (usuario);

CREATE TABLE vendas.estoque (
                                id serial4 NOT NULL,
                                valor float8 NULL,
                                "data" date NULL,
                                usuario int4 NULL,
                                CONSTRAINT estoque_pk PRIMARY KEY (id),
                                CONSTRAINT estoque_fk FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);

CREATE TABLE vendas.forma_pagamento (
                                        id int4 NOT NULL DEFAULT nextval('vendas.newtable_id_seq'::regclass),
                                        descricao varchar NULL,
                                        credito bool NULL DEFAULT false,
                                        CONSTRAINT newtable_pk PRIMARY KEY (id)
);

CREATE TABLE vendas.venda (
                              id serial4 NOT NULL,
                              id_cliente int4 NULL,
                              valor float8 NULL,
                              qtd int4 NULL,
                              "data" date NULL,
                              usuario int4 NULL,
                              cancelada bool NULL,
                              data_hora_cancelamento timestamp NULL,
                              id_forma_pagamento int4 NULL,
                              desconto bool NULL,
                              percentual_desconto float8 NULL,
                              CONSTRAINT venda_pkey PRIMARY KEY (id),
                              CONSTRAINT venda_fk FOREIGN KEY (id_forma_pagamento) REFERENCES vendas.forma_pagamento(id),
                              CONSTRAINT venda_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES vendas.clientes(id),
                              CONSTRAINT venda_usuario_fkey FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);
CREATE INDEX venda_cliente ON vendas.venda USING btree (id_cliente);
CREATE INDEX venda_usuario_idx ON vendas.venda USING btree (usuario);

CREATE TABLE vendas.caixa_diario (
    id      serial4  PRIMARY KEY,
    data    date     NOT NULL,
    valor   float8   NOT NULL,
    usuario int4     REFERENCES vendas.usuario(id)
);

INSERT INTO vendas.forma_pagamento
(id, descricao, credito)
VALUES(1, 'DINHEIRO', false);
INSERT INTO vendas.forma_pagamento
(id, descricao, credito)
VALUES(2, 'CARTÃO CRÉDITO', true);
INSERT INTO vendas.forma_pagamento
(id, descricao, credito)
VALUES(3, 'PIX', false);
INSERT INTO vendas.forma_pagamento
(id, descricao, credito)
VALUES(4, 'CARTÃO DÉBITO', false);
