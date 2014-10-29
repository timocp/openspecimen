
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllDistributionProtocolEvent extends RequestEvent {

	private String shortTitle;
	
	private int maxResults;
	
	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
