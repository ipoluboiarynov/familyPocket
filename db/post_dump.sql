--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Homebrew)
-- Dumped by pg_dump version 14.5 (Homebrew)

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
-- Name: fp_db; Type: SCHEMA; Schema: -; Owner: root
--

CREATE SCHEMA fp_db;


ALTER SCHEMA fp_db OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.account (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    icon character varying(50) NOT NULL,
    start_date date DEFAULT CURRENT_DATE,
    color character varying(7) NOT NULL,
    credit_limit numeric(10,2) DEFAULT 0 NOT NULL,
    start_balance numeric(10,2) DEFAULT 0 NOT NULL,
    account_type_id integer NOT NULL,
    user_id integer NOT NULL,
    currency_id integer NOT NULL
);


ALTER TABLE fp_db.account OWNER TO root;

--
-- Name: account_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.account ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: account_type; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.account_type (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    negative boolean DEFAULT false NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE fp_db.account_type OWNER TO root;

--
-- Name: account_type_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.account_type ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.account_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: accounts_for_filter; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.accounts_for_filter (
    account_id integer NOT NULL,
    filter_id integer NOT NULL
);


ALTER TABLE fp_db.accounts_for_filter OWNER TO root;

--
-- Name: categories_for_filter; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.categories_for_filter (
    category_id integer NOT NULL,
    filter_id integer NOT NULL
);


ALTER TABLE fp_db.categories_for_filter OWNER TO root;

--
-- Name: category; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.category (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    icon character varying(50) NOT NULL,
    is_expense boolean DEFAULT true NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE fp_db.category OWNER TO root;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.category ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: currency; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.currency (
    id integer NOT NULL,
    name character varying(3) NOT NULL,
    icon character varying(50),
    base boolean DEFAULT false NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE fp_db.currency OWNER TO root;

--
-- Name: currency_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.currency ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.currency_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: filter; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.filter (
    id integer NOT NULL,
    start_date date,
    end_date date,
    user_id integer NOT NULL,
    record_type character varying(8),
    name character varying(255) NOT NULL
);


ALTER TABLE fp_db.filter OWNER TO root;

--
-- Name: filter_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.filter ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.filter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: record; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.record (
    id integer NOT NULL,
    record_date date DEFAULT CURRENT_DATE NOT NULL,
    amount numeric(10,2) DEFAULT 0 NOT NULL,
    comment text,
    record_type character varying(8) NOT NULL,
    account_id integer NOT NULL,
    user_id integer NOT NULL,
    category_id integer
);


ALTER TABLE fp_db.record OWNER TO root;

--
-- Name: record_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.record ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: role; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.role (
    id integer NOT NULL,
    name character varying(50)
);


ALTER TABLE fp_db.role OWNER TO root;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.role ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: template; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.template (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    amount numeric(10,2) DEFAULT 0,
    category_id integer,
    user_id integer NOT NULL,
    record_type character varying(8),
    account_id integer
);


ALTER TABLE fp_db.template OWNER TO root;

--
-- Name: template_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db.template ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db."user" (
    id integer NOT NULL,
    name character varying(50),
    email character varying(50) NOT NULL,
    password character varying(60)
);


ALTER TABLE fp_db."user" OWNER TO root;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: fp_db; Owner: root
--

ALTER TABLE fp_db."user" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME fp_db.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_role; Type: TABLE; Schema: fp_db; Owner: root
--

CREATE TABLE fp_db.user_role (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE fp_db.user_role OWNER TO root;

--
-- Data for Name: account; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.account (id, name, icon, start_date, color, credit_limit, start_balance, account_type_id, user_id, currency_id) FROM stdin;
3	American Express	fab fa-cc-amex	2021-11-01	#8f7eff	2000.00	0.00	2	2	1
1	Cash USD	fas fa-wallet	2021-11-01	#50e883	0.00	2000.00	1	2	1
4	Apple credit card	fab fa-cc-apple-pay	2021-11-01	#8dddff	1500.00	0.00	2	2	1
5	US Bank Debit	fas fa-piggy-bank	2021-10-01	#42c876	0.00	10000.00	3	2	2
6	SBER credit card	far fa-credit-card	2021-11-07	#795aff	50000.00	0.00	2	2	3
2	Wells Fargo debit card	fab fa-cc-visa	2021-11-01	#ff4b52	0.00	0.00	3	2	1
\.


--
-- Data for Name: account_type; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.account_type (id, name, negative, user_id) FROM stdin;
1	Cash	f	2
3	Debit Card	f	2
2	Credit Card	t	2
4	Loan	t	2
\.


--
-- Data for Name: accounts_for_filter; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.accounts_for_filter (account_id, filter_id) FROM stdin;
\.


--
-- Data for Name: categories_for_filter; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.categories_for_filter (category_id, filter_id) FROM stdin;
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.category (id, name, icon, is_expense, user_id) FROM stdin;
1	Grocery	fas fa-shopping-basket	t	2
2	Salary	fas fa-hand-holding-usd	f	2
3	Fuel	fas fa-car	t	2
4	Connection	fas fa-mobile-alt	t	2
5	Fast Food	fas fa-hamburger	t	2
6	Clothes	fas fa-tshirt	t	2
7	Dividends	fas fa-percent	f	2
8	Second Job	fas fa-home	f	2
9	Education	fas fa-graduation-cap	t	2
\.


--
-- Data for Name: currency; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.currency (id, name, icon, base, user_id) FROM stdin;
3	RUB	fas fa-ruble-sign	f	2
2	EUR	fas fa-euro-sign	t	2
1	USD	fas fa-dollar-sign	f	2
\.


--
-- Data for Name: filter; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.filter (id, start_date, end_date, user_id, record_type, name) FROM stdin;
6	2021-11-01	2021-11-22	2	\N	Test
3	2021-11-06	2021-11-13	2	\N	New Filter 2
7	\N	\N	2	TR_IN	Filter All IN
\.


--
-- Data for Name: record; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.record (id, record_date, amount, comment, record_type, account_id, user_id, category_id) FROM stdin;
2	2021-11-03	230.54	Walmart	EXPENSE	4	2	1
4	2021-11-06	32.56	McDonald's	EXPENSE	3	2	5
5	2021-11-08	40.00	Verizon bill	EXPENSE	4	2	4
6	2021-11-10	250.34	Dividends from BTI	INCOME	2	2	7
7	2021-11-12	55.99	T-Shirt	EXPENSE	2	2	6
9	2021-11-17	580.65	Dividends from T	INCOME	2	2	7
10	2021-11-19	38.46	Culvers	EXPENSE	4	2	5
11	2021-11-21	2500.65	Second Job Cash	INCOME	1	2	8
12	2021-11-22	43.56	Exxon Mobil	EXPENSE	1	2	3
13	2021-11-22	123.34	Shoes	EXPENSE	1	2	6
8	2021-11-15	1500.04	College bill	EXPENSE	3	2	9
14	2021-10-25	1000.00	Shopping in Paris	EXPENSE	5	2	6
15	2021-11-28	1000.00	Cafe	EXPENSE	6	2	5
27	2021-11-02	100.00	28	TR_OUT	2	2	\N
28	2021-11-02	88.71	27	TR_IN	5	2	\N
31	2021-11-29	200.65	GAZPROM	INCOME	6	2	7
32	2021-11-29	2050.43	GAZPROM	INCOME	6	2	7
33	2021-11-29	10.00	34	TR_OUT	5	2	\N
34	2021-11-29	11.27	33	TR_IN	3	2	\N
35	2021-11-29	20.00	36	TR_OUT	4	2	\N
36	2021-11-29	1490.86	35	TR_IN	6	2	\N
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.role (id, name) FROM stdin;
2	USER
\.


--
-- Data for Name: template; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.template (id, name, amount, category_id, user_id, record_type, account_id) FROM stdin;
2	Verizon	40.00	4	2	EXPENSE	2
4	test	0.00	5	2	EXPENSE	1
5	new template	100.00	6	2	\N	\N
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db."user" (id, name, email, password) FROM stdin;
1	\N	new@mail.com	test
2	Ivan	test@mail.com	$2a$10$rph3EUHAI6G42vq8q.2t7.uYGwJbej6l5IKsCPsNQPGBvSEcfreQO
3	Ivan	aaa@a.ru	$2a$10$o85WneFoE9xrueMqgNK0D.mWhvKKveZMWEp.Ia4K7ouWYxP5FnEma
\.


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: fp_db; Owner: root
--

COPY fp_db.user_role (user_id, role_id) FROM stdin;
\.


--
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.account_id_seq', 8, true);


--
-- Name: account_type_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.account_type_id_seq', 4, true);


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.category_id_seq', 9, true);


--
-- Name: currency_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.currency_id_seq', 3, true);


--
-- Name: filter_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.filter_id_seq', 7, true);


--
-- Name: record_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.record_id_seq', 36, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.role_id_seq', 2, true);


--
-- Name: template_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.template_id_seq', 5, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: fp_db; Owner: root
--

SELECT pg_catalog.setval('fp_db.user_id_seq', 3, true);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: account_type account_type_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account_type
    ADD CONSTRAINT account_type_pkey PRIMARY KEY (id);


--
-- Name: accounts_for_filter accounts_for_filter_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.accounts_for_filter
    ADD CONSTRAINT accounts_for_filter_pkey PRIMARY KEY (account_id, filter_id);


--
-- Name: categories_for_filter categories_for_filter_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.categories_for_filter
    ADD CONSTRAINT categories_for_filter_pkey PRIMARY KEY (category_id, filter_id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: currency currency_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);


--
-- Name: filter filter_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.filter
    ADD CONSTRAINT filter_pkey PRIMARY KEY (id);


--
-- Name: record record_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.record
    ADD CONSTRAINT record_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: template template_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.template
    ADD CONSTRAINT template_pkey PRIMARY KEY (id);


--
-- Name: user user_email_key; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: accounts_for_filter fk_accountfilter; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.accounts_for_filter
    ADD CONSTRAINT fk_accountfilter FOREIGN KEY (account_id) REFERENCES fp_db.account(id);


--
-- Name: record fk_accountrecord; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.record
    ADD CONSTRAINT fk_accountrecord FOREIGN KEY (account_id) REFERENCES fp_db.account(id);


--
-- Name: template fk_accounttemplate; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.template
    ADD CONSTRAINT fk_accounttemplate FOREIGN KEY (account_id) REFERENCES fp_db.account(id);


--
-- Name: categories_for_filter fk_categoryfilter; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.categories_for_filter
    ADD CONSTRAINT fk_categoryfilter FOREIGN KEY (category_id) REFERENCES fp_db.category(id);


--
-- Name: record fk_categoryrecord; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.record
    ADD CONSTRAINT fk_categoryrecord FOREIGN KEY (category_id) REFERENCES fp_db.category(id);


--
-- Name: template fk_categorytemplate; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.template
    ADD CONSTRAINT fk_categorytemplate FOREIGN KEY (category_id) REFERENCES fp_db.category(id);


--
-- Name: account fk_currencyaccount; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account
    ADD CONSTRAINT fk_currencyaccount FOREIGN KEY (currency_id) REFERENCES fp_db.currency(id);


--
-- Name: accounts_for_filter fk_filteraccount; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.accounts_for_filter
    ADD CONSTRAINT fk_filteraccount FOREIGN KEY (filter_id) REFERENCES fp_db.filter(id);


--
-- Name: categories_for_filter fk_filtercategory; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.categories_for_filter
    ADD CONSTRAINT fk_filtercategory FOREIGN KEY (filter_id) REFERENCES fp_db.filter(id);


--
-- Name: account fk_typeaccount; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account
    ADD CONSTRAINT fk_typeaccount FOREIGN KEY (account_type_id) REFERENCES fp_db.account_type(id) NOT VALID;


--
-- Name: user_role fk_ur_role; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.user_role
    ADD CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES fp_db.role(id);


--
-- Name: user_role fk_ur_user; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.user_role
    ADD CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: account fk_useraccount; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account
    ADD CONSTRAINT fk_useraccount FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: account_type fk_useraccount; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.account_type
    ADD CONSTRAINT fk_useraccount FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: category fk_usercategory; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.category
    ADD CONSTRAINT fk_usercategory FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: filter fk_userfilter; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.filter
    ADD CONSTRAINT fk_userfilter FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: record fk_userrecord; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.record
    ADD CONSTRAINT fk_userrecord FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- Name: template fk_usertemplate; Type: FK CONSTRAINT; Schema: fp_db; Owner: root
--

ALTER TABLE ONLY fp_db.template
    ADD CONSTRAINT fk_usertemplate FOREIGN KEY (user_id) REFERENCES fp_db."user"(id);


--
-- PostgreSQL database dump complete
--

