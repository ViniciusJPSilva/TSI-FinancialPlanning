--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8
-- Dumped by pg_dump version 14.8

-- Started on 2023-07-09 20:57:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 211 (class 1259 OID 81923)
-- Name: seq-categoria; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."seq-categoria"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."seq-categoria" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 81952)
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    codigo bigint DEFAULT nextval('public."seq-categoria"'::regclass) NOT NULL,
    descricao character varying(256) NOT NULL
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 81924)
-- Name: seq-despesa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."seq-despesa"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."seq-despesa" OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 81960)
-- Name: despesa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.despesa (
    codigo bigint DEFAULT nextval('public."seq-despesa"'::regclass) NOT NULL,
    descricao character varying(256) NOT NULL,
    cod_categoria bigint NOT NULL
);


ALTER TABLE public.despesa OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 81922)
-- Name: seq-forma-pagamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."seq-forma-pagamento"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."seq-forma-pagamento" OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 81944)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forma_pagamento (
    codigo bigint DEFAULT nextval('public."seq-forma-pagamento"'::regclass) NOT NULL,
    descricao character varying(256) NOT NULL
);


ALTER TABLE public.forma_pagamento OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 81925)
-- Name: seq-investimento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."seq-investimento"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."seq-investimento" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 81988)
-- Name: investimento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.investimento (
    codigo bigint DEFAULT nextval('public."seq-investimento"'::regclass) NOT NULL,
    objetivo character varying(128) NOT NULL,
    estrategia character varying(64) NOT NULL,
    nome character varying(128) NOT NULL,
    valor_investido real NOT NULL,
    posicao real NOT NULL,
    rendimento_bruto real NOT NULL,
    rentabilidade real NOT NULL,
    vencimento date NOT NULL
);


ALTER TABLE public.investimento OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 81973)
-- Name: orcamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orcamento (
    mes_ano character varying(7) NOT NULL,
    cod_despesa bigint NOT NULL,
    data_despesa date NOT NULL,
    data_pagamento date,
    cod_forma_pagamento bigint NOT NULL,
    valor real NOT NULL,
    situacao boolean NOT NULL
);


ALTER TABLE public.orcamento OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 81921)
-- Name: seq-renda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."seq-renda"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."seq-renda" OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 81926)
-- Name: renda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.renda (
    codigo bigint DEFAULT nextval('public."seq-renda"'::regclass) NOT NULL,
    descricao character varying(256) NOT NULL
);


ALTER TABLE public.renda OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 81934)
-- Name: renda_mensal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.renda_mensal (
    cod_renda bigint NOT NULL,
    data date NOT NULL,
    valor real NOT NULL
);


ALTER TABLE public.renda_mensal OWNER TO postgres;

--
-- TOC entry 3208 (class 2606 OID 81959)
-- Name: categoria categoria_descricao_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_descricao_key UNIQUE (descricao);


--
-- TOC entry 3212 (class 2606 OID 81967)
-- Name: despesa despesa_descricao_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_descricao_key UNIQUE (descricao);


--
-- TOC entry 3204 (class 2606 OID 81951)
-- Name: forma_pagamento forma_pagamento_descricao_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_descricao_key UNIQUE (descricao);


--
-- TOC entry 3210 (class 2606 OID 81957)
-- Name: categoria pk_categoria; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT pk_categoria PRIMARY KEY (codigo);


--
-- TOC entry 3214 (class 2606 OID 81965)
-- Name: despesa pk_despesa; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT pk_despesa PRIMARY KEY (codigo);


--
-- TOC entry 3206 (class 2606 OID 81949)
-- Name: forma_pagamento pk_forma_pagamento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT pk_forma_pagamento PRIMARY KEY (codigo);


--
-- TOC entry 3218 (class 2606 OID 81993)
-- Name: investimento pk_investimento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.investimento
    ADD CONSTRAINT pk_investimento PRIMARY KEY (codigo);


--
-- TOC entry 3216 (class 2606 OID 81977)
-- Name: orcamento pk_orcamento; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT pk_orcamento PRIMARY KEY (mes_ano, cod_despesa);


--
-- TOC entry 3198 (class 2606 OID 81931)
-- Name: renda pk_renda; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.renda
    ADD CONSTRAINT pk_renda PRIMARY KEY (codigo);


--
-- TOC entry 3202 (class 2606 OID 81938)
-- Name: renda_mensal pk_renda_mensal; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT pk_renda_mensal PRIMARY KEY (cod_renda, data);


--
-- TOC entry 3200 (class 2606 OID 81933)
-- Name: renda renda_descricao_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.renda
    ADD CONSTRAINT renda_descricao_key UNIQUE (descricao);


--
-- TOC entry 3220 (class 2606 OID 81968)
-- Name: despesa fk_categoria; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT fk_categoria FOREIGN KEY (cod_categoria) REFERENCES public.categoria(codigo) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3221 (class 2606 OID 81978)
-- Name: orcamento fk_despesa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT fk_despesa FOREIGN KEY (cod_despesa) REFERENCES public.despesa(codigo) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3222 (class 2606 OID 81983)
-- Name: orcamento fk_forma_pagamento; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT fk_forma_pagamento FOREIGN KEY (cod_forma_pagamento) REFERENCES public.forma_pagamento(codigo) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3219 (class 2606 OID 81939)
-- Name: renda_mensal fk_renda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT fk_renda FOREIGN KEY (cod_renda) REFERENCES public.renda(codigo) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2023-07-09 20:57:41

--
-- PostgreSQL database dump complete
--

