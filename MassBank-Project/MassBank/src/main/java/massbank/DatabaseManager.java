package massbank;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class DatabaseManager {

	private final static String sqlAccessions			= "SELECT ID FROM RECORD";
	
	private final static String sqlPEAK					= "SELECT * FROM PEAK                 WHERE ID = ? ORDER BY MZ";
//	private final static String sqlSPECTRUM				= "SELECT * FROM SPECTRUM             WHERE  = ?";
//	private final static String sqlTREE					= "SELECT * FROM TREE                 WHERE  = ?";
	private final static String sqlRECORD				= "SELECT * FROM RECORD               WHERE ID = ?";
	private final static String sqlCOMMENT				= "SELECT * FROM COMMENT              WHERE ID = ? ORDER BY NAME";
	private final static String sqlCH_COMPOUND_CLASS	= "SELECT * FROM CH_COMPOUND_CLASS    WHERE ID = ? ORDER BY CLASS";
	private final static String sqlCH_NAME				= "SELECT * FROM CH_NAME              WHERE ID = ? ORDER BY NAME";
	private final static String sqlCH_LINK				= "SELECT * FROM CH_LINK              WHERE ID = ? ORDER BY LINK_NAME";
	private final static String sqlINSTRUMENT			= "SELECT * FROM INSTRUMENT           WHERE INSTRUMENT_NO = ?";
	private final static String sqlAC_MASS_SPECTROMETRY	= "SELECT * FROM AC_MASS_SPECTROMETRY WHERE ID = ? ORDER BY NAME";
	private final static String sqlAC_CHROMATOGRAPHY	= "SELECT * FROM AC_CHROMATOGRAPHY    WHERE ID = ? ORDER BY NAME";
	private final static String sqlMS_FOCUSED_ION		= "SELECT * FROM MS_FOCUSED_ION       WHERE ID = ? ORDER BY NAME";
	private final static String sqlMS_DATA_PROCESSING	= "SELECT * FROM MS_DATA_PROCESSING   WHERE ID = ? ORDER BY NAME";
	
	private final String driver;
	private final String user;
	private final String password;
	
	private final String databaseName;
	private final String dbHostName;
	private final String connectUrl;
	private final Connection con;
	
	private final PreparedStatement statementAccessions			 ;
	
	private final PreparedStatement statementPEAK				 ;
//	private final PreparedStatement statementSPECTRUM			 ;
//	private final PreparedStatement statementTREE				 ;
	private final PreparedStatement statementRECORD				 ;
	private final PreparedStatement statementCOMMENT			 ;
	private final PreparedStatement statementCH_COMPOUND_CLASS	 ;
	private final PreparedStatement statementCH_NAME			 ;
	private final PreparedStatement statementCH_LINK			 ;
	private final PreparedStatement statementINSTRUMENT			 ;
	private final PreparedStatement statementAC_MASS_SPECTROMETRY;
	private final PreparedStatement statementAC_CHROMATOGRAPHY	 ;
	private final PreparedStatement statementMS_FOCUSED_ION		 ;
	private final PreparedStatement statementMS_DATA_PROCESSING	 ;
	
	public DatabaseManager(String databaseName) {
		// collect connection data
		this.driver		= "org.mariadb.jdbc.Driver";
//		this.driver		= "com.mysql.jdbc.Driver";
		this.user		= MassBankEnv.get(MassBankEnv.KEY_DB_USER);
		this.password	= MassBankEnv.get(MassBankEnv.KEY_DB_PASSWORD);
		
		this.databaseName = databaseName;
		String dbHostName = MassBankEnv.get(MassBankEnv.KEY_DB_HOST_NAME);
		if ( !MassBankEnv.get(MassBankEnv.KEY_DB_MASTER_NAME).equals("") ) {
			dbHostName = MassBankEnv.get(MassBankEnv.KEY_DB_MASTER_NAME);
		}
		this.dbHostName	= dbHostName;
		this.connectUrl = "jdbc:mysql://" + this.dbHostName + "/" + this.databaseName;
		
		// create connection
		this.con = this.openConnection();
		
		// create prepared statements
		PreparedStatement statementAccessions			= null;
		
		PreparedStatement statementPEAK					= null;
//		PreparedStatement statementSPECTRUM				= null;
//		PreparedStatement statementTREE					= null;
		PreparedStatement statementRECORD				= null;
		PreparedStatement statementCOMMENT				= null;
		PreparedStatement statementCH_COMPOUND_CLASS	= null;
		PreparedStatement statementCH_NAME				= null;
		PreparedStatement statementCH_LINK				= null;
		PreparedStatement statementINSTRUMENT			= null;
		PreparedStatement statementAC_MASS_SPECTROMETRY	= null;
		PreparedStatement statementAC_CHROMATOGRAPHY	= null;
		PreparedStatement statementMS_FOCUSED_ION		= null;
		PreparedStatement statementMS_DATA_PROCESSING	= null;
		try {
			statementAccessions				= this.con.prepareStatement(sqlAccessions			);
			
			statementPEAK					= this.con.prepareStatement(sqlPEAK					);
//			statementSPECTRUM				= this.con.prepareStatement(sqlSPECTRUM				);
//			statementTREE					= this.con.prepareStatement(sqlTREE					);
			statementRECORD					= this.con.prepareStatement(sqlRECORD				);
			statementCOMMENT				= this.con.prepareStatement(sqlCOMMENT				);
			statementCH_COMPOUND_CLASS		= this.con.prepareStatement(sqlCH_COMPOUND_CLASS	);
			statementCH_NAME				= this.con.prepareStatement(sqlCH_NAME				);
			statementCH_LINK				= this.con.prepareStatement(sqlCH_LINK				);
			statementINSTRUMENT				= this.con.prepareStatement(sqlINSTRUMENT			);
			statementAC_MASS_SPECTROMETRY	= this.con.prepareStatement(sqlAC_MASS_SPECTROMETRY	);
			statementAC_CHROMATOGRAPHY		= this.con.prepareStatement(sqlAC_CHROMATOGRAPHY	);
			statementMS_FOCUSED_ION			= this.con.prepareStatement(sqlMS_FOCUSED_ION		);
			statementMS_DATA_PROCESSING		= this.con.prepareStatement(sqlMS_DATA_PROCESSING	);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// store prepared statements
		this.statementAccessions			= statementAccessions			;
		
		this.statementPEAK					= statementPEAK					;
//		this.statementSPECTRUM				= statementSPECTRUM				;
//		this.statementTREE					= statementTREE					;
		this.statementRECORD				= statementRECORD				;
		this.statementCOMMENT				= statementCOMMENT				;
		this.statementCH_COMPOUND_CLASS		= statementCH_COMPOUND_CLASS	;
		this.statementCH_NAME				= statementCH_NAME				;
		this.statementCH_LINK				= statementCH_LINK				;
		this.statementINSTRUMENT			= statementINSTRUMENT			;
		this.statementAC_MASS_SPECTROMETRY	= statementAC_MASS_SPECTROMETRY	;
		this.statementAC_CHROMATOGRAPHY		= statementAC_CHROMATOGRAPHY	;
		this.statementMS_FOCUSED_ION		= statementMS_FOCUSED_ION		;
		this.statementMS_DATA_PROCESSING	= statementMS_DATA_PROCESSING	;
	}
	private Connection openConnection() {
		Connection con	= null;
		try {
			Class.forName(this.driver);
			con = DriverManager.getConnection(this.connectUrl, this.user, this.password);
			
			// 自動コミットモードを解除
			// no auto-commit
			con.setAutoCommit(false);

			// トランザクション分離レベルをセット
			// set transaction isolation level
			con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return con;
	}
	public void closeConnection() {
		try {
			if ( this.con != null ) this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String[] getAccessions(){
		String[] accessions	= null;
		try {
			// get accession IDs
			List<String> accessionList	= new ArrayList<String>();
			ResultSet resultSet	= this.statementRECORD.executeQuery();
			while(resultSet.next())	accessionList.add(resultSet.getString("ID"));
			resultSet.close();
			
			// box accession IDs
			accessions	= accessionList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return accessions;
	}
	public AccessionData getAccessionData(String accessionId){
		// initialization of fields
		String ACCESSION		= null;
		String RECORD_TITLE		= null;
		Date DATE				= null;
		String AUTHORS			= null;
		String LICENSE			= null;
		String COPYRIGHT		= null;
		String PUBLICATION		= null;
		List<String> COMMENT	= new ArrayList<String>();
		List<String> CH$NAME	= new ArrayList<String>();
		List<String> CH$COMPOUND_CLASS_NAME		= new ArrayList<String>();
		List<String> CH$COMPOUND_CLASS_CLASS	= new ArrayList<String>();
		String CH$FORMULA		= null;
		double CH$EXACT_MASS	= -1;
		String CH$SMILES		= null;
		String CH$IUPAC			= null;
		List<String> CH$LINK_NAME	= new ArrayList<String>();
		List<String> CH$LINK_ID		= new ArrayList<String>();
		String AC$INSTRUMENT		= null;
		String AC$INSTRUMENT_TYPE	= null;
		List<String> AC$MASS_SPECTROMETRY	= new ArrayList<String>();
		List<String> AC$CHROMATOGRAPHY		= new ArrayList<String>();
		List<String> MS$FOCUSED_ION			= new ArrayList<String>();
		List<String> MS$DATA_PROCESSING		= new ArrayList<String>();
		String PK$SPLASH			= null;
//		List<String> PK$ANNOTATION	= new ArrayList<String>();
		int PK$NUM_PEAK				= -1;
		List<Double> PK$PEAK_MZ		= new ArrayList<Double>();
		List<Double> PK$PEAK_INT	= new ArrayList<Double>();
		List<Short> PK$PEAK_REL		= new ArrayList<Short>();
		
		// fetch information
		try {
			// tmp
			ResultSet resultSet	= null;
			int INSTRUMENT_NO	= -1;
			
			// RECORD				
			this.statementRECORD.setString(1, accessionId);
			resultSet	= this.statementRECORD.executeQuery();
			if(!resultSet.next()){
				resultSet.close();
				System.out.println("No entry for accession " + accessionId);
				return null;
			}
			ACCESSION		= resultSet.getString("ID");
			// general information
			RECORD_TITLE	= resultSet.getString(	"RECORD_TITLE");
			DATE			= resultSet.getDate(	"DATE");
			AUTHORS			= resultSet.getString(	"AUTHORS");
			LICENSE			= resultSet.getString(	"LICENSE");
			COPYRIGHT		= resultSet.getString(	"COPYRIGHT");
			PUBLICATION		= resultSet.getString(	"PUBLICATION");
			// structure information
			CH$FORMULA		= resultSet.getString(	"FORMULA");
			CH$EXACT_MASS	= resultSet.getDouble(	"EXACT_MASS");
			INSTRUMENT_NO	= resultSet.getInt(		"INSTRUMENT_NO");
			// miscellaneous
			PK$SPLASH		= resultSet.getString(	"PK_SPLASH");
			CH$SMILES		= resultSet.getString(	"SMILES");
			CH$IUPAC		= resultSet.getString(	"IUPAC");
//			MS_TYPE			= resultSet.getString(	"MS_TYPE");
			
			// INSTRUMENT			
			this.statementINSTRUMENT.setInt(1, INSTRUMENT_NO);
			resultSet	= this.statementINSTRUMENT.executeQuery();
			if(resultSet.next()){
				AC$INSTRUMENT		= resultSet.getString(	"INSTRUMENT_NAME");
				AC$INSTRUMENT_TYPE	= resultSet.getString(	"INSTRUMENT_TYPE");
			} else{
				resultSet.close();
				return null;
			}
			// PEAK					
			this.statementPEAK.setString(1, accessionId);
			resultSet	= this.statementPEAK.executeQuery();
			while(resultSet.next()){
				PK$PEAK_MZ.add( resultSet.getDouble("MZ"));
				PK$PEAK_INT.add(resultSet.getDouble("INTENSITY"));
				PK$PEAK_REL.add(resultSet.getShort(	"RELATIVE"));
			}
			resultSet.close();
//			// SPECTRUM				
//			this.statementSPECTRUM				
//			// TREE					
//			this.statementTREE					
			// COMMENT				
			this.statementCOMMENT.setString(1, accessionId);
			resultSet	= this.statementCOMMENT.executeQuery();
			while(resultSet.next())	COMMENT.add(resultSet.getString("NAME"));
			resultSet.close();			
			// CH_COMPOUND_CLASS		
			this.statementCH_COMPOUND_CLASS.setString(1, accessionId);
			resultSet	= this.statementCH_COMPOUND_CLASS.executeQuery();
			while(resultSet.next()){
				CH$COMPOUND_CLASS_NAME .add(resultSet.getString("NAME"));
				CH$COMPOUND_CLASS_CLASS.add(resultSet.getString("CLASS"));
			}
			resultSet.close();	
			// CH_NAME				
			this.statementCH_NAME.setString(1, accessionId);
			resultSet	= this.statementCH_NAME.executeQuery();
			while(resultSet.next())	CH$NAME.add(resultSet.getString("NAME"));
			resultSet.close();		
			// CH_LINK				
			this.statementCH_LINK.setString(1, accessionId);
			resultSet	= this.statementCH_LINK.executeQuery();
			while(resultSet.next()){
				CH$LINK_NAME.add(resultSet.getString("LINK_NAME"));
				CH$LINK_ID  .add(resultSet.getString("LINK_ID"));
			}
			resultSet.close();			
			// AC_MASS_SPECTROMETRY	
			this.statementAC_MASS_SPECTROMETRY.setString(1, accessionId);
			resultSet	= this.statementAC_MASS_SPECTROMETRY.executeQuery();
			while(resultSet.next())	AC$MASS_SPECTROMETRY.add(resultSet.getString("NAME"));
			resultSet.close();
			// AC_CHROMATOGRAPHY		
			this.statementAC_CHROMATOGRAPHY.setString(1, accessionId);
			resultSet	= this.statementAC_CHROMATOGRAPHY.executeQuery();
			while(resultSet.next())	AC$CHROMATOGRAPHY.add(resultSet.getString("NAME"));
			resultSet.close();
			// MS_FOCUSED_ION		
			this.statementMS_FOCUSED_ION.setString(1, accessionId);
			resultSet	= this.statementMS_FOCUSED_ION.executeQuery();
			while(resultSet.next())	MS$FOCUSED_ION.add(resultSet.getString("NAME"));
			resultSet.close();
			// MS_DATA_PROCESSING	
			this.statementMS_DATA_PROCESSING.setString(1, accessionId);
			resultSet	= this.statementMS_DATA_PROCESSING.executeQuery();
			while(resultSet.next())	MS$DATA_PROCESSING.add(resultSet.getString("NAME"));
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		// box information
		return new AccessionData(
				ACCESSION, 
				RECORD_TITLE, 
				DATE, 
				AUTHORS, 
				LICENSE, 
				COPYRIGHT, 
				PUBLICATION, 
				COMMENT.toArray(new String[0]), 
				CH$NAME.toArray(new String[0]), 
				CH$COMPOUND_CLASS_NAME.toArray(new String[0]), 
				CH$COMPOUND_CLASS_CLASS.toArray(new String[0]), 
				CH$FORMULA, 
				CH$EXACT_MASS, 
				CH$SMILES, 
				CH$IUPAC, 
				CH$LINK_NAME.toArray(new String[0]), 
				CH$LINK_ID.toArray(new String[0]), 
				AC$INSTRUMENT, 
				AC$INSTRUMENT_TYPE, 
				AC$MASS_SPECTROMETRY.toArray(new String[0]), 
				AC$CHROMATOGRAPHY.toArray(new String[0]), 
				MS$FOCUSED_ION.toArray(new String[0]), 
				MS$DATA_PROCESSING.toArray(new String[0]), 
				PK$SPLASH, 
//				PK$ANNOTATION.toArray(new String[0]), 
				PK$NUM_PEAK, 
				ArrayUtils.toPrimitive(PK$PEAK_MZ.toArray(new Double[0])),
				ArrayUtils.toPrimitive(PK$PEAK_INT.toArray(new Double[0])),
				ArrayUtils.toPrimitive(PK$PEAK_REL.toArray(new Short[0]))
		);
	}
}
