<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
    <script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
	<script  language="JavaScript" type="text/javascript"src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.js"></script> 
	<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/custom/dhtmlxgrid_drag_custom.js"></script> 
	<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script> 
	<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxgrid.css">
	<script language="JavaScript" type="text/javascript" src='dhtmlxSuite_v35/dhtmlxGrid/codebase/ext/dhtmlxgrid_export.js'></script>
	<link rel="stylesheet" type="text/css" href="css/catissue_suite.css">
	<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.css">
	<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_skyblue.css">
	<script src="dhtmlxSuite_v35/dhtmlxTabbar/codebase/dhtmlxcontainer.js"></script>
	<script src="dhtmlxSuite_v35/dhtmlxWindows/codebase/dhtmlxwindows.js"></script>
	<script  src="jss/wz_tooltip.js" type="text/javascript"></script>
<script language="JavaScript"  type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCombo/codebase/ext/dhtmlxcombo_whp.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css"/>


<style>
.a span {
    color: RED;
}
</style>

</head>
<body>
  <table border="0" width="100%">
    <form name="containerScan" id="containerScan" action="DisplayScanContainerDetails.do">
	<tr height="20%">
	  <td>
		&nbsp;
	  </td>
	</tr>
    <tr>
      <td width="5%">
        
      </td>
      <td class="black_ar_b" width="15%">
        IP Address: &nbsp;<input type="text" id="ipAddress" name="ipAddress"/>
      </td>
	  <td align="left" width="15%">
	  <select name="contName" class="black_ar" id="contName"></select>
	  </td>
	  <td align="left" width="1%">
         <input type="button" name="submit1" value="scan" onClick="getScanDataFromIP()"/>
		 <input type="hidden" name="selCont" id="selCont"/>
      </td>
	  <td align="left" width="30%">
        
      </td>
    </tr>
    <tr>
      <td colspan="3">
      
      </td>
    </tr>
    </form>
  </table>
</body>
</html>
<script>
      window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
</script>
<script>
  function getScanDataFromIP(){
	  var ipAdd = document.getElementById('ipAddress').value;
	  var contName = collectionEventUserIdCombo.getSelectedText();
	  if(!ipAdd){
	  alert("Please specify the IP address.");
	  return;
	  }alert(contName);
	  document.getElementById('selCont').value=collectionEventUserIdCombo.getSelectedText();
	  document.forms["containerScan"].action="DisplayScanContainerDetails.do?ctName='"+contName+"'";
	  document.forms["containerScan"].submit();
  }
  var collUserName='';
  var collUserId='';
  var collectionEventUserIdCombo = new dhtmlXCombo("contName","contName","100px");;
	collectionEventUserIdCombo.setOptionWidth(200);
	collectionEventUserIdCombo.setSize(200);
	collectionEventUserIdCombo.loadXML('/openspecimen/CatissueCommonAjaxAction.do?type=getContainerNames',function(){
		collectionEventUserIdCombo.setComboText(collUserName);
		collectionEventUserIdCombo.setComboValue(collUserId);
		collectionEventUserIdCombo.DOMelem_input.title=collUserName;
	
	});
	
	collectionEventUserIdCombo.attachEvent("onKeyPressed",function(){
		collectionEventUserIdCombo.enableFilteringMode(true,'/openspecimen/CatissueCommonAjaxAction.do?type=getContainerNames',false);
		collectionEventUserIdCombo.attachEvent("onChange", function(){collectionEventUserIdCombo.DOMelem_input.focus();});
		});
	//collectionEventUserIdCombo.attachEvent("onOpen",onComboClick);
	collectionEventUserIdCombo.attachEvent("onSelectionChange",function(){
var diagnosisVal = collectionEventUserIdCombo.getSelectedText();
		if(diagnosisVal)
			collectionEventUserIdCombo.DOMelem_input.title=collectionEventUserIdCombo.getSelectedText();
		else
			collectionEventUserIdCombo.DOMelem_input.title='Start typing to see values';
});
	collectionEventUserIdCombo.attachEvent("onXLE",function (){collectionEventUserIdCombo.addOption(collUserId,collUserName);});
	dhtmlxEvent(collectionEventUserIdCombo.DOMelem_input,"mouseover",function(){
var diagnosisVal = collectionEventUserIdCombo.getSelectedText();
		if(diagnosisVal){
			collectionEventUserIdCombo.DOMelem_input.title=collectionEventUserIdCombo.getSelectedText();}
		else
			collectionEventUserIdCombo.DOMelem_input.title='Start typing to see values';
});
</script>