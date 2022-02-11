CREATE TABLE IF NOT EXISTS public.test_table1
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(255) COLLATE pg_catalog."default",
    group_id bigint,
    CONSTRAINT test_table_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;
