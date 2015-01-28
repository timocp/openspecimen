package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class GetAllBoxScannerEvent extends ResponseEvent{

	private List<BoxScannerDetail> scannerDetails;

	
	public List<BoxScannerDetail> getScannerDetails() {
		return scannerDetails;
	}

	
	public void setScannerDetails(List<BoxScannerDetail> scannerDetails) {
		this.scannerDetails = scannerDetails;
	}
	
	public static GetAllBoxScannerEvent ok(List<BoxScannerDetail> details) {
		GetAllBoxScannerEvent event = new GetAllBoxScannerEvent();
		event.setScannerDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
}
