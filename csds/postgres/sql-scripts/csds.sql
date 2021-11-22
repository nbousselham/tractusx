--

BEGIN;

SET client_encoding = 'LATIN1';

-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id int8 NOT NULL,
	access_token varchar(255) NULL,
	email varchar(255) NULL,
	full_name varchar(255) NULL,
	"password" varchar(255) NULL,
	user_name varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- Auto-generated SQL script #202111151427
INSERT INTO public.users (id,email,full_name,"password",user_name)
	VALUES (1,'sachin.argade@t-systems.com','Sachin Argade','$2a$10$BNd8DYbKtTEtvuR.xlDo0.HjY2EMlqL5R10LpU9F9cPKOiH6yFpY6','admin');
	

-- DROP TABLE public.roles;

CREATE TABLE public.roles (
	id int8 NOT NULL,
	"role" varchar(255) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);	
	
-- Auto-generated SQL script #202111191623
INSERT INTO public.roles (id,"role")
	VALUES (1,'Recycler');
INSERT INTO public.roles (id,"role")
	VALUES (2,'Non Recycler');
INSERT INTO public.roles (id,"role")
	VALUES (3,'OEM');


-- DROP TABLE public.use_cases;

CREATE TABLE public.use_cases (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT use_cases_pkey PRIMARY KEY (id)
);

-- Auto-generated SQL script #202111191625
INSERT INTO public.use_cases (id,"name")
	VALUES (1,'Circular economy');
INSERT INTO public.use_cases (id,"name")
	VALUES (2,'Sustainability');
INSERT INTO public.use_cases (id,"name")
	VALUES (3,'Traceability');
	



