CREATE DATABASE `SAMPLE_DB` CHARACTER SET = 'latin1';
USE `SAMPLE_DB`;

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
-- Table structure for table `RECORD`
--
DROP TABLE IF EXISTS RECORD;
CREATE TABLE RECORD (
	ID            CHAR(8) 			NOT NULL,
	-- general information
	RECORD_TITLE  VARCHAR(255) 		NOT NULL,	-- add
	DATE          DATE 				NOT NULL,
	AUTHORS       VARCHAR(255)		NOT NULL,	-- add
	LICENSE       VARCHAR(255)		NOT NULL,	-- add
	COPYRIGHT     VARCHAR(255),     			-- add
	PUBLICATION   VARCHAR(255),     			-- add
	-- structure information
	FORMULA       VARCHAR(255)		NOT NULL,	-- mod size
	EXACT_MASS    DOUBLE			NOT NULL,
	INSTRUMENT_NO TINYINT UNSIGNED	NOT NULL,
	-- miscellaneous
	PK_SPLASH     VARCHAR(255)		NOT NULL,	-- add
	SMILES        TEXT,
	IUPAC         TEXT,
	MS_TYPE       VARCHAR(8)		NOT NULL,
	-- table setup
	PRIMARY KEY(ID),
	FOREIGN KEY (INSTRUMENT_NO)					-- add
		REFERENCES INSTRUMENT(INSTRUMENT_NO)	-- add
		ON DELETE NO ACTION						-- add
		ON UPDATE CASCADE,						-- add
	INDEX(RECORD_TITLE, AUTHORS, FORMULA, EXACT_MASS, INSTRUMENT_NO, MS_TYPE) -- mod
);

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
	PRIMARY KEY (ID,MZ),				-- modiefied form UNIQUE
	FOREIGN KEY (ID)					-- add
		REFERENCES RECORD(ID)			-- add
		ON DELETE CASCADE				-- add
		ON UPDATE CASCADE				-- add
);

--
-- View structure for view 'PK_NUM'
--
DROP VIEW IF EXISTS PK_NUM;					-- add
CREATE VIEW PK_NUM AS 						-- add
	SELECT ID, count(ID) AS PK$NUM_PEAK 	-- add
	FROM PEAK 								-- add
	GROUP BY ID;							-- add

--
-- Table structure for table `SPECTRUM`
--
DROP TABLE IF EXISTS SPECTRUM;
CREATE TABLE SPECTRUM (
	ID          CHAR(8)      NOT NULL,
	NAME        VARCHAR(255) NOT NULL,
	ION         TINYINT      NOT NULL,
	PRECURSOR_MZ SMALLINT UNSIGNED,
	PRIMARY KEY (ID),
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
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
	INDEX(ID), 
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);

--
-- Table structure for table `COMMENT`
--
DROP TABLE IF EXISTS COMMENT;               -- add
CREATE TABLE COMMENT (                      -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	PRIMARY KEY (ID, NAME),                 -- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);                                          -- add

--
-- Table structure for table `CH_COMPOUND_CLASS`
--
DROP TABLE IF EXISTS CH_COMPOUND_CLASS;     -- add
CREATE TABLE CH_COMPOUND_CLASS (            -- add
	ID              CHAR(8)       NOT NULL, -- add
	CLASS           VARCHAR(255),           -- add
	NAME            TEXT          NOT NULL, -- add
	PRIMARY KEY (ID, CLASS),                -- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);                                          -- add

--
-- Table structure for table `CH_NAME`
--
DROP TABLE IF EXISTS CH_NAME;
CREATE TABLE CH_NAME (
	ID              CHAR(8)      NOT NULL,
	NAME            VARCHAR(255) NOT NULL,  -- mod size
	PRIMARY KEY (ID, NAME),                 -- mod, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
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
	PRIMARY KEY (ID, LINK_NAME),			-- changed from INDEX (ID) -> PRIMARY KEY (ID, LINK_NAME) (recrod can have multiple links)
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);

--
-- Table structure for table `AC_MASS_SPECTROMETRY`
--
DROP TABLE IF EXISTS AC_MASS_SPECTROMETRY;      	-- add
CREATE TABLE AC_MASS_SPECTROMETRY (             	-- add
	ID              		CHAR(8)      NOT NULL,  -- add
	NAME            		VARCHAR(255) NOT NULL,  -- add
	-- COLLISION_ENERGY 		VARCHAR(255),		-- add
	-- COLLISION_GAS 			VARCHAR(255),		-- add
	-- DATE 					VARCHAR(255),		-- add
	-- DESOLVATION_GAS_FLOW 	VARCHAR(255),		-- add
	-- DESOLVATION_TEMPERATURE VARCHAR(255),		-- add
	-- IONIZATION_ENERGY 		VARCHAR(255),		-- add
	-- LASER 					VARCHAR(255),		-- add
	-- MATRIX 					VARCHAR(255),		-- add
	-- MASS_ACCURACY 			VARCHAR(255),		-- add
	-- REAGENT_GAS 				VARCHAR(255),		-- add
	-- SCANNING 				VARCHAR(255),		-- add
	PRIMARY KEY (ID, NAME),							-- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)								-- add
		REFERENCES RECORD(ID)						-- add
		ON DELETE CASCADE							-- add
		ON UPDATE CASCADE							-- add
);

--
-- Table structure for table `AC_CHROMATOGRAPHY`
--
DROP TABLE IF EXISTS AC_CHROMATOGRAPHY;         -- add
CREATE TABLE AC_CHROMATOGRAPHY (                -- add
	ID              	CHAR(8)      NOT NULL,  -- add
	NAME            	VARCHAR(255) NOT NULL,  -- add
	-- CAPILLARY_VOLTAGE	VARCHAR(255),		-- add
	-- COLUMN_NAME			VARCHAR(255),		-- add
	-- COLUMN_TEMPERATURE	VARCHAR(255),		-- add
	-- FLOW_GRADIENT		VARCHAR(255),		-- add
	-- FLOW_RATE			VARCHAR(255),		-- add
	-- RETENTION_TIME		VARCHAR(255),		-- add
	-- SOLVENT				VARCHAR(255),		-- add
	-- NAPS_RTI			VARCHAR(255), 			-- add
	PRIMARY KEY (ID, NAME),						-- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)							-- add
		REFERENCES RECORD(ID)					-- add
		ON DELETE CASCADE						-- add
		ON UPDATE CASCADE						-- add
);

--
-- Table structure for table `MS_FOCUSED_ION`
--
DROP TABLE IF EXISTS MS_FOCUSED_ION;        -- add
CREATE TABLE MS_FOCUSED_ION (               -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	-- BASE_PEAK		VARCHAR(255),		-- add
	-- DERIVATIVE_FORM	VARCHAR(255),		-- add
	-- DERIVATIVE_MASS	VARCHAR(255),		-- add
	-- DERIVATIVE_TYPE	VARCHAR(255),		-- add
	-- ION_TYPE		VARCHAR(255),			-- add
	-- PRECURSOR_MZ	VARCHAR(255),			-- add
	-- PRECURSOR_TYPE	VARCHAR(255),		-- add
	PRIMARY KEY (ID, NAME),					-- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);

--
-- Table structure for table `MS_DATA_PROCESSING`
--
DROP TABLE IF EXISTS MS_DATA_PROCESSING;    -- add
CREATE TABLE MS_DATA_PROCESSING (           -- add
	ID              CHAR(8)      NOT NULL,  -- add
	NAME            VARCHAR(255) NOT NULL,  -- add
	-- FIND_PEAK		VARCHAR(255),		-- add
	-- WHOLE			VARCHAR(255),		-- add
	PRIMARY KEY (ID, NAME),					-- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
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

--
-- Table structure for table `BIOLOGICAL_SAMPLE`
--
DROP TABLE IF EXISTS BIOLOGICAL_SAMPLE;		-- add
CREATE TABLE BIOLOGICAL_SAMPLE (			-- add
	ID              CHAR(8)      NOT NULL,	-- add
	SCIENTIFIC_NAME VARCHAR(255),			-- add
	LINEAGE			VARCHAR(255),			-- add
	SAMPLE 			VARCHAR(255),			-- add
	INDEX(ID),								-- add
	PRIMARY KEY (ID),						-- add						
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);											-- add

--
-- Table structure for table `SP_LINK`
--
DROP TABLE IF EXISTS SP_LINK;				-- add
CREATE TABLE SP_LINK (						-- add
	ID              CHAR(8)      NOT NULL,	-- add
	LINK_NAME       VARCHAR(100) NOT NULL,	-- add
	LINK_ID         VARCHAR(100) NOT NULL,	-- add
	UNIQUE(ID,LINK_NAME,LINK_ID),			-- add
	PRIMARY KEY(ID),						-- add, changed from INDEX -> PRIMARY KEY
	FOREIGN KEY (ID)						-- add
		REFERENCES RECORD(ID)				-- add
		ON DELETE CASCADE					-- add
		ON UPDATE CASCADE					-- add
);											-- add

GRANT ALL PRIVILEGES ON *.* TO bird IDENTIFIED BY 'bird2006' WITH GRANT OPTION;
