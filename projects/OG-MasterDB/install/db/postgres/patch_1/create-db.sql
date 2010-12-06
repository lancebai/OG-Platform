-- IMPORTANT:
--
-- This file was generated by concatenating the other .sql files together. It can be
-- used for testing, but the separate SQL sequences will be necessary if the Security Master
-- and Position Master need to be installed in different databases.
--
-- Please do not modify it - modify the originals and recreate this using 'ant create-db-sql'.


    create sequence hibernate_sequence start 1 increment 1;

-- create-db-config.sql: Config Master

-- design has one document
--  config
-- unitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence cfg_config_seq
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby/HSQL, not accepted by Postgresql

create table cfg_config (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    config_type varchar(255) not null,
    config bytea not null,
    primary key (id),
    constraint cfg_chk_config_ver_order check (ver_from_instant <= ver_to_instant),
    constraint cfg_chk_config_corr_order check (corr_from_instant <= corr_to_instant)
);

create index ix_cfg_config_oid on cfg_config(oid);
create index ix_cfg_config_config_type on cfg_config(config_type);


-- create-db-refdata.sql

-- Holiday Master design has one document
--  holiday and associated dates
-- bitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence hol_holiday_seq
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby/HSQL, not accepted by Postgresql

create table hol_holiday (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    provider_scheme varchar(255),
    provider_value varchar(255),
    hol_type varchar(255) not null,
    region_scheme varchar(255),
    region_value varchar(255),
    exchange_scheme varchar(255),
    exchange_value varchar(255),
    currency_iso varchar(255),
    primary key (id),
    constraint hol_chk_holiday_ver_order check (ver_from_instant <= ver_to_instant),
    constraint hol_chk_holiday_corr_order check (corr_from_instant <= corr_to_instant)
);

create table hol_date (
    holiday_id bigint not null,
    hol_date date not null,
    constraint hol_fk_date2hol foreign key (holiday_id) references hol_holiday (id)
);

create index ix_hol_holiday_oid on hol_holiday(oid);
create index ix_hol_holiday_type on hol_holiday(hol_type);

-- Exchange Master design has one document
--  exchange and associated identifiers
-- bitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence exg_exchange_seq
    start with 1000 increment by 1 no cycle;
create sequence exg_idkey_seq
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby/HSQL, not accepted by Postgresql

create table exg_exchange (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    time_zone varchar(255),
    detail bytea not null,
    primary key (id),
    constraint exg_chk_exchange_ver_order check (ver_from_instant <= ver_to_instant),
    constraint exg_chk_exchange_corr_order check (corr_from_instant <= corr_to_instant)
);

create table exg_idkey (
    id bigint not null,
    key_scheme varchar(255) not null,
    key_value varchar(255) not null,
    primary key (id),
    constraint exg_chk_idkey unique (key_scheme, key_value)
);

create table exg_exchange2idkey (
    exchange_id bigint not null,
    idkey_id bigint not null,
    primary key (exchange_id, idkey_id),
    constraint exg_fk_exgidkey2exg foreign key (exchange_id) references exg_exchange (id),
    constraint exg_fk_exgidkey2idkey foreign key (idkey_id) references exg_idkey (id)
);
-- exg_exchange2idkey is fully dependent of exg_exchange

create index ix_exg_exchange_oid on exg_exchange(oid);


-- create-db-position.sql: Security Master

-- design has two documents
--  portfolio and tree of nodes (nested set model)
--  position and associated security key
-- bitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence pos_master_seq
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby, not accepted by Postgresql

create table pos_portfolio (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    primary key (id),
    constraint pos_chk_port_ver_order check (ver_from_instant <= ver_to_instant),
    constraint pos_chk_port_corr_order check (corr_from_instant <= corr_to_instant)
);

create table pos_node (
    id bigint not null,
    oid bigint not null,
    portfolio_id bigint not null,
    portfolio_oid bigint not null,
    parent_node_id bigint,
    depth int,
    tree_left bigint not null,
    tree_right bigint not null,
    name varchar(255),
    primary key (id),
    constraint pos_fk_node2portfolio foreign key (portfolio_id) references pos_portfolio (id),
    constraint pos_fk_node2parentnode foreign key (parent_node_id) references pos_node (id)
);
-- pos_node is fully dependent of pos_portfolio
-- portfolio_oid is an optimization (can be derived via portfolio_id)
-- parent_node_id is an optimization (tree_left/tree_right hold all the tree structure)
-- depth is an optimization (tree_left/tree_right hold all the tree structure)

create table pos_position (
    id bigint not null,
    oid bigint not null,
    portfolio_oid bigint not null,
    parent_node_oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    quantity decimal(31,8) not null,
    primary key (id),
    constraint pos_chk_posi_ver_order check (ver_from_instant <= ver_to_instant),
    constraint pos_chk_posi_corr_order check (corr_from_instant <= corr_to_instant)
);
-- portfolio_oid is an optimization

create table pos_securitykey (
    id bigint not null,
    position_id bigint not null,
    id_scheme varchar(255) not null,
    id_value varchar(255) not null,
    primary key (id),
    constraint pos_fk_securitykey2position foreign key (position_id) references pos_position (id)
);
-- pos_securitykey is fully dependent of pos_position

create table pos_trade (
    id bigint not null,
    oid bigint not null,
    position_id bigint not null,
    quantity decimal(31,8) not null,
    trade_instant timestamp not null,
    cparty_scheme varchar(255) not null,
    cparty_value varchar(255) not null,
    primary key (id),
    constraint pos_fk_trade2position foreign key (position_id) references pos_position (id)
);
-- pos_trade is fully dependent of pos_position


DROP TABLE IF EXISTS tss_identifier CASCADE;
DROP TABLE IF EXISTS tss_identification_scheme CASCADE;
DROP TABLE IF EXISTS tss_data_point CASCADE;
DROP TABLE IF EXISTS tss_data_point_delta CASCADE;
DROP TABLE IF EXISTS tss_meta_data CASCADE;
DROP TABLE IF EXISTS tss_identifier_bundle CASCADE;
DROP TABLE IF EXISTS tss_data_source CASCADE;
DROP TABLE IF EXISTS tss_data_provider CASCADE;
DROP TABLE IF EXISTS tss_data_field CASCADE;
DROP TABLE IF EXISTS tss_observation_time CASCADE;

DROP SEQUENCE IF EXISTS tss_data_field_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_data_provider_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_data_source_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_identification_scheme_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_identifier_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_observation_time_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_identifier_bundle_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tss_meta_data_id_seq CASCADE;


CREATE SEQUENCE tss_data_field_id_seq START 1;
CREATE SEQUENCE tss_data_provider_id_seq START 1;
CREATE SEQUENCE tss_data_source_id_seq START 1;
CREATE SEQUENCE tss_identification_scheme_id_seq START 1;
CREATE SEQUENCE tss_identifier_id_seq START 1;
CREATE SEQUENCE tss_observation_time_id_seq START 1;
CREATE SEQUENCE tss_identifier_bundle_id_seq START 1;
CREATE SEQUENCE tss_meta_data_id_seq START 1;

CREATE TABLE tss_data_source (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_data_source_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_data_source_id_seq OWNED BY tss_data_source.id;
CREATE UNIQUE INDEX idx_data_source_name on tss_data_source(name);

CREATE TABLE tss_data_provider (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_data_provider_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_data_provider_id_seq OWNED BY tss_data_provider.id;
CREATE UNIQUE INDEX idx_data_provider_name on tss_data_provider(name);

CREATE TABLE tss_data_field (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_data_field_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_data_field_id_seq OWNED BY tss_data_field.id;
CREATE UNIQUE INDEX idx_data_field_name on tss_data_field(name);

CREATE TABLE tss_observation_time (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_observation_time_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_observation_time_id_seq OWNED BY tss_observation_time.id;
CREATE UNIQUE INDEX idx_observation_time_name on tss_observation_time(name);

CREATE TABLE tss_identification_scheme (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_identification_scheme_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_identification_scheme_id_seq OWNED BY tss_identification_scheme.id;
CREATE UNIQUE INDEX idx_identification_scheme_name on tss_identification_scheme(name);

CREATE TABLE tss_identifier_bundle (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_identifier_bundle_id_seq'),
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
ALTER SEQUENCE tss_identifier_bundle_id_seq OWNED BY tss_identifier_bundle.id;
CREATE UNIQUE INDEX idx_identifier_bundle_name on tss_identifier_bundle(name);

CREATE TABLE tss_meta_data (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_meta_data_id_seq'),
	active INTEGER NOT NULL
	  CONSTRAINT active_constraint CHECK (active IN (0,1)),
	bundle_id BIGINT NOT NULL
	  constraint fk_meta_bundle  REFERENCES tss_identifier_bundle(id),
	data_source_id BIGINT NOT NULL
	  constraint fk_meta_data_source  REFERENCES tss_data_source(id),
	data_provider_id BIGINT NOT NULL
	  constraint fk_meta_data_provider  REFERENCES tss_data_provider(id),
	data_field_id BIGINT NOT NULL
	  constraint fk_meta_data_field  REFERENCES tss_data_field(id),
	observation_time_id BIGINT NOT NULL
	  constraint fk_meta_observation_time  REFERENCES tss_observation_time(id)
);
ALTER SEQUENCE tss_meta_data_id_seq OWNED BY tss_meta_data.id;
CREATE INDEX idx_meta_data ON tss_meta_data (active, data_source_id, data_provider_id, data_field_id, observation_time_id);

CREATE TABLE tss_data_point (
	meta_data_id BIGINT NOT NULL
	  constraint fk_dp_meta_data  REFERENCES tss_meta_data (id),
	ts_date date NOT NULL,
	value DOUBLE PRECISION NOT NULL,
	PRIMARY KEY (meta_data_id, ts_date)
);

CREATE TABLE tss_data_point_delta (
	meta_data_id BIGINT NOT NULL
	  constraint fk_dp_delta_meta_data  REFERENCES tss_meta_data (id),
	time_stamp TIMESTAMP NOT NULL,
	ts_date date NOT NULL,
	old_value DOUBLE PRECISION NOT NULL,
	operation char(1) NOT NULL
	 CONSTRAINT operation_constraint CHECK ( operation IN ('I', 'U', 'D', 'Q'))
);

CREATE TABLE tss_intraday_data_point (
	meta_data_id BIGINT NOT NULL
	  constraint fk_i_dp_meta_data  REFERENCES tss_meta_data (id),
	ts_date TIMESTAMP NOT NULL,
	value DOUBLE PRECISION NOT NULL,
	PRIMARY KEY (meta_data_id, ts_date)
);

CREATE TABLE tss_intraday_data_point_delta (
	meta_data_id BIGINT NOT NULL
	  constraint fk_i_dp_delta_meta_data  REFERENCES tss_meta_data (id),
	time_stamp TIMESTAMP NOT NULL,
	ts_date TIMESTAMP NOT NULL,
	old_value DOUBLE PRECISION NOT NULL,
	operation char(1) NOT NULL
	 CONSTRAINT operation_constraint_i CHECK ( operation IN ('I', 'U', 'D', 'Q'))
);

CREATE TABLE tss_identifier (
	id BIGINT NOT NULL
	  PRIMARY KEY
	  DEFAULT nextval('tss_identifier_id_seq'),
	bundle_id BIGINT NOT NULL
	  constraint fk_identifier_bundle  REFERENCES tss_identifier_bundle(id),
	identification_scheme_id BIGINT NOT NULL
	  constraint fk_identifier_identification_scheme  REFERENCES tss_identification_scheme(id),
	identifier_value VARCHAR(255) NOT NULL,
	valid_from date,
	valid_to date
);

ALTER SEQUENCE tss_identifier_id_seq OWNED BY tss_identifier.id;
CREATE INDEX idx_identifier_scheme_value on tss_identifier (identification_scheme_id, identifier_value);
CREATE INDEX idx_identifier_value ON tss_identifier(identifier_value);

