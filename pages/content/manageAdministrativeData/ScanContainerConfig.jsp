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
      <td>
        
      </td>
      <td class="black_ar_b">
        IP Address: &nbsp;<input type="text" id="ipAddress" name="ipAddress"/>&nbsp;<input type="button" name="submit1" value="scan" onClick="getScanDataFromIP()"/>
      </td>
	  <td align="left">
        
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
  function getScanDataFromIP(){
	  var ipAdd = document.getElementById('ipAddress').value;
	  if(!ipAdd){
	  alert("Please specify the IP address.");
	  return;
	  }
	  document.forms["containerScan"].action="DisplayScanContainerDetails.do?ipAddress='"+ipAdd+"'";
	  document.forms["containerScan"].submit();
  }
</script>