CREATE DATABASE `SAMPLE_DB`;
USE `SAMPLE_DB`;

--
-- Table structure for table `PEAK`
--
CREATE TABLE PEAK (
	ID          char(8)  NOT NULL,
	MZ          double   NOT NULL,
	INTENSITY   float    NOT NULL,
	RELATIVE    smallint NOT NULL,
	UNIQUE(ID,MZ)
);

--
-- Table structure for table `SPECTRUM`
--
CREATE TABLE SPECTRUM (
	ID          char(8)      NOT NULL,
	NAME        varchar(255) NOT NULL,
	ION         tinyint      NOT NULL,
	PARENT_ID   char(8),
	COLLISION_ENERGY VARCHAR(10),
	PRECURSOR_MZ SMALLINT UNSIGNED,
	PRIMARY KEY(ID),
	INDEX(PARENT_ID)
);

--
-- Table structure for table `TREE`
--
CREATE TABLE TREE (
	NO          int unsigned      NOT NULL,
	PARENT      int unsigned      NOT NULL,
	POS         smallint unsigned NOT NULL,
	SON         smallint          NOT NULL,
	INFO        varchar(127)      NOT NULL,
	ID          char(8),
	INDEX(PARENT),
	INDEX(ID)
);

--
-- Table structure for table `RECORD`
--
CREATE TABLE RECORD (
	ID            CHAR(8) NOT NULL,
	DATE          DATE,
	FORMULA       VARCHAR(30),
	EXACT_MASS    DOUBLE,
	INSTRUMENT_NO TINYINT UNSIGNED,
	SMILES        VARCHAR(200),
	IUPAC         VARCHAR(200),
	PRECURSOR_MZ  SMALLINT UNSIGNED,
	PRIMARY KEY(ID),
	INDEX(FORMULA, EXACT_MASS, INSTRUMENT_NO)
);

--
-- Table structure for table `CH_NAME`
--
CREATE TABLE CH_NAME (
	ID              CHAR(8)      NOT NULL,
	NAME            VARCHAR(200) NOT NULL,
	INDEX(ID)
);

--
-- Table structure for table `CH_LINK`
--
CREATE TABLE CH_LINK (
	ID              CHAR(8) NOT NULL,
	CAS             TEXT,
	CHEBI           TEXT,
	CHEMPDB         TEXT,
	KEGG            TEXT,
	NIKKAJI         TEXT,
	PUBCHEM         TEXT,
	SITE_ID         VARCHAR(16),
	KNAPSACK        TEXT,
	KAPPAVIEW       TEXT,
	LIPIDBANK       TEXT,
	FLAVONOIDVIEW   TEXT,
	PRIMARY KEY(ID)
);

--
-- Table structure for table `INSTRUMENT`
--
CREATE TABLE INSTRUMENT (
	INSTRUMENT_NO      TINYINT UNSIGNED NOT NULL,
	INSTRUMENT_TYPE    VARCHAR(50)      NOT NULL,
	INSTRUMENT_NAME    VARCHAR(200)     NOT NULL,
	PRIMARY KEY(INSTRUMENT_NO)
);

--
-- Table structure for table `MOLFILE`
--
CREATE TABLE MOLFILE (
	FILE              VARCHAR(8)        NOT NULL,
	NAME              VARCHAR(200)      NOT NULL,
	UNIQUE(NAME,FILE)
);

GRANT ALL PRIVILEGES ON *.* TO bird@localhost IDENTIFIED BY 'bird2006' WITH GRANT OPTION;
