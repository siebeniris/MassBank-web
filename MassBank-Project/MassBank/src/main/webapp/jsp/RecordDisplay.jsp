<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
/*******************************************************************************
 *
 * Copyright (C) 2010 JST-BIRD MassBank
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *******************************************************************************
 *
 ******************************************************************************/
%>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="massbank.GetConfig" %>
<%@ page import="massbank.MassBankEnv" %>
<%@ page import="massbank.AccessionData" %>
<%@ page import="massbank.StructureToSvgStringGenerator" %>
<%@ page import="massbank.StructureToSvgStringGenerator.ClickablePreviewImageData" %>
<%
	// get accession and database name from url parameters 
	// http://localhost/MassBank/jsp/RecordDisplay.jsp?id=XXX00001
	// String accession = "XXX00001";
	
	String accession = null;
	
	Enumeration<String> names = request.getParameterNames();
	while ( names.hasMoreElements() ) {
		String key = (String) names.nextElement();
		String val = (String) request.getParameter( key );	
		switch(key){
			case "id":	accession = val; 
						break;
			default:		System.out.println("Warning: Unused argument " + key + "=" + val);
		}
	}
	
	// if accession is missing abort
	if(accession != null && accession.equals("")) 
		accession = null;	
	if(accession == null) {
			System.out.println("Error: Missing argument 'id'");
			return;
	}

	// get accession data
	
	// AccessionData accData = AccessionData.getAccessionDataFromFile(databaseName, accession);
	AccessionData accData = AccessionData.getAccessionDataFromDatabase(accession);
	if(accData == null)
		return;
	
	String shortName	= accData.get("RECORD_TITLE").get(0)[2];
	shortName = shortName.substring(0,shortName.indexOf(";")).trim();
			
	// get clickable image data for structure view
	
	String tmpUrlFolder		= MassBankEnv.get(MassBankEnv.KEY_BASE_URL) + "temp";
	//String tmpUrlFolder		= request.getServletContext().getAttribute("ctx").toString() + "/temp";// ${ctx}
	String tmpFileFolder	= MassBankEnv.get(MassBankEnv.KEY_TOMCAT_APPTEMP_PATH);
	ClickablePreviewImageData clickablePreviewImageData	= StructureToSvgStringGenerator.createClickablePreviewImage(
			accData, tmpFileFolder, tmpUrlFolder,
			80, 200, 436
	);
	
	String svgMedium	= null;
	if(clickablePreviewImageData != null)
		svgMedium	= clickablePreviewImageData.getMediumClickableImage();
	
	// compile entry information
	StringBuilder sb	= new StringBuilder();
	
	/*
All links:
----------

'LICENSE:',								'https://creativecommons.org/licenses/',
'PUBLICATION:',							'http://www.ncbi.nlm.nih.gov/pubmed/%s?dopt=Citation',
'COMMENT: \[MSn\]',						'Dispatcher.jsp?type=disp&id=%s&site=%s',
'COMMENT: \[Merging\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s',
'COMMENT: \[Merged\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s',
'COMMENT: \[Mass spectrometry\]',		'',
'COMMENT: \[Chromatography\]',			'',
'COMMENT: \[Profile\]',					'../DB/profile/%s/%s',
'COMMENT: \[Mixture\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s',
'CH\$FORMULA:',							'http://www.chemspider.com/Search.aspx?q=%s',
'CH\$LINK: CAS',						'https://www.google.com/search?q=&quot;%s&quot;',
'CH\$LINK: CAYMAN',						'http://www.caymanchem.com/app/template/Product.vm/catalog/%s',
'CH\$LINK: CHEBI',						'http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:%s',
'CH\$LINK: CHEMPDB',					'http://www.ebi.ac.uk/msd-srv/chempdb/cgi-bin/cgi.pl?FUNCTION=getByCode&amp;CODE=%s',
'CH\$LINK: CHEMSPIDER',					'http://www.chemspider.com/%s',
'CH\$LINK: COMPTOX',                    'https://comptox.epa.gov/dashboard/dsstoxdb/results?search=%s',
'CH\$LINK: FLAVONOIDVIEWER',			'http://www.metabolome.jp/software/FlavonoidViewer/',
'CH\$LINK: HMDB',						'http://www.hmdb.ca/metabolites/%s',
'CH\$LINK: INCHIKEY',					'https://www.google.com/search?q=&quot;%s&quot;',
'CH\$LINK: KAPPAVIEW',					'http://kpv.kazusa.or.jp/kpv4/compoundInformation/view.action?id=%s',
'CH\$LINK: KEGG',						'http://www.genome.jp/dbget-bin/www_bget?%s:%s',
'CH\$LINK: KNAPSACK',					'http://kanaya.naist.jp/knapsack_jsp/info.jsp?sname=C_ID&word=%s',
'CH\$LINK: LIPIDBANK',					'http://lipidbank.jp/cgi-bin/detail.cgi?id=%s',
'CH\$LINK: LIPIDMAPS',					'http://www.lipidmaps.org/data/get_lm_lipids_dbgif.php?LM_ID=%s',
'CH\$LINK: NIKKAJI',					'http://nikkajiweb.jst.go.jp/nikkaji_web/pages/top.jsp?SN=%s&CONTENT=syosai',
'CH\$LINK: PUBCHEM',					'http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?',
'CH\$LINK: OligosaccharideDataBase',	'http://www.fukuyama-u.ac.jp/life/bio/biochem/%s.html%s',
'CH\$LINK: OligosaccharideDataBase2D',	'http://www.fukuyama-u.ac.jp/life/bio/biochem/%s.html',
'SP\$LINK: NCBI-TAXONOMY',				'http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=%s',
'SP\$SAMPLE: LOCATION', 				'https://www.ebi.ac.uk/ontology-lookup/?termId=%s',
'MS\$RELATED_MS: PREVIOUS_SPECTRUM',	'Dispatcher.jsp?type=disp&id=%s',
#'PK\$SPLASH:', 							'https://www.google.com/search?q=&quot;%s&quot;'
'PK\$SPLASH:',                          'http://mona.fiehnlab.ucdavis.edu/#/spectra/splash/%s'

Links not implemented yet:
--------------------------
'COMMENT: \[MSn\]',						'Dispatcher.jsp?type=disp&id=%s&site=%s'
'COMMENT: \[Merging\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s'
'COMMENT: \[Merged\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s'
'COMMENT: \[Mass spectrometry\]',		''
'COMMENT: \[Chromatography\]',			''
'COMMENT: \[Profile\]',					'../DB/profile/%s/%s'
'COMMENT: \[Mixture\]',					'Dispatcher.jsp?type=disp&id=%s&site=%s'
'SP\$SAMPLE: LOCATION', 				'https://www.ebi.ac.uk/ontology-lookup/?termId=%s'
'MS\$RELATED_MS: PREVIOUS_SPECTRUM',	'Dispatcher.jsp?type=disp&id=%s'

<a href="URL" target="_blank">VAL</a>
"<a href=\"URL\" target=\"_blank\">" + VAL + "</a>"
	*/	

	sb.append("ACCESSION: " + accData.get("ACCESSION").get(0)[2] + "\n");
	if(accData.get("RECORD_TITLE").get(0)[2] != null)	
		sb.append("RECORD_TITLE: " + accData.get("RECORD_TITLE").get(0)[2] + "\n");
	if(accData.get("DATE").get(0)[2] != null)			
		sb.append("DATE: " + accData.get("DATE").get(0)[2] + "\n");
	if(accData.get("AUTHORS").get(0)[2] != null)			
		sb.append("AUTHORS: " + accData.get("AUTHORS").get(0)[2] + "\n");
	if(accData.get("LICENSE").get(0)[2] != null)			
		sb.append("LICENSE: " + "<a href=\"https://creativecommons.org/licenses/\" target=\"_blank\">" + accData.get("LICENSE").get(0)[2] + "</a>" + "\n");
	if(accData.get("COPYRIGHT").get(0)[2] != null)		
		sb.append("COPYRIGHT: " + accData.get("COPYRIGHT").get(0)[2] + "\n");
	
	if(accData.get("PUBLICATION").get(0)[2] != null){
		Pattern pattern = Pattern.compile(".*(PMID:[ ]?\\d{8,8}).*");
	    Matcher matcher = pattern.matcher(accData.get("PUBLICATION").get(0)[2]);
	    if(matcher.matches()){
	    	String PMID	= accData.get("PUBLICATION").get(0)[2].substring(matcher.start(1), matcher.end(1));
	    	String id	= PMID.substring("PMID:".length()).trim();
	    	String PUBLICATION	= accData.get("PUBLICATION").get(0)[2].replaceAll(PMID, "<a href=\"http:\\/\\/www.ncbi.nlm.nih.gov/pubmed/" + id + "?dopt=Citation\" target=\"_blank\">" + PMID + "</a>");
	    	sb.append("PUBLICATION: " + PUBLICATION + "\n");
	    } else {
			sb.append("PUBLICATION: " + accData.get("PUBLICATION").get(0)[2] + "\n");
	    }
	}
	
	for(String COMMENT[] : accData.get("COMMENT"))
		sb.append("COMMENT: " + COMMENT[2] + "\n");
	sb.append("<hr size=\"1\" color=\"silver\" width=\"98%\" align=\"left\">");
	for(String CH$NAME[] : accData.get("CH$NAME"))
		sb.append("CH$NAME: " + CH$NAME[2] + "\n");
	for(int idx = 0; idx < accData.get("CH$COMPOUND_CLASS").size(); idx++)
		sb.append("CH$COMPOUND_CLASS: " + accData.get("CH$COMPOUND_CLASS").get(idx)[2] + "\n");
//		sb.append("CH$COMPOUND_CLASS: " + accData.CH$COMPOUND_CLASS_CLASS[idx] + " " + accData.CH$COMPOUND_CLASS_NAME[idx] + "\n");
	if(accData.get("CH$FORMULA").get(0)[2] != null)		
		sb.append("CH$FORMULA: " + "<a href=\"http://www.chemspider.com/Search.aspx?q=" + accData.get("CH$FORMULA").get(0)[2] + "\" target=\"_blank\">" + accData.get("CH$FORMULA").get(0)[2] + "</a>" + "\n");
	if(Double.parseDouble(accData.get("CH$EXACT_MASS").get(0)[2]) != -1)		
		sb.append("CH$EXACT_MASS: " + accData.get("CH$EXACT_MASS").get(0)[2] + "\n");
	if(accData.get("CH$SMILES").get(0)[2] != null)		
		sb.append("CH$SMILES: " + accData.get("CH$SMILES").get(0)[2] + "\n");
	if(accData.get("CH$IUPAC").get(0)[2] != null)		
		sb.append("CH$IUPAC: " + accData.get("CH$IUPAC").get(0)[2] + "\n");
	
	// TODO fetch from AccessionData accData
	String CH$CDK_DEPICT_SMILES				= accData.get("CH$CDK_DEPICT_SMILES").get(0)[2];//"CCOCCOCCO |Sg:n:3,4,5:2:ht| PEG-2";
	String CH$CDK_DEPICT_GENERIC_SMILES		= accData.get("CH$CDK_DEPICT_GENERIC_SMILES").get(0)[2];//"c1ccc(cc1)/C=C/C(=O)O[R]";
	String CH$CDK_DEPICT_STRUCTURE_SMILES	= accData.get("CH$CDK_DEPICT_STRUCTURE_SMILES").get(0)[2];//"c1ccc(cc1)/C=C/C(=O)O";
	
	if(CH$CDK_DEPICT_SMILES != null){
		ClickablePreviewImageData clickablePreviewImageData2	= StructureToSvgStringGenerator.createClickablePreviewImage(
				CH$CDK_DEPICT_SMILES, null, CH$CDK_DEPICT_SMILES,
				tmpFileFolder, tmpUrlFolder,
				80, 200, 436
		);
		if(clickablePreviewImageData2 != null)
			sb.append("CH$CDK_DEPICT_SMILES: " + clickablePreviewImageData2.getMediumClickablePreviewLink("CH$CDK_DEPICT_SMILES", CH$CDK_DEPICT_SMILES));
		else
			sb.append("CH$CDK_DEPICT_SMILES: " + CH$CDK_DEPICT_SMILES + "\n");
	}
	
	if(CH$CDK_DEPICT_GENERIC_SMILES != null){
		ClickablePreviewImageData clickablePreviewImageData3	= StructureToSvgStringGenerator.createClickablePreviewImage(
				CH$CDK_DEPICT_GENERIC_SMILES, null, CH$CDK_DEPICT_GENERIC_SMILES,
				tmpFileFolder, tmpUrlFolder,
				80, 200, 436
		);
		if(clickablePreviewImageData3 != null)
			sb.append("CH$CDK_DEPICT_GENERIC_SMILES: " + clickablePreviewImageData3.getMediumClickablePreviewLink("CH$CDK_DEPICT_GENERIC_SMILES", CH$CDK_DEPICT_GENERIC_SMILES));
		else
			sb.append("CH$CDK_DEPICT_GENERIC_SMILES: " + CH$CDK_DEPICT_GENERIC_SMILES + "\n");
	}
	
	if(CH$CDK_DEPICT_STRUCTURE_SMILES != null){
		ClickablePreviewImageData clickablePreviewImageData4	= StructureToSvgStringGenerator.createClickablePreviewImage(
				CH$CDK_DEPICT_STRUCTURE_SMILES, null, CH$CDK_DEPICT_STRUCTURE_SMILES,
				tmpFileFolder, tmpUrlFolder,
				80, 200, 436
		);
		if(clickablePreviewImageData4 != null)
			sb.append("CH$CDK_DEPICT_STRUCTURE_SMILES: " + clickablePreviewImageData4.getMediumClickablePreviewLink("CH$CDK_DEPICT_STRUCTURE_SMILES", CH$CDK_DEPICT_STRUCTURE_SMILES));
		else
			sb.append("CH$CDK_DEPICT_STRUCTURE_SMILES: " + CH$CDK_DEPICT_STRUCTURE_SMILES + "\n");
	}
	
	for(int idx = 0; idx < accData.get("CH$LINK").size(); idx++){
		String CH$LINK_ID	= accData.get("CH$LINK").get(idx)[2];
		switch(accData.get("CH$LINK").get(idx)[1]){
			case "CAS":								CH$LINK_ID	= "<a href=\"https://www.google.com/search?q=&quot;" + CH$LINK_ID + "&quot;"									+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "CAYMAN":                      	CH$LINK_ID	= "<a href=\"https://www.caymanchem.com/app/template/Product.vm/catalog/" + CH$LINK_ID							+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "CHEBI":                       	CH$LINK_ID	= "<a href=\"https://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:" + CH$LINK_ID								+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "CHEMPDB":                     	CH$LINK_ID	= "<a href=\"https://www.ebi.ac.uk/msd-srv/chempdb/cgi-bin/cgi.pl?FUNCTION=getByCode&amp;CODE=" + CH$LINK_ID	+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "CHEMSPIDER":                  	CH$LINK_ID	= "<a href=\"https://www.chemspider.com/" + CH$LINK_ID															+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "COMPTOX": 	                	CH$LINK_ID	= "<a href=\"https://comptox.epa.gov/dashboard/dsstoxdb/results?search=" + CH$LINK_ID							+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "FLAVONOIDVIEWER":             	CH$LINK_ID	= "<a href=\"http://www.metabolome.jp/software/FlavonoidViewer/"												+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "HMDB":                        	CH$LINK_ID	= "<a href=\"http://www.hmdb.ca/metabolites/" + CH$LINK_ID														+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "INCHIKEY":                    	CH$LINK_ID	= "<a href=\"https://www.google.com/search?q=&quot;" + CH$LINK_ID + "&quot;"									+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "KAPPAVIEW":                   	CH$LINK_ID	= "<a href=\"http://kpv.kazusa.or.jp/kpv4/compoundInformation/view.action?id=" + CH$LINK_ID						+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "KEGG":                        	CH$LINK_ID	= "<a href=\"http://www.genome.jp/dbget-bin/www_bget?cpd:" + CH$LINK_ID											+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "KNAPSACK":                    	CH$LINK_ID	= "<a href=\"http://kanaya.naist.jp/knapsack_jsp/info.jsp?sname=C_ID&word=" + CH$LINK_ID						+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "LIPIDBANK":                   	CH$LINK_ID	= "<a href=\"http://lipidbank.jp/cgi-bin/detail.cgi?id=" + CH$LINK_ID											+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "LIPIDMAPS":                   	CH$LINK_ID	= "<a href=\"http://www.lipidmaps.org/data/get_lm_lipids_dbgif.php?LM_ID=" + CH$LINK_ID							+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "NIKKAJI":                     	CH$LINK_ID	= "<a href=\"https://jglobal.jst.go.jp/en/redirect?Nikkaji_No=" + CH$LINK_ID + "&CONTENT=syosai"		+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "OligosaccharideDataBase":     	CH$LINK_ID	= "<a href=\"http://www.fukuyama-u.ac.jp/life/bio/biochem/" + CH$LINK_ID + ".html" + CH$LINK_ID					+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "OligosaccharideDataBase2D":   	CH$LINK_ID	= "<a href=\"http://www.fukuyama-u.ac.jp/life/bio/biochem/" + CH$LINK_ID + ".html"								+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "NCBI-TAXONOMY":               	CH$LINK_ID	= "<a href=\"https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=" + CH$LINK_ID							+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>"; break;
			case "PUBCHEM":{
				if(CH$LINK_ID.startsWith("CID:"))	CH$LINK_ID	= "<a href=\"https://pubchem.ncbi.nlm.nih.gov/compound/" + CH$LINK_ID.substring("CID:".length())				+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>";
				if(CH$LINK_ID.startsWith("SID:"))	CH$LINK_ID	= "<a href=\"https://pubchem.ncbi.nlm.nih.gov/substance/" + CH$LINK_ID.substring("SID:".length())				+ "\" target=\"_blank\">" + CH$LINK_ID + "</a>";
				break;
			}
		}
		
		sb.append("CH$LINK: " + accData.get("CH$LINK").get(idx)[1] + " " + CH$LINK_ID + "\n");
	}
	sb.append("<hr size=\"1\" color=\"silver\" width=\"98%\" align=\"left\">");
	sb.append("AC$INSTRUMENT: " + accData.get("AC$INSTRUMENT").get(0)[2] + "\n");
	sb.append("AC$INSTRUMENT_TYPE: " + accData.get("AC$INSTRUMENT_TYPE").get(0)[2] + "\n");
	if (accData.get("AC$MASS_SPECTROMETRY") != null) {
		for(String AC$MASS_SPECTROMETRY[] : accData.get("AC$MASS_SPECTROMETRY")) {
			sb.append("AC$MASS_SPECTROMETRY: " + AC$MASS_SPECTROMETRY[1] + " " + AC$MASS_SPECTROMETRY[2] + "\n");
		}
	}
	if (accData.get("AC$CHROMATOGRAPHY") != null) {
		for(String AC$CHROMATOGRAPHY[] : accData.get("AC$CHROMATOGRAPHY")) {
			sb.append("AC$CHROMATOGRAPHY: " + AC$CHROMATOGRAPHY[1] + " " + AC$CHROMATOGRAPHY + "\n");
		}
	}
	sb.append("<hr size=\"1\" color=\"silver\" width=\"98%\" align=\"left\">");
	if (accData.get("MS$FOCUSED_ION") != null) {
		for(String MS$FOCUSED_ION[] : accData.get("MS$FOCUSED_ION")) {
			sb.append("MS$FOCUSED_ION: " + MS$FOCUSED_ION[1] + " " + MS$FOCUSED_ION[2] + "\n");
		}
	}
	if (accData.get("MS$DATA_PROCESSING") != null) {
		for(String MS$DATA_PROCESSING[] : accData.get("MS$DATA_PROCESSING")) {
			sb.append("MS$DATA_PROCESSING: " + MS$DATA_PROCESSING[1] + " " + MS$DATA_PROCESSING[2] + "\n");
		}
	}
	sb.append("<hr size=\"1\" color=\"silver\" width=\"98%\" align=\"left\">");
	if(accData.get("PK$SPLASH").get(0)[2] != null)		
		sb.append("PK$SPLASH: " + "<a href=\"http://mona.fiehnlab.ucdavis.edu/#/spectra/splash/" +accData.get("PK$SPLASH").get(0)[2] + "\" target=\"_blank\">" + accData.get("PK$SPLASH").get(0)[2] + "</a>" + "\n"); // https://www.google.com/search?q=&quot;%s&quot;
//	PK$ANNOTATION: m/z tentative_formula formula_count mass error(ppm)
//	  57.0701 C4H9+ 1 57.0699 4.61
//	  67.0542 C5H7+ 1 67.0542 0.35
//	  69.0336 C4H5O+ 1 69.0335 1.14
	sb.append("PK$NUM_PEAK: " + accData.get("PK$NUM_PEAK").get(0)[2] + "\n");
	
	sb.append("PK$PEAK: m/z int. rel.int." + "\n");
	
// 	String[] PK$PEAK_MZ		= accData.formatPK$PEAK_MZ(true);
// 	String[] PK$PEAK_INT	= accData.formatPK$PEAK_INT(true);
// 	String[] PK$PEAK_REL	= accData.formatPK$PEAK_REL(true);
	for(int idx = 0; idx < accData.get("PK$PEAK").size(); idx++)
		sb.append("  " + accData.get("PK$PEAK").get(idx)[2] + "\n");
// 		sb.append("  " + 
// 				PK$PEAK_MZ [idx] + " " + 
// 				PK$PEAK_INT[idx] + " " + 
// 				PK$PEAK_REL[idx] + "\n"
// 		);
	sb.append("//");
	
	String recordString	= sb.toString();	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="author" content="MassBank" />
		<meta name="coverage" content="worldwide" />
		<meta name="Targeted Geographic Area" content="worldwide" />
		<meta name="rating" content="general" />
		<meta name="copyright" content="Copyright (c) 2006 MassBank Project and (c) 2011 NORMAN Association" />
		<meta name="description" content="MassBank Record of <%=accData.get("ACCESSION").get(0)[2]%>">
		<meta name="keywords" content="<%=shortName%>, mass spectrum, MassBank record, mass spectrometry, mass spectral library">
		<meta name="revisit_after" content="30 days">
		<meta name="hreflang" content="en">
		<meta name="variableMeasured" content="m/z">
		<meta http-equiv="Content-Style-Type" content="text/css">
		<meta http-equiv="Content-Script-Type" content="text/javascript">
		<link rel="stylesheet" type="text/css" href="../css/Common.css">
		<script type="text/javascript" src="../script/Common.js"></script>
		<!-- SpeckTackle dependencies-->
		<script type="text/javascript" src="https://code.jquery.com/jquery-1.8.3.min.js" ></script>
		<script type="text/javascript" src="../script/StructurePreview.js"></script>
		<script type="text/javascript" src="https://d3js.org/d3.v3.min.js"></script>
		<!-- SpeckTackle library-->
		<script type="text/javascript" src="../script/st.min.js" charset="utf-8"></script>
		<!-- SpeckTackle style sheet-->
		<link rel="stylesheet" href="../css/st.css" type="text/css" />
		<!-- SpeckTackle MassBank loading script-->
		<script type="text/javascript" src="../script/massbank_specktackle.js"></script>
		<title><%=shortName%> Mass Spectrum</title>
	</head>
	<body style="font-family:Times;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td>
					<h1>MassBank Record: <%=accData.get("ACCESSION").get(0)[2]%> </h1>
				</td>
			</tr>
		</table>
		<iframe src="../menu.html" width="860" height="30px" frameborder="0" marginwidth="0" scrolling="no"></iframe>
		<hr size="1">
		<br>
		<font size="+1" style="background-color:LightCyan">&nbsp;<%=accData.get("RECORD_TITLE").get(0)[2]%> (3)&nbsp;</font>
		<hr size="1">
		<table>
			<tr>
				<td valign="top">
					<font style="font-size:10pt;" color="dimgray">Mass Spectrum</font>
					<br>
					<div id="spectrum_canvas" style="height: 200px; width: 750px; background-color: white"></div>
				</td>
				<td valign="top">
					<font style="font-size:10pt;" color="dimgray">Chemical Structure</font><br>
					<% // display svg
					if(clickablePreviewImageData != null){
						// paste small image to web site %>
						<%=svgMedium%><%
					} else {
						// no structure there or svg generation failed%>
						<img src="../image/not_available_s.gif" width="200" height="200" style="margin:0px;">
					<%}%></td>
			</tr>
		</table>
<hr size="1">
<pre style="font-family:Courier New;font-size:10pt">
<%=recordString%>
</pre>
		<hr size=1>
		<iframe src="../copyrightline.html" width="800" height="20px" frameborder="0" marginwidth="0" scrolling="no"></iframe>
	</body>
</html>