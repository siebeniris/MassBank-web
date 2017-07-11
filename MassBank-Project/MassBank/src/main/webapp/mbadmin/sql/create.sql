CREATE DATABASE `SAMPLE_DB` CHARACTER SET = 'latin1';
USE `SAMPLE_DB`;

--
-- Table structure for table `PEAK`
--
DROP TABLE IF EXISTS PEAK;
CREATE TABLE PEAK (
	ID          CHAR(8)  NOT NULL,
	MZ          DOUBLE   NOT NULL,
	INTENSITY   FLOAT    NOT NULL,
	RELATIVE    SMALLINT NOT NULL,
	-- ANNOTATION: add ??? m/z	tentative_formula	formula_count	mass	error(ppm) / 57.0701	C4H9+	1	57.0699	4.61
	-- TENTATIVE_FORMULA VARCHAR(255), -- add
	-- FORMULA_COUNT     SMALLINT,     -- add
	-- THEORETICAL_MASS  FLOAT,        -- add
	-- ERROR_PPM         FLOAT         -- add
	UNIQUE(ID,MZ)
);

--
-- Table structure for table `SPECTRUM`
--
DROP TABLE IF EXISTS SPECTRUM;
CREATE TABLE SPECTRUM (
	ID          CHAR(8)      NOT NULL,
	NAME        VARCHAR(255) NOT NULL,
	ION         TINYINT      NOT NULL,
	PRECURSOR_MZ SMALLINT UNSIGNED,
	PRIMARY KEY(ID)
);

--
-- Table structure for table `TREE`
--
DROP TABLE IF EXISTS TREE;
CREATE TABLE TREE (
	NO          INT UNSIGNED      NOT NULL,
	PARENT      INT UNSIGNED      NOT NULL,
	POS         SMALLINT UNSIGNED NOT NULL,
	SON         SMALLINT          NOT NULL,
	INFO        VARCHAR(255)      NOT NULL, -- mod size
	ID          CHAR(8),
	INDEX(PARENT),
	INDEX(ID)
);

--
-- Table structure for table `RECORD`
--
DROP TABLE IF EXISTS RECORD;
CREATE TABLE RECORD (
	ID            CHAR(8) NOT NULL,
	-- general information
	RECORD_TITLE  VARCHAR(255),     -- add
	DATE          DATE,
	AUTHORS       VARCHAR(255),     -- add
	LICENSE       VARCHAR(255),     -- add
	COPYRIGHT     VARCHAR(255),     -- add
	PUBLICATION   VARCHAR(255),     -- add
	-- structure information
	FORMULA       VARCHAR(255),     -- mod size
	EXACT_MASS    DOUBLE,
	INSTRUMENT_NO TINYINT UNSIGNED,
	-- miscellaneous
	PK_SPLASH     VARCHAR(255),     -- add
	SMILES        TEXT,
	IUPAC         TEXT,
	MS_TYPE       VARCHAR(8),
	-- table setup
	PRIMARY KEY(ID),
	INDEX(RECORD_TITLE, AUTHORS, FORMULA, EXACT_MASS, INSTRUMENT_NO, MS_TYPE) -- mod
);

--
-- Table structure for table `COMMENT`
--
DROP TABLE IF EXISTS COMMENT;                   -- add
CREATE TABLE COMMENT (                          -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	INDEX(ID, NAME)                         -- add
);                                              -- add

--
-- Table structure for table `CH_COMPOUND_CLASS`
--
DROP TABLE IF EXISTS CH_COMPOUND_CLASS;         -- add
CREATE TABLE CH_COMPOUND_CLASS (                -- add
	ID              CHAR(8)       NOT NULL, -- add
	CLASS           VARCHAR(255),           -- add
	NAME            TEXT          NOT NULL, -- add
	INDEX(ID, CLASS)                        -- add
);                                              -- add

--
-- Table structure for table `CH_NAME`
--
DROP TABLE IF EXISTS CH_NAME;
CREATE TABLE CH_NAME (
	ID              CHAR(8)      NOT NULL,
	NAME            VARCHAR(255) NOT NULL,  -- mod size
	INDEX(ID, NAME)                         -- mod
);

--
-- Table structure for table `CH_LINK`
--
DROP TABLE IF EXISTS CH_LINK;
CREATE TABLE CH_LINK (
	ID              CHAR(8)      NOT NULL,
	LINK_NAME       VARCHAR(100) NOT NULL,
	LINK_ID         VARCHAR(100) NOT NULL,
	UNIQUE(ID,LINK_NAME,LINK_ID),
	INDEX(ID)
);

--
-- Table structure for table `INSTRUMENT`
--
DROP TABLE IF EXISTS INSTRUMENT;
CREATE TABLE INSTRUMENT (
	INSTRUMENT_NO      TINYINT UNSIGNED NOT NULL,
	INSTRUMENT_TYPE    VARCHAR(50)      NOT NULL,
	INSTRUMENT_NAME    VARCHAR(200)     NOT NULL,
	PRIMARY KEY(INSTRUMENT_NO)
);

--
-- Table structure for table `AC_MASS_SPECTROMETRY`
--
DROP TABLE IF EXISTS AC_MASS_SPECTROMETRY;      -- add
CREATE TABLE AC_MASS_SPECTROMETRY (             -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	INDEX(ID, NAME)                         -- add
);

--
-- Table structure for table `AC_CHROMATOGRAPHY`
--
DROP TABLE IF EXISTS AC_CHROMATOGRAPHY;         -- add
CREATE TABLE AC_CHROMATOGRAPHY (                -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	INDEX(ID, NAME)                         -- add
);

--
-- Table structure for table `MS_FOCUSED_ION`
--
DROP TABLE IF EXISTS MS_FOCUSED_ION;            -- add
CREATE TABLE MS_FOCUSED_ION (                   -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	INDEX(ID, NAME)                         -- add
);

--
-- Table structure for table `MS_DATA_PROCESSING`
--
DROP TABLE IF EXISTS MS_DATA_PROCESSING;        -- add
CREATE TABLE MS_DATA_PROCESSING (               -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	INDEX(ID, NAME)                         -- add
);

-- 
-- Table structure for table `MOLFILE`
-- 
-- DROP TABLE IF EXISTS MOLFILE;
-- CREATE TABLE MOLFILE (
-- 	FILE              VARCHAR(8)        NOT NULL,
-- 	NAME              VARCHAR(100)      NOT NULL,
-- 	UNIQUE(NAME,FILE)
-- );


GRANT ALL PRIVILEGES ON *.* TO bird IDENTIFIED BY 'bird2006' WITH GRANT OPTION;
