<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>searchSpectrum メソッド</title>
<link rel="stylesheet" type="text/css" href="api_ref.css">
</head>
<body>
<span class="head">MassBank WEB-API リファレンス</span>
<hr>
<h1 class="method">searchSpectrum メソッド</h1>
<b>類似スペクトルを検索します。</b><br>
<br>
<b>パラメータ</b><br>
<table border="1" width="850">
<tr>
<td><b>mzs</b></td><td>[型]string 配列</td><td>ピークの m/z値</td>
</tr>
<tr>
<td><b>intensities</b></td><td>[型]string 配列</td><td>ピークの intensity値</td>
</tr>
<tr>
<td><b>unit</b></td><td>[型]string</td><td>Tolerance値の単位。"unit" または "ppm"を指定する。(指定しない場合は"unit")</td>
</tr>
<tr>
<td><b>tolerance</b></td><td>[型]string</td><td>Tolerance値。(指定しない場合は、"unit"指定時:0.3, "ppm"指定時:50)</td> 
</tr>
<tr>
<td><b>cutoff</b></td><td>[型]string</td><td>cutoffで指定された値以下のintensityは無視する。(指定しない場合は50)</td>
</tr>
<tr>
<td><b>instrumentTypes</b></td><td>[型]string 配列</td>
<td>
分析機器種別を指定する（複数指定可）。"all"を指定した場合は、すべての種別が検索対象になります。<br>
getInstrumentTypes メソッドにて取得した値以外は指定しないでください。<br>
</td>
</tr>
<tr>
<td><b>ionMode</b></td><td>[型]string</td>
<td>
イオン化モード<br>
"Positive", "Negative", "Both"のいずれか(大文字・小文字区別なし)を指定します。<br>
</td>
</tr>
<tr>
</tr>
<tr>
<td><b>maxNumResults</b></td><td>[型]int</td><td>検索結果数の上限。"0"を指定した場合はすべてを取得します。</td>
</tr>
</table>

<br><br>
<b>レスポンス</b><br>
検索結果を&nbsp;<span class="dtype">SearchResult</span>&nbsp;型（以下の構造体）で返します。<br>
<table border="1" width="850">
<tr><td>
&nbsp;&nbsp;-&nbsp;<b>numResults</b>&nbsp;:&nbsp;検索結果数&nbsp;[型]int<br>
&nbsp;&nbsp;-&nbsp;<b>results</b>&nbsp;:&nbsp;ヒットしたレコードの情報&nbsp;&nbsp;[型]<span class="dtype">Result</span>&nbsp;型（以下の構造体）の配列<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;｜<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├&nbsp;<b>id</b>&nbsp;:&nbsp;MassBank ID [型]string<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;｜&nbsp;<b>title</b>&nbsp;:&nbsp;レコードタイトル [型]string<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;｜&nbsp;<b>formula</b>&nbsp;:&nbsp;分子式 [型]string<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;｜&nbsp;<b>exactMass</b>&nbsp;:&nbsp;精密質量 [型]string<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└&nbsp;<b>score</b>&nbsp;:&nbsp;スコア [型]string<br>
</td></tr>
</table>
<br>
<b>例外</b><br>
エラーメッセージ<br>
"Invalid parameter : xxxxx"&nbsp;:&nbsp;パラメータが不正な場合
<br><br>
</div>
<hr color="silver">
<br>
■PHPソースコード<br>
<div class="src">
<pre>
<font color="green">// パラメータセット</font>
$mzs = array('273.096', '289.086', '290.118', '291.096', '292.113', '579.169', '580.179');
$inte = array('300', '300', '300', '300', '300', '300', '300' );
$unit = "";		<font color="green">// unit値は省略可(デフォルト"unit")</font>
$tol = "";			<font color="green">// tolerance値は省略可(デフォルト"0.3")</font>
$cutoff = "";		<font color="green">// cutoff値は省略可(デフォルト"50")</font>
$inst = array("all");
$ion = "Positive";
$params = array(
	"mzs" => $mzs, "intensities" => $inte,   "unit" => $unit,
	"tolerance" => $tol, "cutoff" => $cutoff, "instrumentTypes" => $inst,
	"ionMode" => $ion, "maxNumResults" => 20
);

<font color="green">// メソッド呼び出し</font>
$soap = new SoapClient('http://www.massbank.jp/api/services/MassBankAPI?wsdl');
<font color="blue">try {</font>
	$res = $soap-><b>searchSpectrum</b>( $params );
<font color="blue">}
catch (SoapFault $e) {
	echo $e->getMessage();
	return;
}
</font>
<font color="green">// 検索結果表示</font>
$ret = $res->return;
echo "&lt;table&gt;\n";
for ($i = 0 ; $i < $ret->numResults; $i++) {
	$info = $ret->results[$i];
	echo "&lt;tr&gt\n";
	echo "&lt;td&gt$info->score&lt;/td&gt";
	echo "&lt;td&gt$info->title&lt;/td&gt";
	echo "&lt;td&gt$info->formula&lt;/td&gt";
	echo "&lt;td&gt$info->exactMass&lt;/td&gt";
	echo "&lt;td&gt$info->id&lt;/td&gt\n";
	echo "&lt;/tr&gt\n";
}
echo "&lt;/table&gt\n";
</pre>
</div>
<br>
<br>
■検索結果(実際の結果です)<br>
<div class="res2">
<?php
$mzs = array('273.096', '289.086', '290.118', '291.096', '292.113', '579.169', '580.179');
$inte = array('300', '300', '300', '300', '300', '300', '300' );
$unit = "unit";
$tol = "0.3";
$cutoff = "50";
$inst = array("all");
$ion = "Positive";
$params = array(
	"mzs" => $mzs, "intensities" => $inte,   "unit" => $unit,
	"tolerance" => $tol, "cutoff" => $cutoff, "instrumentTypes" => $inst,
	"ionMode" => $ion, "maxNumResults" => 20
);


//$soap = new SoapClient('http://www.massbank.jp/api/services/MassBankAPI?wsdl');
$soap = new SoapClient('http://localhost/api/services/MassBankAPI?wsdl');
try {
	$res = $soap->searchSpectrum( $params );
}
catch (SoapFault $e) {
	echo $e->getMessage();
	return;
}

// 検索結果表示
$ret = $res->return;
echo "<table>\n";
for ($i = 0 ; $i < $ret->numResults; $i++) {
	$info = $ret->results[$i];
	echo "<tr>\n";
	echo "<td>$info->score</td>";
	echo "<td>$info->title</td>";
	echo "<td>$info->formula</td>";
	echo "<td>$info->exactMass</td>";
	echo "<td>$info->id</td>\n";
	echo "</tr>\n";
}
echo "</table>\n";
?>
</div>
</body>
</html>
