
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;

public class BoxScannerDetail {

	private String name;

	private String ipAddress;

	private Long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public static BoxScannerDetail fromDomain(BoxScanner boxScanner){
		BoxScannerDetail detail = new BoxScannerDetail();
		detail.setId(boxScanner.getId());
		detail.setIpAddress(boxScanner.getIpAddress());
		detail.setName(boxScanner.getName());
		return detail;
	}

}
