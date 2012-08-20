window.dhx_globalImgPath = "dhtmlx_suite/imgs/";

var gridOn = 0, gridInit=0;
var timerForInvestigatorFilter, piText;

// This function will initialise Custom DHTMLX DropDown control
function initDropDownGrid(gridDropDownInfo) 
{
		var gridObj = new dhtmlXGridObject(gridDropDownInfo['gridObj']);
		gridObj.setImagePath("dhtmlx_suite/imgs/");
		gridObj.setHeader(" ");
		gridObj.setInitWidths("*");
		gridObj.setColAlign("left");
		gridObj.setColSorting("server");
		gridObj.setSkin("drop");
		gridObj.enablePaging(true, 500, 0, gridDropDownInfo['pagingArea'], true, gridDropDownInfo['infoArea']);
		gridObj.setPagingSkin("bricks");
		
		gridObj.enableRowsHover(true, "gridHover");
		gridObj.enableMultiline(true);
		gridObj.init();
		
		// event to be call on selection of option from the dropdown
		gridObj.attachEvent("onRowSelect", gridDropDownInfo['onOptionSelect']); 
		
		//if the row is selected by arrow keys and if we move the mouse over 
		//the grid rows then the previously selected rows remains selected
		gridObj.attachEvent("onMouseOver", function(id,ind){ gridObj.selectRowById(id, false, true , false);
		});
		
		//whenever the page is changed the focus is lost from the text box due 
		//to which the arrow keys won't respond since we are handling key press event on the text box
		gridObj.attachEvent("onPageChanged", function(ind,fInd,lInd){
			document.getElementById(gridDropDownInfo['dropDownId']).focus();
		});
		hideGrid(gridDropDownInfo['gridDiv']);
		gridObj.load(gridDropDownInfo['actionToDo'], gridDropDownInfo['callBackAction']);
		return gridObj;
}



//Arrow key navigation is not supported by the grids without selecting a row. So we are handling the arrow key press on the text box used for both the
//dropdowns.whenever the button is clicked (that is present beside the text box) it sets the focus on the text box.
//The below method takes the key code and accordingly handles the condition the left and right arrow keys will change the page and up and down keys will 
//select the values in the grid

function keyNavigation(evnt,gridDropDownInfo, gridObj,isGridVisible)
{
	if(isGridVisible)
	{
		if(evnt.keyCode == 40)//KeyDown
		{
			var next = gridObj.getRowIndex(gridObj.getSelectedRowId())+1;
			gridObj.selectRow(next);
		}
		else if(evnt.keyCode == 38) //keyUp
		{
			var previous = gridObj.getRowIndex(gridObj.getSelectedRowId())-1;
			gridObj.selectRow(previous);
		}
		else if(evnt.keyCode == 37) //keyLeft
		{
			gridObj.changePage(gridObj.currentPage-1);
		}
		else if(evnt.keyCode == 39)//keyRight
		{
			gridObj.changePage(gridObj.currentPage+1);
		}
		else if(evnt.keyCode == 13)//KeyEnter
		{
				document.getElementById(gridDropDownInfo['dropDownId']).value = gridObj.cellById(gridObj.getSelectedRowId(),0).getValue();
				gridObj.selectRowById(gridObj.getSelectedRowId(), false, true , true);
				hideGrid(gridDropDownInfo['gridDiv']);
		}
	}
}

function autoCompleteControl(event,gridDropDownInfo,gridObj)
{ 
	if(event.keyCode != 13 && event.keyCode != 39 && event.keyCode != 37 )
	{
		if(document.getElementById(gridDropDownInfo['dropDownId']).value=="")
		{
			document.getElementsByName(gridDropDownInfo['propertyId']).value = "";
			//hideGrid(gridDropDownInfo['gridDiv']);
			//gridDropDownInfo['visibilityStatusVariable'] = false;
			gridInit=0;
		}
		else
		{
			if(gridOn==0)
			{
				showGrid(gridDropDownInfo['gridDiv'],gridDropDownInfo['dropDownId']);
				gridDropDownInfo['visibilityStatusVariable'] = true;
			}
		}
		//gridObj.loadXML(gridDropDownInfo['actionToDo']+"&containerName="+document.getElementById(gridDropDownInfo['dropDownId']).value,gridDropDownInfo['callBackAction']);
		gridObj.clearAndLoad(gridDropDownInfo['actionToDo']+"&containerName="+document.getElementById(gridDropDownInfo['dropDownId']).value, gridDropDownInfo['callBackAction']);
		//gridObj.filterBy(0,document.getElementById(gridDropDownInfo['dropDownId']).value);
	}	
}

function hideGrid(gridContDiv1)
{	
	document.getElementById(gridContDiv1).style.display = "none";
	gridOn = 0;
}

function showGrid(gridContainingDiv,dropDownId)
{	
	document.getElementById(gridContainingDiv).style.display = "block";
	document.getElementById(gridContainingDiv).style.position = "absolute";
	document.getElementById(gridContainingDiv).style.top = document.getElementById(dropDownId).style.top + document.getElementById(dropDownId).style.height;
	gridOn=1;
	//piGrid.clearAndLoad("ParticipantConnector.do?gridFor=" + pGridInfo['gridDiv'] + "&csId=" +csId , pGridCallBack);
}

function showHideGrid(e,dropDownInfoObject,gridObject)
{		
		setValue(e,dropDownInfoObject['gridDiv'], dropDownInfoObject['dropDownId']);
		//alert(dropDownInfoObject['visibilityStatusVariable']);
		if(dropDownInfoObject['visibilityStatusVariable'])
		{
			hideGrid(dropDownInfoObject['gridDiv']);
			dropDownInfoObject['visibilityStatusVariable'] = false;
		}
		else 
		 {	
			showGrid(dropDownInfoObject['gridDiv'],dropDownInfoObject['dropDownId']);
			dropDownInfoObject['visibilityStatusVariable'] = true;
			//gridObject.load(dropDownInfoObject['actionToDo'],"");
			gridObject.filterBy(0,"");
		 }
} 

function rowSelectEvent(selectedRowId,index,infoObj, gridObject)
{
	document.getElementsByName(infoObj['propertyId'])[0].value = selectedRowId;
	document.getElementById(infoObj['dropDownId']).value = gridObject.cellById(selectedRowId,index).getValue();
	//document.getElementById("waitingImage").style.display = "block";
	hideGrid(infoObj['gridDiv']);
	infoObj['visibilityStatusVariable'] = false;
}

//Stop event propogation.
function noEventPropogation(e)
{
	if (window.event)//IE
	{
		window.event.cancelBubble = true;
	}
	else 
	{
		e.stopPropagation();
	}
}

function setValue(e,gridDivId, dropDownId)
{
		document.getElementById(dropDownId).focus();
		noEventPropogation(e);
}

