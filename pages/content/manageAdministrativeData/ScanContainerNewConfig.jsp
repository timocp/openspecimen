<!doctype html>
<html lang="en">
<head>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.ActionErrors"%>
<%@ page import="org.apache.struts.action.Action"%>
<%@ page import="org.apache.struts.action.ActionError"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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


	<table width="100%" border="0" cellpadding="3" cellspacing="6">
		<form name="containerScan" id="containerScan" action="DisplayScanContainerDetails.do">
		<tr>
                    <td colspan="7" align="left" class="bottomtd"><%@ include
                        file="/pages/content/common/ActionErrors.jsp"%>
                    </td>
                </tr>
                        <tr>
                            <td width="1%" align="center" class="black_ar"><span
                                class="blue_ar_b"></span></td>
                            <td width="17%" align="left" class="black_ar">Container Name</td>
                            <td width="19%" align="left"><input type="text" id="containerName" name="containerName" size="29"/></td>
                            <td width="13%" align="left">&nbsp;</td>

                            
                                
                                <td width="32%" align="left" colspan="3" valign="top">&nbsp;</td>
                            
                        </tr>
						<tr>
                            <td width="1%" align="center" class="black_ar"><span
                                class="blue_ar_b"></span></td>
                            <td width="17%" align="left" class="black_ar">Choose Scanner</td>
                            <td width="19%" align="left">
								<select  tabindex="13" name="boxScanner"  id="boxScanner" class="black_ar">
									<logic:iterate id="scannerList" name="scannerList">
										<logic:notEqual name="scannerList"  property='name' value="-- Select --">
											<option value="<bean:write name='scannerList' property='value'/>"><bean:write name="scannerList" property="name"/></option>
										</logic:notEqual>
									</logic:iterate>
								</select>
							</td>
                            <td width="13%" align="left">&nbsp;</td>
							<td width="32%" align="left" colspan="3" valign="top">&nbsp;</td>
                        </tr>
						<tr>
                            <td width="1%" align="center" class="black_ar"><span
                                class="blue_ar_b"></span></td>
                            <td width="17%" align="left" class="black_ar"><input type="button" name="submit1" value="scan" onClick="getScanDataFromIP()"/></td>
                            <td width="19%" align="left">
								
		 <input type="hidden" name="scannerName" id="scannerName"/>
							</td>
                            <td width="13%" align="left">&nbsp;</td>

                            
                                
                                <td width="32%" align="left" colspan="3" valign="top">&nbsp;</td>
                            
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
	  var containerName = document.getElementById('containerName').value;
	  var boxScanner = collectionEventUserIdCombo.getSelectedText();
	  if(!containerName){
	  alert("Please specify Container Name.");
	  return;
	  }
	  document.getElementById('scannerName').value=collectionEventUserIdCombo.getSelectedText();
	  document.forms["containerScan"].action="DisplayScanContainerDetails.do?scannerName="+boxScanner+"&ctName="+containerName;
	  
	  document.forms["containerScan"].submit();
  }
  var collUserName='';
  var collUserId='';
  var collectionEventUserIdCombo = dhtmlXComboFromSelect("boxScanner");
		collectionEventUserIdCombo.setSize(203);
		//collectionEventUserIdCombo.attachEvent("onChange", function(){onSpecimenTypeChange(this);validateAndProcessDeriveComboData(this);});
		collectionEventUserIdCombo.attachEvent("onOpen",onComboClick);
		collectionEventUserIdCombo.attachEvent("onKeyPressed",onComboKeyPress);
	
	
</script>