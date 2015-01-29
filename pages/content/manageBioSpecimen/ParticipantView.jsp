<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/participantView.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<style type="text/css">
    #myoutercontainer { text-align: center;display:block;float: left; }
    #myinnercontainer { display: block; vertical-align: middle;*overflow: hidden;}   
    .cprHeadingMargin {margin-left:50px;}
    .windowElementPaddingLeft {padding-left:10px}
    .windowElementPaddingTop {padding-top:15px;}
</style>
<script>
      window.dhx_globalImgPath="dhtmlxSuite_v35/dhtmlxWindows/codebase/imgs/";
     var eventPointLabels = ${requestScope.eventPointLabels}; 
     var scgLabels = ${requestScope.scgLabels}; 
     var specLabelString = ${requestScope.specLabelString};
</script>
<html>
<head>
<LINK type=text/css rel=stylesheet href="css/participantEffects.css" />
<link rel="stylesheet" type="text/css" href="dhtmlxSuite_v35/dhtmlxCombo/codebase/dhtmlxcombo.css" />
</head>
<body onload="initComboForSCGEvents()">

<input type="hidden" name="requestFrom" value="participantView" />
<input type="hidden" name="CPQuery" value="CPQuery" />
<input type="hidden" name="pId" id="pId" value="${participantDto.participantId}" />
<input type="hidden" name="cpId" id="cpId" value="${participantDto.cpId}" />
<input type="hidden" name="cprId" id="cprId" value="${requestScope.cprId}" />


<table width="100%" border="0"  cellpadding="10" cellspacing="0" class="whitetable_bg"> 
    <tr class="tr_bg_blue1 blue_ar_b">
            <td  class="heading_text_style">
                <bean:message key="participant.view.profile.summary"/> <span id="summaryParticipantName"></span>
                [<bean:write name="participantDto" property="ppid" />]
            </td>
    </tr>
    <tr>
        <td class="bottomtd"><div id="msgDiv" style="display:none" class="alert alert-success">Participant updated successfully.</div><div id="errDiv" style="display:none" class="alert alert-error">Error while updating participant, please try again later.</div></td>
    </tr>
    
</table>

<div id="participantDetails" class="align_left_style">
<div>
</div>
<fieldset class="field_set"> 
  <legend class="blue_ar_b legend_font_size"> <bean:message key="participant.view.participant.details"/></legend>
    <table width="100%" border="0"  cellpadding="5px" cellspacing="0"   class="whitetable_bg">
         <tr>
            <td  align="right" class="black_ar  padding_right_style" width="20%">
                <b>ASIG ID</b> 
            </td>
            <td class="black_ar" width="20%">
				<bean:write name="participantDto" property="ppid" />
			</td>
            <td align="right" class="black_ar padding_right_style" width="30%"> 
                <b>Last Contact Date</b>
			</td>
            <td class="black_ar" width="30%"> 
                      <fmt:formatDate value="${participantDto.dethOfDate}" pattern="${datePattern}" />
			</td>
       </tr>
	   
	   <tr class="tr_alternate_color_lightGrey">
         <td  align="right" class="black_ar bottomtd  padding_right_style" width="20%"> 
            <b>UR Number</b>
         </td> 
         <td class="black_ar bottomtd" width="20%">
              <bean:write name="participantDto" property="urNumber" />
        </td>
        <td  align="right" class="black_ar bottomtd  padding_right_style" width="30%"> 
            <b>Consent To Use Of Blood</b>
        </td>
        <td class="black_ar bottomtd" width="30%"> 
             <bean:write name="participantDto" property="consentBlood" />
        </td>
      </tr>
	  
	  <tr>
         <td  align="right" class="black_ar bottomtd  padding_right_style" width="20%"> 
            <b>Status In Research</b>
         </td> 
         <td class="black_ar bottomtd" width="20%">
                        <bean:write name="participantDto" property="vitalStatus" />
        </td>
        <td  align="right" class="black_ar bottomtd  padding_right_style" width="30%"> 
            <b>Site</b>
        </td>
        <td class="black_ar bottomtd" width="30%"> 
             <bean:write name="participantDto" property="siteName" />
        </td>
      </tr>
	  <tr>
         <td  align="right" class="black_ar bottomtd  padding_right_style" width="20%"> 
            <b><bean:message key="participant.dnaQuality"/></b>
         </td> 
         <td class="black_ar bottomtd" width="25%">
           <input type="radio" value="Good" id="dnaQualityGood" name="specimenChild" onclick="onCheckboxButtonClick(this)" 
             style="vertical-align: middle;"/>
	   <span class="black_ar" style="vertical-align: middle;">
	     <bean:message key="participant.dnaQuality.good"/>&nbsp;
	   </span>
	   <input type="radio" value="Poor" id="dnaQualityPoor" name="specimenChild" onclick="onCheckboxButtonClick(this)" 
             style="vertical-align: middle;"/>
	   <span class="black_ar" style="vertical-align: middle;">
	     <bean:message key="participant.dnaQuality.poor"/>&nbsp;
	   </span>
           <input type="radio" value="Unknown" id="dnaQualityUnknown" name="specimenChild" onclick="onCheckboxButtonClick(this)" 
             style="vertical-align: middle;"/>
	   <span class="black_ar" style="vertical-align: middle;">
	     <bean:message key="participant.dnaQuality.unknown"/>&nbsp;
	   </span>   	 
         </td>
         <td  align="left" class="black_ar bottomtd" width="30%"> 
           <input type="button" value="Update" onclick="updateParticipant()"/>
         </td>
         <td class="black_ar bottomtd" width="30%"> 
            
        </td>
      </tr>
    </table>
</fieldset>
</div>

<script>
var dnaQlty = '${requestScope.dnaQlty}';
if(dnaQlty == 'Good') {
  document.getElementById('dnaQualityGood').checked=true;
}
else if(dnaQlty == 'Poor') {
  document.getElementById('dnaQualityPoor').checked=true;
}
else {
  document.getElementById('dnaQualityUnknown').checked=true;
}

function createRequest() {
  var result = null;
  if (window.XMLHttpRequest) {
    // FireFox, Safari, etc.
    result = new XMLHttpRequest();
   
  }
  else if (window.ActiveXObject) {
    // MSIE
    result = new ActiveXObject("Microsoft.XMLHTTP");
  } 
  else {
    // No known mechanism -- consider aborting the application
  }
  return result;
}

function updateParticipant(){
var partId = document.getElementById('pId').value;
var req = createRequest(); 
var radios = document.getElementsByName("specimenChild");
var checkedRadio;
for (var i = 0; i < radios.length; i++) {       
  if (radios[i].checked) {
    checkedRadio=radios[i].value;
    break;
  }
}
// defined above
	// Create the callback:
	req.onreadystatechange = function() {
		if (req.readyState != 4) return; // Not there yet
		var response = eval('('+ req.responseText+')');
		var msgDiv = document.getElementById('msgDiv');
		if(response.success == "success")
		{
			msgDiv.style.display='block';
			errDiv.style.display='none';
		}
		else
		{
			msgDiv.style.display='none';
			errDiv.style.display='block';
		}
	}
	var param =  "pId="+partId+"&dnaQlty="+checkedRadio;
	req.open("GET", "CatissueCommonAjaxAction.do?type=updateParticipant&"+param);
	req.send();
}
</script>
 
</body>
</html>
