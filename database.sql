CREATE DATABASE home_fashion;




SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;




CREATE SCHEMA public;




ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 8 (class 2615 OID 457452)
-- Name: vendas; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA vendas;


ALTER SCHEMA vendas OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE vendas.usuario (
	id serial NOT NULL,
	nome varchar NULL,
	login varchar NULL,
	senha varchar NULL,
	ativo bool NOT NULL DEFAULT true,
	CONSTRAINT pk_usuario_id PRIMARY KEY (id),
	CONSTRAINT unique_login UNIQUE (login)
);


CREATE TABLE vendas.clientes (
	id serial NOT NULL,
	nome varchar NULL,
	telefone1 int4 NULL,
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
	CONSTRAINT pk_planta_id PRIMARY KEY (id),
	CONSTRAINT clientes_usuario_fkey FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);


CREATE TABLE vendas.venda (
	id serial NOT NULL,
	id_cliente int4 NULL,
	valor float8 NULL,
	qtd int4 NULL,
	"data" date NULL,
	usuario int4 NULL,
	cancelada bool NULL,
	data_hora_cancelamento timestamp NULL,
	CONSTRAINT venda_pkey PRIMARY KEY (id),
	CONSTRAINT venda_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES vendas.clientes(id),
	CONSTRAINT venda_usuario_fkey FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);
CREATE INDEX venda_cliente ON vendas.venda USING btree (id_cliente);



CREATE TABLE vendas.pagamentos (
	id serial NOT NULL,
	id_venda int4 NULL,
	valor_pago float8 NULL,
	data_pagamento date NULL,
	usuario int4 NULL,
	cancelada bool NULL,
	data_hora_cancelamento timestamp NULL,
	CONSTRAINT pagamentos_pkey PRIMARY KEY (id),
	CONSTRAINT pagamentos_id_venda_fkey FOREIGN KEY (id_venda) REFERENCES vendas.venda(id),
	CONSTRAINT pagamentos_usuario_fkey FOREIGN KEY (usuario) REFERENCES vendas.usuario(id)
);
CREATE INDEX a ON vendas.pagamentos USING btree (id_venda);
CREATE INDEX fk_usuario ON vendas.pagamentos USING btree (usuario);
