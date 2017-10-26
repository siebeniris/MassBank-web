package massbank;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class DatabaseManager {
	
	private final static String driver = "org.mariadb.jdbc.Driver";
//	private final static String user = MassBankEnv.get(MassBankEnv.KEY_DB_USER);
//	private final static String password = MassBankEnv.get(MassBankEnv.KEY_DB_PASSWORD);
	private final static String user = "bird";
	private final static String password = "bird2006";
//	private final static String databaseName = "MassBankNew";
	private String databaseName;
	private final static String dbHostName = getDbHostName();
//	private final static String connectUrl = "jdbc:mysql://" + dbHostName;
	private final String connectUrl;
//	private final static String connectUrl = "jdbc:mysql://" + dbHostName + "/" + databaseName;
	private Connection con;

	private final static String sqlAC_CHROMATOGRAPHY = "SELECT * FROM AC_CHROMATOGRAPHY WHERE RECORD = ?";
	private final static String sqlAC_MASS_SPECTROMETRY = "SELECT * FROM AC_MASS_SPECTROMETRY WHERE RECORD = ?";
	private final static String sqlCH_LINK = "SELECT * FROM CH_LINK WHERE COMPOUND = ?";
	private final static String sqlCOMMENT = "SELECT * FROM COMMENT WHERE RECORD = ?";
	private final static String sqlCOMPOUND = "SELECT * FROM COMPOUND WHERE ID = ?";
	private final static String sqlCOMPOUND_CLASS = "SELECT * FROM COMPOUND_CLASS WHERE ID = ?";
	private final static String sqlCOMPOUND_COMPOUND_CLASS = "SELECT * FROM COMPOUND_COMPOUND_CLASS WHERE COMPOUND = ?";
	private final static String sqlCOMPOUND_NAME = "SELECT * FROM COMPOUND_NAME WHERE COMPOUND = ?";
	private final static String sqlINSTRUMENT = "SELECT * FROM INSTRUMENT WHERE ID = ?";
	private final static String sqlMS_DATA_PROCESSING = "SELECT * FROM MS_DATA_PROCESSING WHERE RECORD = ?";
	private final static String sqlMS_FOCUSED_ION = "SELECT * FROM MS_FOCUSED_ION WHERE RECORD = ?";
	private final static String sqlNAME = "SELECT * FROM NAME WHERE ID = ?";
	private final static String sqlPEAK = "SELECT * FROM PEAK WHERE RECORD = ?";
	private final static String sqlPK_NUM_PEAK = "SELECT * FROM PK_NUM_PEAK WHERE RECORD = ?";
	private final static String sqlRECORD = "SELECT * FROM RECORD WHERE ACCESSION = ?";
	private final static String sqlSAMPLE = "SELECT * FROM SAMPLE WHERE ID = ?";
	private final static String sqlSP_LINK = "SELECT * FROM SP_LINK WHERE SAMPLE = ?";
	private final static String sqlSP_SAMPLE = "SELECT * FROM SP_SAMPLE WHERE SAMPLE = ?";
	private final static String sqlANNOTATION_HEADER = "SELECT * FROM ANNOTATION_HEADER WHERE RECORD = ?";
	
	private final PreparedStatement statementAC_CHROMATOGRAPHY;
	private final PreparedStatement statementAC_MASS_SPECTROMETRY;
	private final PreparedStatement statementCH_LINK;
	private final PreparedStatement statementCOMMENT;
	private final PreparedStatement statementCOMPOUND;
	private final PreparedStatement statementCOMPOUND_CLASS;
	private final PreparedStatement statementCOMPOUND_COMPOUND_CLASS;
	private final PreparedStatement statementCOMPOUND_NAME;
	private final PreparedStatement statementINSTRUMENT;
	private final PreparedStatement statementMS_DATA_PROCESSING;
	private final PreparedStatement statementMS_FOCUSED_ION;
	private final PreparedStatement statementNAME;
	private final PreparedStatement statementPEAK;
	private final PreparedStatement statementPK_NUM_PEAK;
	private final PreparedStatement statementRECORD;
	private final PreparedStatement statementSAMPLE;
	private final PreparedStatement statementSP_LINK;
	private final PreparedStatement statementSP_SAMPLE;
	private final PreparedStatement statementANNOTATION_HEADER;

	private final static String insertCompound = "INSERT INTO COMPOUND VALUES(?,?,?,?,?,?,?,?)";
	private final static String insertCompound_Class = "INSERT INTO COMPOUND_CLASS VALUES(?,?,?,?)";
	private final static String insertCompound_Compound_Class = "INSERT INTO COMPOUND_COMPOUND_CLASS VALUES(?,?)";
	private final static String insertName = "INSERT INTO NAME VALUES(?,?)";
	private final static String insertCompound_Name = "INSERT IGNORE INTO COMPOUND_NAME VALUES(?,?)";
	private final static String insertCH_LINK = "INSERT INTO CH_LINK VALUES(?,?,?)";
	private final static String insertSAMPLE = "INSERT INTO SAMPLE VALUES(?,?,?)";
	private final static String insertSP_LINK = "INSERT INTO SP_LINK VALUES(?,?)";
	private final static String insertSP_SAMPLE = "INSERT INTO SP_SAMPLE VALUES(?,?)";
	private final static String insertINSTRUMENT = "INSERT INTO INSTRUMENT VALUES(?,?,?)";
	private final static String insertRECORD = "INSERT INTO RECORD VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final static String insertCOMMENT = "INSERT INTO COMMENT VALUES(?,?)";
	private final static String insertAC_MASS_SPECTROMETRY = "INSERT INTO AC_MASS_SPECTROMETRY VALUES(?,?,?)";
	private final static String insertAC_CHROMATOGRAPHY = "INSERT INTO AC_CHROMATOGRAPHY VALUES(?,?,?)";
	private final static String insertMS_FOCUSED_ION = "INSERT INTO MS_FOCUSED_ION VALUES(?,?,?)";
	private final static String insertMS_DATA_PROCESSING = "INSERT INTO MS_DATA_PROCESSING VALUES(?,?,?)";
//	private final static String insertPEAK = "INSERT INTO PEAK VALUES(?,?,?,?,?,?,?,?)";
	private final static String insertPEAK = "INSERT INTO PEAK VALUES(?,?,?,?,?)";
//	private final static String updatePEAK = "UPDATE PEAK SET PK_ANNOTATION_TENTATIVE_FORMULA = ?, PK_ANNOTATION_FORMULA_COUNT = ?, PK_ANNOTATION_THEORETICAL_MASS = ?, PK_ANNTOATION_ERROR_PPM = ? WHERE RECORD = ? AND PK_PEAK_MZ = ?";
	private final static String updatePEAKs = "UPDATE PEAK SET PK_ANNOTATION = ? WHERE RECORD = ? AND PK_PEAK_MZ = ?";
	private final static String insertANNOTATION_HEADER = "INSERT INTO ANNOTATION_HEADER VALUES(?,?)";
	
	private final PreparedStatement statementInsertCompound;
	private final PreparedStatement statementInsertCompound_Class;
	private final PreparedStatement statementInsertCompound_Compound_Class;
	private final PreparedStatement statementInsertName;
	private final PreparedStatement statementInsertCompound_Name;
	private final PreparedStatement statementInsertCH_LINK;
	private final PreparedStatement statementInsertSAMPLE;
	private final PreparedStatement statementInsertSP_LINK;
	private final PreparedStatement statementInsertSP_SAMPLE;
	private final PreparedStatement statementInsertINSTRUMENT;
	private final PreparedStatement statementInsertRECORD;
	private final PreparedStatement statementInsertCOMMENT;
	private final PreparedStatement statementInsertAC_MASS_SPECTROMETRY;
	private final PreparedStatement statementInsertAC_CHROMATOGRAPHY;
	private final PreparedStatement statementInsertMS_FOCUSED_ION;
	private final PreparedStatement statementInsertMS_DATA_PROCESSING;
	private final PreparedStatement statementInsertPEAK;
//	private final PreparedStatement statementUpdatePEAK;
	private final PreparedStatement statementUpdatePEAKs;
	private final PreparedStatement statementInsertANNOTATION_HEADER;
		
	public static DatabaseManager create(String dbName) {
		try {
			return new DatabaseManager(dbName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static DatabaseManager create() {
		return create("MassBankNew");
	}
	
	public DatabaseManager(String dbName) throws SQLException {
		this.databaseName = dbName;
		this.connectUrl = "jdbc:mysql://" + dbHostName + "/" + databaseName + "?rewriteBatchedStatements=true";
			this.openConnection();
			statementAC_CHROMATOGRAPHY = this.con.prepareStatement(sqlAC_CHROMATOGRAPHY);
			statementAC_MASS_SPECTROMETRY = this.con.prepareStatement(sqlAC_MASS_SPECTROMETRY);
			statementCH_LINK = this.con.prepareStatement(sqlCH_LINK);
			statementCOMMENT = this.con.prepareStatement(sqlCOMMENT);
			statementCOMPOUND = this.con.prepareStatement(sqlCOMPOUND);
			statementCOMPOUND_CLASS = this.con.prepareStatement(sqlCOMPOUND_CLASS);
			statementCOMPOUND_COMPOUND_CLASS = this.con.prepareStatement(sqlCOMPOUND_COMPOUND_CLASS);
			statementCOMPOUND_NAME = this.con.prepareStatement(sqlCOMPOUND_NAME);
			statementINSTRUMENT = this.con.prepareStatement(sqlINSTRUMENT);
			statementMS_DATA_PROCESSING = this.con.prepareStatement(sqlMS_DATA_PROCESSING);
			statementMS_FOCUSED_ION = this.con.prepareStatement(sqlMS_FOCUSED_ION);
			statementNAME = this.con.prepareStatement(sqlNAME);
			statementPEAK = this.con.prepareStatement(sqlPEAK);
			statementPK_NUM_PEAK = this.con.prepareStatement(sqlPK_NUM_PEAK);
			statementRECORD = this.con.prepareStatement(sqlRECORD);
			statementSAMPLE = this.con.prepareStatement(sqlSAMPLE);
			statementSP_LINK = this.con.prepareStatement(sqlSP_LINK);
			statementSP_SAMPLE = this.con.prepareStatement(sqlSP_SAMPLE);
			statementANNOTATION_HEADER = this.con.prepareStatement(sqlANNOTATION_HEADER);
			
			statementInsertCompound = this.con.prepareStatement(insertCompound);
			statementInsertCompound_Class = this.con.prepareStatement(insertCompound_Class);
			statementInsertCompound_Compound_Class = this.con.prepareStatement(insertCompound_Compound_Class);
			statementInsertName = this.con.prepareStatement(insertName);
			statementInsertCompound_Name = this.con.prepareStatement(insertCompound_Name);
			statementInsertCH_LINK = this.con.prepareStatement(insertCH_LINK);
			statementInsertSAMPLE = this.con.prepareStatement(insertSAMPLE);
			statementInsertSP_LINK = this.con.prepareStatement(insertSP_LINK);
			statementInsertSP_SAMPLE = this.con.prepareStatement(insertSP_SAMPLE);
			statementInsertINSTRUMENT = this.con.prepareStatement(insertINSTRUMENT);
			statementInsertRECORD = this.con.prepareStatement(insertRECORD);
			statementInsertCOMMENT = this.con.prepareStatement(insertCOMMENT);
			statementInsertAC_MASS_SPECTROMETRY = this.con.prepareStatement(insertAC_MASS_SPECTROMETRY);
			statementInsertAC_CHROMATOGRAPHY = this.con.prepareStatement(insertAC_CHROMATOGRAPHY);
			statementInsertMS_FOCUSED_ION = this.con.prepareStatement(insertMS_FOCUSED_ION);
			statementInsertMS_DATA_PROCESSING = this.con.prepareStatement(insertMS_DATA_PROCESSING);
			statementInsertPEAK = this.con.prepareStatement(insertPEAK);
//			statementUpdatePEAK = this.con.prepareStatement(updatePEAK);
			statementUpdatePEAKs = this.con.prepareStatement(updatePEAKs);
			statementInsertANNOTATION_HEADER = this.con.prepareStatement(insertANNOTATION_HEADER);

	}
	
	private void openConnection() {
		Connection con	= null;
		try {
			Class.forName(DatabaseManager.driver);
			con = DriverManager.getConnection(this.connectUrl, DatabaseManager.user, DatabaseManager.password);
			con.setAutoCommit(false);
			con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
			this.con = con;
	}

	private void closeConnection() {
		try {
			if ( this.con != null ) this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	

	public AccessionData getAccessionData(String accessionId) {
		AccessionData acc = new AccessionData();
		try {
			this.statementRECORD.setString(1, accessionId);
			ResultSet set = this.statementRECORD.executeQuery();
			int fkCH = -1;
			int fkSP = -1;
			int fkAC_INSTRUMENT = -1;
			while (set.next()) {
				acc.add("ACCESSION", null, set.getString("ACCESSION"));
				acc.add("RECORD_TITLE", null, set.getString("RECORD_TITLE"));
				acc.add("DATE", null, set.getString("DATE"));
				acc.add("AUTHORS", null, set.getString("AUTHORS"));
				acc.add("LICENSE", null, set.getString("LICENSE"));
				acc.add("COPYRIGHT", null, set.getString("COPYRIGHT"));
				acc.add("PUBLICATION", null, set.getString("PUBLICATION"));
				fkCH = set.getInt("CH");
				fkSP = set.getInt("SP");
				fkAC_INSTRUMENT = set.getInt("AC_INSTRUMENT");
				acc.add("AC$MASS_SPECTROMETRY", "MS_TYPE", set.getString("AC_MASS_SPECTROMETRY_MS_TYPE"));
				acc.add("AC$MASS_SPECTROMETRY", "ION_MODE", set.getString("AC_MASS_SPECTROMETRY_ION_MODE"));
				acc.add("PK$SPLASH", null, set.getString("PK_SPLASH"));
				this.statementAC_CHROMATOGRAPHY.setString(1, set.getString("ACCESSION"));
				this.statementAC_MASS_SPECTROMETRY.setString(1, set.getString("ACCESSION"));
				this.statementMS_DATA_PROCESSING.setString(1, set.getString("ACCESSION"));
				this.statementMS_FOCUSED_ION.setString(1, set.getString("ACCESSION"));
				this.statementCOMMENT.setString(1, set.getString("ACCESSION"));
				this.statementPEAK.setString(1, set.getString("ACCESSION"));
				this.statementPK_NUM_PEAK.setString(1, set.getString("ACCESSION"));
				ResultSet tmp = this.statementAC_CHROMATOGRAPHY.executeQuery();
				while (tmp.next()) {
					acc.add("AC$CHROMATOGRAPHY", tmp.getString("SUBTAG"), tmp.getString("VALUE"));
				}
				tmp = this.statementAC_MASS_SPECTROMETRY.executeQuery();
				while (tmp.next()) {
					acc.add("AC$MASS_SPECTROMETRY", tmp.getString("SUBTAG"), tmp.getString("VALUE"));
				}
				tmp = this.statementMS_DATA_PROCESSING.executeQuery();
				while (tmp.next()) {
					acc.add("MS$DATA_PROCESSING", tmp.getString("SUBTAG"), tmp.getString("VALUE"));
				}
				tmp = this.statementMS_FOCUSED_ION.executeQuery();
				while (tmp.next()) {
					acc.add("MS$FOCUSED_ION", tmp.getString("SUBTAG"), tmp.getString("VALUE"));
				}
				tmp = this.statementCOMMENT.executeQuery();
				while (tmp.next()) {
					acc.add("COMMENT", null, tmp.getString("COMMENT"));
				}
				tmp = this.statementPEAK.executeQuery();
//				acc.add("PK$PEAK", null, "m/z int. rel.int.");
				while (tmp.next()) {
					acc.add("PK$PEAK", null, tmp.getDouble("PK_PEAK_MZ") + " " + tmp.getFloat("PK_PEAK_INTENSITY") + " " + tmp.getShort("PK_PEAK_RELATIVE"));
					acc.add("PK$ANNOTATION", null, tmp.getString("PK_ANNOTATION"));
				}
				this.statementANNOTATION_HEADER.setString(1, accessionId);
				tmp = this.statementANNOTATION_HEADER.executeQuery();
				while (tmp.next()) {
					acc.annotationHeader = tmp.getString("HEADER");
				}
				tmp = this.statementPK_NUM_PEAK.executeQuery();
				while (tmp.next()) {
					acc.add("PK$NUM_PEAK", null, Integer.valueOf(tmp.getInt("PK_NUM_PEAK")).toString());
				}
			}
			if (fkCH == -1)
				return null;
			if (fkCH != -1)
				this.statementCOMPOUND.setInt(1, fkCH);
			set = this.statementCOMPOUND.executeQuery();
			while (set.next()) {
				acc.add("CH$FORMULA", null, set.getString("CH_FORMULA"));
				acc.add("CH$EXACT_MASS", null, set.getString("CH_EXACT_MASS"));
				acc.add("CH$SMILES", null, set.getString("CH_SMILES"));
				acc.add("CH$IUPAC", null, set.getString("CH_IUPAC"));
				acc.add("CH$CDK_DEPICT_SMILES", null, set.getString("CH_CDK_DEPICT_SMILES"));
				acc.add("CH$CDK_DEPICT_GENERIC_SMILES", null, set.getString("CH_CDK_DEPICT_GENERIC_SMILES"));
				acc.add("CH$CDK_DEPICT_STRUCTURE_SMILES", null, set.getString("CH_CDK_DEPICT_STRUCTURE_SMILES"));
			}
			this.statementCH_LINK.setInt(1, fkCH);
			set = this.statementCH_LINK.executeQuery();
			while (set.next()) {
				acc.add("CH$LINK", set.getString("DATABASE_NAME"), set.getString("DATABASE_ID"));
			}
			this.statementCOMPOUND_COMPOUND_CLASS.setInt(1, fkCH);
			set = this.statementCOMPOUND_COMPOUND_CLASS.executeQuery();
			while (set.next()) {
				this.statementCOMPOUND_CLASS.setInt(1, set.getInt("CLASS"));
				ResultSet tmp = this.statementCOMPOUND_CLASS.executeQuery();
				while (tmp.next()) {
					acc.add("CH$COMPOUND_CLASS", null, tmp.getString("CH_COMPOUND_CLASS"));
				}
			}
			this.statementCOMPOUND_NAME.setInt(1, fkCH);
			set = this.statementCOMPOUND_NAME.executeQuery();
			while (set.next()) {
				this.statementNAME.setInt(1, set.getInt("NAME"));
				ResultSet tmp = this.statementNAME.executeQuery();
				while (tmp.next()) {
					acc.add("CH$NAME", null, tmp.getString("CH_NAME"));
				}
			}
			this.statementSAMPLE.setInt(1,fkSP);
			set = this.statementSAMPLE.executeQuery();
			while (set.next()) {
				acc.add("SP$SCIENTIFIC_NAME",null,set.getString("SP_SCIENTIFIC_NAME"));
				acc.add("SP_LINEAGE", null, set.getString("SP_LINEAGE"));
				this.statementSP_LINK.setInt(1,set.getInt("ID"));
				this.statementSP_SAMPLE.setInt(1, set.getInt("ID"));
				ResultSet tmp = this.statementSP_LINK.executeQuery();
				while (tmp.next()) {
					acc.add("SP$LINK", null, tmp.getString("SP_LINK"));
				}
				tmp = this.statementSP_SAMPLE.executeQuery();
				while (tmp.next()) {
					acc.add("SP$SAMPLE", null, tmp.getString("SP_SAMPLE"));
				}
			}
			if (fkAC_INSTRUMENT != -1)
				this.statementINSTRUMENT.setInt(1, fkAC_INSTRUMENT);
			set = this.statementINSTRUMENT.executeQuery();
			while (set.next()) {
				acc.add("AC$INSTRUMENT", null, set.getString("AC_INSTRUMENT"));
				acc.add("AC$INSTRUMENT_TYPE", null, set.getString("AC_INSTRUMENT_TYPE"));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error: " + accessionId);
			return null;
		}
//		this.openConnection();
		
		return acc;
	}
	
	private static String getDbHostName() {
		String dbHostName = MassBankEnv.get(MassBankEnv.KEY_DB_HOST_NAME);
		if ( !MassBankEnv.get(MassBankEnv.KEY_DB_MASTER_NAME).equals("") ) {
			dbHostName = MassBankEnv.get(MassBankEnv.KEY_DB_MASTER_NAME);
		}
		return dbHostName;
	}
	
	private HashMap<String,String> getDatabaseOfAccessions() {
		GetConfig config = new GetConfig(MassBankEnv.get(MassBankEnv.KEY_BASE_URL));
		String[] dbNames = config.getDbName();
		HashMap<String,String> dbMapping = new HashMap<String,String>();
		Connection con	= null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(connectUrl, user, password);
			con.setAutoCommit(false);
			con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
			for (String db : dbNames) {
				String sql = "SELECT ACCESSION FROM " + db + ".RECORD";
				PreparedStatement stmnt = con.prepareStatement(sql);
				ResultSet resultSet	= stmnt.executeQuery();
				while (resultSet.next()) {
					dbMapping.put(resultSet.getString("ACCESSION"), db);
				}
				resultSet.close();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( con != null )
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return dbMapping;
	}
	
	public void batchPersist(ArrayList<AccessionFile> accs) {
		for (AccessionFile acc : accs) {
			if (acc != null) {
				if (acc.isValid()) {							
					try {
						persistAccessionFile(acc, true);
					} catch (Exception e) {
//						e.printStackTrace();
					}
				} else {
				}
			}
		}
		this.closeConnection();
	}
	
	public void persistAccessionFile(AccessionFile acc) {
		persistAccessionFile(acc, false);
	}
	
	public void persistAccessionFile(AccessionFile acc, boolean bulk) {
//		this.openConnection();
//		String insertCompound = "INSERT INTO COMPOUND VALUES(?,?,?,?,?,?,?,?)";
//		PreparedStatement stmnt = con.prepareStatement(insertCompound);
		try {
		//System.out.println(System.nanoTime());
		statementInsertCompound.setNull(1, java.sql.Types.INTEGER);
		statementInsertCompound.setString(2, acc.get("CH$FORMULA").get(0)[2]);
		statementInsertCompound.setString(3, acc.get("CH$EXACT_MASS").get(0)[2]);
		statementInsertCompound.setString(4, acc.get("CH$SMILES").get(0)[2]);
		statementInsertCompound.setString(5, acc.get("CH$IUPAC").get(0)[2]);
		if (acc.get("CH$CDK_DEPICT_SMILES").size() != 0) {
			statementInsertCompound.setString(6, acc.get("CH$CDK_DEPICT_SMILES").get(0)[2]);
		} else {
			statementInsertCompound.setNull(6, java.sql.Types.VARCHAR);
		}
		if (acc.get("CH$CDK_DEPICT_GENERIC_SMILES").size() != 0) {
			statementInsertCompound.setString(7, acc.get("CH$CDK_DEPICT_GENERIC_SMILES").get(0)[2]);
		} else {
			statementInsertCompound.setNull(7, java.sql.Types.VARCHAR);
		}
		if (acc.get("CH$CDK_DEPICT_STRUCTURE_SMILES").size() != 0) {
			statementInsertCompound.setString(8, acc.get("CH$CDK_DEPICT_STRUCTURE_SMILES").get(0)[2]);
		} else {
			statementInsertCompound.setNull(8, java.sql.Types.VARCHAR);
		}
		statementInsertCompound.executeUpdate();
		ResultSet set = statementInsertCompound.getGeneratedKeys();
		set.next();
		int compoundId = set.getInt("ID");
		
		
		//System.out.println(System.nanoTime());
		int compoundClassId;
//		String insertCompoundClass = "INSERT INTO COMPOUND_CLASS VALUES(?,?,?,?)";
//		stmnt = con.prepareStatement(insertCompoundClass);
		for (String[] el : acc.get("CH$COMPOUND_CLASS")) {
			statementInsertCompound_Class.setNull(1, java.sql.Types.INTEGER);		
			statementInsertCompound_Class.setString(2, null);
			statementInsertCompound_Class.setString(3, null);
			statementInsertCompound_Class.setString(4, el[2]);
			statementInsertCompound_Class.executeUpdate();
			set = statementInsertCompound_Class.getGeneratedKeys();
			set.next();
			compoundClassId = set.getInt("ID");
			
//			String insertCompoundCompoundClass = "INSERT INTO COMPOUND_COMPOUND_CLASS VALUES(?,?)";
//			stmnt = con.prepareStatement(insertCompoundCompoundClass);
			statementInsertCompound_Compound_Class.setInt(1, compoundId);
			statementInsertCompound_Compound_Class.setInt(2, compoundClassId);
			statementInsertCompound_Compound_Class.executeUpdate();
		}
		
		//System.out.println(System.nanoTime());
		int nameId;
//		String insertName = "INSERT INTO NAME VALUES(?,?)";
//		stmnt = con.prepareStatement(insertName);
		for (String[] el : acc.get("CH$NAME")) {
			statementInsertName.setNull(1, java.sql.Types.INTEGER);
			statementInsertName.setString(2, el[2]);
			try {
				statementInsertName.executeUpdate();
				set = statementInsertName.getGeneratedKeys();
				set.next();
				nameId = set.getInt("ID");

//				String insertCompoundName = "INSERT INTO COMPOUND_NAME VALUES(?,?)";
//				stmnt = con.prepareStatement(insertCompoundName);
				statementInsertCompound_Name.setInt(1, compoundId);
				statementInsertCompound_Name.setInt(2, nameId);
				statementInsertCompound_Name.executeUpdate();
			} catch (SQLException e) {
				if (e.getErrorCode() == 1062) {
					PreparedStatement retrieveIdForName = con.prepareStatement("SELECT ID FROM NAME WHERE CH_NAME = ?");
					retrieveIdForName.setString(1, el[2]);
					set = retrieveIdForName.executeQuery();
					set.next();
					nameId = set.getInt("ID");
					statementInsertCompound_Name.setInt(1, compoundId);
					statementInsertCompound_Name.setInt(2, nameId);
					statementInsertCompound_Name.executeUpdate();
				} else {
					this.closeConnection();
					throw e;
//					e.printStackTrace();
//					nameId = -1;
				}
			}
		}
		
		//System.out.println(System.nanoTime());
//		String insertChLink = "INSERT INTO CH_LINK VALUES(?,?,?)";
//		stmnt = con.prepareStatement(insertChLink);
		for (String[] el : acc.get("CH$LINK")) {
			statementInsertCH_LINK.setInt(1,compoundId);
			statementInsertCH_LINK.setString(2, el[1]);
			statementInsertCH_LINK.setString(3, el[2]);
//			statementInsertCH_LINK.executeUpdate();
			statementInsertCH_LINK.addBatch();
		}
		if (!bulk) {
			statementInsertCH_LINK.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		int sampleId = -1;
		statementInsertSAMPLE.setNull(1, java.sql.Types.INTEGER);
		if (acc.get("SP$SCIENTIFIC_NAME").size() != 0) {
			statementInsertSAMPLE.setString(2, acc.get("SP$SCIENTIFIC_NAME").get(0)[2]);
		} else {
			statementInsertSAMPLE.setNull(2, java.sql.Types.VARCHAR);
		}
		if (acc.get("SP$LINEAGE").size() != 0) {
			statementInsertSAMPLE.setString(3, acc.get("SP$LINEAGE").get(0)[2]);
		} else {
			statementInsertSAMPLE.setNull(3, java.sql.Types.VARCHAR);
		}
		if (acc.get("SP$SCIENTIFIC_NAME").size() != 0 && acc.get("SP$LINEAGE").size() != 0) {
			statementInsertSAMPLE.executeUpdate();
			set = statementInsertSAMPLE.getGeneratedKeys();
			set.next();
			sampleId = set.getInt("ID");
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("SP$LINK")) {
			statementInsertSP_LINK.setInt(1, sampleId);
			statementInsertSP_LINK.setString(2, el[2]);
//			statementInsertSP_LINK.executeUpdate();
			statementInsertSP_LINK.addBatch();
		}
		if (!bulk) {
			statementInsertSP_LINK.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("SP$SAMPLE")) {
			statementInsertSP_SAMPLE.setInt(1, sampleId);
			statementInsertSP_SAMPLE.setString(2, el[2]);
//			statementInsertSP_SAMPLE.executeUpdate();
			statementInsertSP_SAMPLE.addBatch();
		}
		if (!bulk) {
			statementInsertSP_SAMPLE.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		statementInsertINSTRUMENT.setNull(1, java.sql.Types.INTEGER);
		statementInsertINSTRUMENT.setString(2, acc.get("AC$INSTRUMENT").get(0)[2]);
		statementInsertINSTRUMENT.setString(3, acc.get("AC$INSTRUMENT_TYPE").get(0)[2]);
		statementInsertINSTRUMENT.executeUpdate();
		set = statementInsertINSTRUMENT.getGeneratedKeys();
		set.next();
		int instrumentId = set.getInt("ID");
		
		//System.out.println(System.nanoTime());
		statementInsertRECORD.setString(1, acc.get("ACCESSION").get(0)[2]);
		statementInsertRECORD.setString(2, acc.get("RECORD_TITLE").get(0)[2]);
		statementInsertRECORD.setString(3, acc.get("DATE").get(0)[2]);
		statementInsertRECORD.setString(4, acc.get("AUTHORS").get(0)[2]);
		if (acc.get("LICENSE").size() != 0) {
			statementInsertRECORD.setString(5, acc.get("LICENSE").get(0)[2]);			
		} else {
			statementInsertRECORD.setNull(5, java.sql.Types.VARCHAR);
		}
		if (acc.get("COPYRIGHT").size() != 0) {
			statementInsertRECORD.setString(6, acc.get("COPYRIGHT").get(0)[2]);			
		} else {
			statementInsertRECORD.setNull(6, java.sql.Types.VARCHAR);
		}
		if (acc.get("PUBLICATION").size() != 0) {
			statementInsertRECORD.setString(7, acc.get("PUBLICATION").get(0)[2]);			
		} else {
			statementInsertRECORD.setNull(7, java.sql.Types.VARCHAR);
		}
		statementInsertRECORD.setInt(8, compoundId);
		if (sampleId > 0) {
			statementInsertRECORD.setInt(9, sampleId);
		} else {
			statementInsertRECORD.setNull(9, java.sql.Types.INTEGER);
		}
		statementInsertRECORD.setInt(10, instrumentId);
		statementInsertRECORD.setString(11, acc.get("AC$MASS_SPECTROMETRY", "MS_TYPE").get(0)[2]);
		statementInsertRECORD.setString(12, acc.get("AC$MASS_SPECTROMETRY", "ION_MODE").get(0)[2]);
		statementInsertRECORD.setString(13, acc.get("PK$SPLASH").get(0)[2]);
		statementInsertRECORD.executeUpdate();
//		set = statementInsertRECORD.getGeneratedKeys();
//		set.next();
		String accession = acc.get("ACCESSION").get(0)[2];
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("COMMENT")) {
			statementInsertCOMMENT.setString(1, accession);
			statementInsertCOMMENT.setString(2, el[2]);
//			statementInsertCOMMENT.executeUpdate();
			statementInsertCOMMENT.addBatch();
		}
		if (!bulk) {
			statementInsertCOMMENT.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("AC$MASS_SPECTROMETRY")) {
			statementInsertAC_MASS_SPECTROMETRY.setString(1, accession);
			statementInsertAC_MASS_SPECTROMETRY.setString(2, el[1]);
			statementInsertAC_MASS_SPECTROMETRY.setString(3, el[2]);
//			statementInsertAC_MASS_SPECTROMETRY.executeUpdate();
			statementInsertAC_MASS_SPECTROMETRY.addBatch();
		}
		if (!bulk) {
			statementInsertAC_MASS_SPECTROMETRY.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("AC$CHROMATOGRAPHY")) {
			statementInsertAC_CHROMATOGRAPHY.setString(1, accession);
			statementInsertAC_CHROMATOGRAPHY.setString(2, el[1]);
			statementInsertAC_CHROMATOGRAPHY.setString(3, el[2]);
//			statementInsertAC_CHROMATOGRAPHY.executeUpdate();
			statementInsertAC_CHROMATOGRAPHY.addBatch();
		}
		if (!bulk) {
			statementInsertAC_CHROMATOGRAPHY.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("MS$FOCUSED_ION")) {
			statementInsertMS_FOCUSED_ION.setString(1, accession);
			statementInsertMS_FOCUSED_ION.setString(2, el[1]);
			statementInsertMS_FOCUSED_ION.setString(3, el[2]);
//			statementInsertMS_FOCUSED_ION.executeUpdate();
			statementInsertMS_FOCUSED_ION.addBatch();
		}
		if (!bulk) {
			statementInsertMS_FOCUSED_ION.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		for (String[] el : acc.get("MS$DATA_PROCESSING")) {
			statementInsertMS_DATA_PROCESSING.setString(1, accession);
			statementInsertMS_DATA_PROCESSING.setString(2, el[1]);
			statementInsertMS_DATA_PROCESSING.setString(3, el[2]);
//			statementInsertMS_DATA_PROCESSING.executeUpdate();
			statementInsertMS_DATA_PROCESSING.addBatch();
		}
		if (!bulk) {
			statementInsertMS_DATA_PROCESSING.executeBatch();
		}

		//System.out.println(System.nanoTime());
		ArrayList<String[]> peak = acc.get("PK$PEAK");
		for (int i = 1; i < peak.size(); i++) {
			statementInsertPEAK.setString(1, accession);
			String values = peak.get(i)[2];
			statementInsertPEAK.setDouble(2, Double.parseDouble(values.substring(0, values.indexOf(" "))));
			values = values.substring(values.indexOf(" ")+1, values.length());
			statementInsertPEAK.setFloat(3, Float.parseFloat(values.substring(0, values.indexOf(" "))));
			values = values.substring(values.indexOf(" ")+1, values.length());
			statementInsertPEAK.setShort(4, Short.parseShort(values.substring(0, values.length())));
			statementInsertPEAK.setNull(5, java.sql.Types.VARCHAR);
//			statementInsertPEAK.setNull(5, java.sql.Types.VARCHAR);
//			statementInsertPEAK.setNull(6, java.sql.Types.SMALLINT);
//			statementInsertPEAK.setNull(7, java.sql.Types.FLOAT);
//			statementInsertPEAK.setNull(8, java.sql.Types.FLOAT);
//			statementInsertPEAK.executeUpdate();
			statementInsertPEAK.addBatch();
		}
		if (!bulk) {
			statementInsertPEAK.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		ArrayList<String[]> annotation = acc.get("PK$ANNOTATION");
		if (annotation.size() != 0) {
			statementInsertANNOTATION_HEADER.setString(1, accession);
			statementInsertANNOTATION_HEADER.setString(2, annotation.get(0)[2]);
			statementInsertANNOTATION_HEADER.executeUpdate();
		}
		for (int i = 1; i < annotation.size(); i++) {
			String values = annotation.get(i)[2];
			Float mz = Float.parseFloat(values.substring(0, values.indexOf(" ")));
//			values = values.substring(values.indexOf(" ")+1, values.length());
			statementUpdatePEAKs.setString(1, values);
			statementUpdatePEAKs.setString(2, accession);
			statementUpdatePEAKs.setFloat(3, mz);
//			statementUpdatePEAK.setString(1, values.substring(0, values.indexOf(" ")));
//			values = values.substring(values.indexOf(" ")+1, values.length());
//			statementUpdatePEAK.setShort(2, Short.parseShort(values.substring(0, values.indexOf(" "))));
//			values = values.substring(values.indexOf(" ")+1, values.length());
//			statementUpdatePEAK.setFloat(3, Float.parseFloat(values.substring(0, values.indexOf(" "))));
//			values = values.substring(values.indexOf(" ")+1, values.length());
//			statementUpdatePEAK.setFloat(4, Float.parseFloat(values.substring(0, values.length())));
//			statementUpdatePEAK.setString(5, accession);
//			statementUpdatePEAK.setFloat(6, mz);
//			statementUpdatePEAK.executeUpdate();
			statementUpdatePEAKs.addBatch();
		}
		if (!bulk) {
			statementUpdatePEAKs.executeBatch();
		}
		
		//System.out.println(System.nanoTime());
		con.commit();
		//System.out.println(System.nanoTime());
		
		} catch (SQLException e) {
			StringBuilder tmp = new StringBuilder();
			tmp.append(e.getMessage());
			tmp.append("\n");
			for (StackTraceElement el : e.getStackTrace()) {
				tmp.append(el.toString());
				tmp.append("\n");
			}
			DevLogger.printToDBLog("DB ERROR " + tmp + " for accession: " + acc.get("ACCESSION").get(0)[2]);
//			try {
//				e.printStackTrace(new PrintStream(new FileOutputStream("/Users/laptop/Desktop/errors/" + acc.get("ACCESSION").get(0)[2] + ".txt")));
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				//e1.printStackTrace();
//			}
			this.closeConnection();
		} catch (IndexOutOfBoundsException e) {
			StringBuilder tmp = new StringBuilder();
			tmp.append(e.getMessage());
			tmp.append("\n");
			for (StackTraceElement el : e.getStackTrace()) {
				tmp.append(el.toString());
				tmp.append("\n");
			}
			DevLogger.printToDBLog("DB ERROR " + tmp + " for accession: " + acc.get("ACCESSION").get(0)[2]);
//			System.out.println(acc.get("ACCESSION").get(0)[2]);
//			System.out.println(acc.get("PK$PEAK").size());
//			System.out.println(acc.get("PK$ANNOTATION").size());
//			try {
//				e.printStackTrace(new PrintStream(new FileOutputStream("/Users/laptop/Desktop/errors/" + acc.get("ACCESSION").get(0)[2] + ".txt")));
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				//e1.printStackTrace();
//			}
		} catch (Exception e) {
			StringBuilder tmp = new StringBuilder();
			tmp.append(e.getMessage());
			tmp.append("\n");
			for (StackTraceElement el : e.getStackTrace()) {
				tmp.append(el.toString());
				tmp.append("\n");
			}
			DevLogger.printToDBLog("DB ERROR " + tmp + " for accession: " + acc.get("ACCESSION").get(0)[2]);
//			try {
//				e.printStackTrace(new PrintStream(new FileOutputStream("/Users/laptop/Desktop/errors/" + acc.get("ACCESSION").get(0)[2] + ".txt")));
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				//e1.printStackTrace();
//			}
			this.closeConnection();
		}
		this.closeConnection();
	}
	
	public static void main (String[] args) throws SQLException {
//		new DatabaseManager("MassBankNew");
		System.out.println("a b".matches("(\\S+[ ]?)+"));
	}
}

//private DatabaseManager(String dbName) throws SQLException {
//	this.databaseName = dbName;
////	this.connectUrl = "jdbc:mysql://" + this.dbHostName + "/" + this.databaseName;
////	PreparedStatement statementAC_CHROMATOGRAPHY = null;
////	PreparedStatement statementAC_MASS_SPECTROMETRY = null;
////	PreparedStatement statementCH_LINK = null;
////	PreparedStatement statementCOMMENT = null;
////	PreparedStatement statementCOMPOUND = null;
////	PreparedStatement statementCOMPOUND_CLASS = null;
////	PreparedStatement statementCOMPOUND_COMPOUND_CLASS = null;
////	PreparedStatement statementCOMPOUND_NAME = null;
////	PreparedStatement statementINSTRUMENT = null;
////	PreparedStatement statementMS_DATA_PROCESSING = null;
////	PreparedStatement statementMS_FOCUSED_ION = null;
////	PreparedStatement statementNAME = null;
////	PreparedStatement statementPEAK = null;
////	PreparedStatement statementPK_NUM_PEAK = null;
////	PreparedStatement statementRECORD = null;
////	PreparedStatement statementSAMPLE = null;
////	PreparedStatement statementSP_LINK = null;
////	PreparedStatement statementSP_SAMPLE = null;	
////	PreparedStatement statementANNOTATION_HEADER = null;
////	
////	PreparedStatement statementInsertCompound = null;
////	PreparedStatement statementInsertCompound_Class = null;
////	PreparedStatement statementInsertCompound_Compound_Class = null;
////	PreparedStatement statementInsertName = null;
////	PreparedStatement statementInsertCompound_Name = null;
////	PreparedStatement statementInsertCH_LINK = null;
////	PreparedStatement statementInsertSAMPLE = null;
////	PreparedStatement statementInsertSP_LINK = null;
////	PreparedStatement statementInsertSP_SAMPLE = null;
////	PreparedStatement statementInsertINSTRUMENT = null;
////	PreparedStatement statementInsertRECORD = null;
////	PreparedStatement statementInsertCOMMENT = null;
////	PreparedStatement statementInsertAC_MASS_SPECTROMETRY = null;
////	PreparedStatement statementInsertAC_CHROMATOGRAPHY = null;
////	PreparedStatement statementInsertMS_FOCUSED_ION = null;
////	PreparedStatement statementInsertMS_DATA_PROCESSING = null;
////	PreparedStatement statementInsertPEAK = null;
////	PreparedStatement statementUpdatePEAK = null;
////	PreparedStatement statementUpdatePEAKs = null;
////	PreparedStatement statementInsertANNOTATION_HEADER = null;
//	
////	try {
//		this.openConnection();
//		statementAC_CHROMATOGRAPHY = this.con.prepareStatement(sqlAC_CHROMATOGRAPHY);
//		statementAC_MASS_SPECTROMETRY = this.con.prepareStatement(sqlAC_MASS_SPECTROMETRY);
//		statementCH_LINK = this.con.prepareStatement(sqlCH_LINK);
//		statementCOMMENT = this.con.prepareStatement(sqlCOMMENT);
//		statementCOMPOUND = this.con.prepareStatement(sqlCOMPOUND);
//		statementCOMPOUND_CLASS = this.con.prepareStatement(sqlCOMPOUND_CLASS);
//		statementCOMPOUND_COMPOUND_CLASS = this.con.prepareStatement(sqlCOMPOUND_COMPOUND_CLASS);
//		statementCOMPOUND_NAME = this.con.prepareStatement(sqlCOMPOUND_NAME);
//		statementINSTRUMENT = this.con.prepareStatement(sqlINSTRUMENT);
//		statementMS_DATA_PROCESSING = this.con.prepareStatement(sqlMS_DATA_PROCESSING);
//		statementMS_FOCUSED_ION = this.con.prepareStatement(sqlMS_FOCUSED_ION);
//		statementNAME = this.con.prepareStatement(sqlNAME);
//		statementPEAK = this.con.prepareStatement(sqlPEAK);
//		statementPK_NUM_PEAK = this.con.prepareStatement(sqlPK_NUM_PEAK);
//		statementRECORD = this.con.prepareStatement(sqlRECORD);
//		statementSAMPLE = this.con.prepareStatement(sqlSAMPLE);
//		statementSP_LINK = this.con.prepareStatement(sqlSP_LINK);
//		statementSP_SAMPLE = this.con.prepareStatement(sqlSP_SAMPLE);
//		statementANNOTATION_HEADER = this.con.prepareStatement(sqlANNOTATION_HEADER);
//		
//		statementInsertCompound = this.con.prepareStatement(insertCompound);
//		statementInsertCompound_Class = this.con.prepareStatement(insertCompound_Class);
//		statementInsertCompound_Compound_Class = this.con.prepareStatement(insertCompound_Compound_Class);
//		statementInsertName = this.con.prepareStatement(insertName);
//		statementInsertCompound_Name = this.con.prepareStatement(insertCompound_Name);
//		statementInsertCH_LINK = this.con.prepareStatement(insertCH_LINK);
//		statementInsertSAMPLE = this.con.prepareStatement(insertSAMPLE);
//		statementInsertSP_LINK = this.con.prepareStatement(insertSP_LINK);
//		statementInsertSP_SAMPLE = this.con.prepareStatement(insertSP_SAMPLE);
//		statementInsertINSTRUMENT = this.con.prepareStatement(insertINSTRUMENT);
//		statementInsertRECORD = this.con.prepareStatement(insertRECORD);
//		statementInsertCOMMENT = this.con.prepareStatement(insertCOMMENT);
//		statementInsertAC_MASS_SPECTROMETRY = this.con.prepareStatement(insertAC_MASS_SPECTROMETRY);
//		statementInsertAC_CHROMATOGRAPHY = this.con.prepareStatement(insertAC_CHROMATOGRAPHY);
//		statementInsertMS_FOCUSED_ION = this.con.prepareStatement(insertMS_FOCUSED_ION);
//		statementInsertMS_DATA_PROCESSING = this.con.prepareStatement(insertMS_DATA_PROCESSING);
////		statementInsertPEAK = this.con.prepareStatement(insertPEAK);
////		statementUpdatePEAK = this.con.prepareStatement(updatePEAK);
//		statementUpdatePEAKs = this.con.prepareStatement(updatePEAKs);
//		statementInsertANNOTATION_HEADER = this.con.prepareStatement(insertANNOTATION_HEADER);
////	} catch (SQLException e) {
////		e.printStackTrace();
////	} finally {
//////		this.closeConnection();
////		this.statementAC_CHROMATOGRAPHY = statementAC_CHROMATOGRAPHY;
////		this.statementAC_MASS_SPECTROMETRY = statementAC_MASS_SPECTROMETRY;
////		this.statementCH_LINK = statementCH_LINK;
////		this.statementCOMMENT = statementCOMMENT;
////		this.statementCOMPOUND = statementCOMPOUND;
////		this.statementCOMPOUND_CLASS = statementCOMPOUND_CLASS;
////		this.statementCOMPOUND_COMPOUND_CLASS = statementCOMPOUND_COMPOUND_CLASS;
////		this.statementCOMPOUND_NAME = statementCOMPOUND_NAME;
////		this.statementINSTRUMENT = statementINSTRUMENT;
////		this.statementMS_DATA_PROCESSING = statementMS_DATA_PROCESSING;
////		this.statementMS_FOCUSED_ION = statementMS_FOCUSED_ION;
////		this.statementNAME = statementNAME;
////		this.statementPEAK = statementPEAK;
////		this.statementPK_NUM_PEAK = statementPK_NUM_PEAK;
////		this.statementRECORD = statementRECORD;
////		this.statementSAMPLE = statementSAMPLE;
////		this.statementSP_LINK = statementSP_LINK;
////		this.statementSP_SAMPLE = statementSP_SAMPLE;
////		this.statementANNOTATION_HEADER = statementANNOTATION_HEADER;
////		
////		this.statementInsertCompound = statementInsertCompound;
////		this.statementInsertCompound_Class = statementInsertCompound_Class;
////		this.statementInsertCompound_Compound_Class = statementInsertCompound_Compound_Class;
////		this.statementInsertName = statementInsertName;
////		this.statementInsertCompound_Name = statementInsertCompound_Name;
////		this.statementInsertCH_LINK = statementInsertCH_LINK;
////		this.statementInsertSAMPLE = statementInsertSAMPLE;
////		this.statementInsertSP_LINK = statementInsertSP_LINK;
////		this.statementInsertSP_SAMPLE = statementInsertSP_SAMPLE;
////		this.statementInsertINSTRUMENT = statementInsertINSTRUMENT;
////		this.statementInsertRECORD = statementInsertRECORD;
////		this.statementInsertCOMMENT = statementInsertCOMMENT;
////		this.statementInsertAC_MASS_SPECTROMETRY = statementInsertAC_MASS_SPECTROMETRY;
////		this.statementInsertAC_CHROMATOGRAPHY = statementInsertAC_CHROMATOGRAPHY;
////		this.statementInsertMS_FOCUSED_ION = statementInsertMS_FOCUSED_ION;
////		this.statementInsertMS_DATA_PROCESSING = statementInsertMS_DATA_PROCESSING;
////		this.statementInsertPEAK = statementInsertPEAK;
////		this.statementUpdatePEAK = statementUpdatePEAK;
////		this.statementUpdatePEAKs = statementUpdatePEAKs;
////		this.statementInsertANNOTATION_HEADER = statementInsertANNOTATION_HEADER;
////	}
//}