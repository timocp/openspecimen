
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.AbstractPosition;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerDetail {

	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private Double tempratureInCentigrade;

	private String siteName;

	private Set<String> allowedCollectionProtocols = new HashSet<String>();

	private Set<String> allowedSpecimenTypes = new HashSet<String>();

	private String comments;

	private Integer dimensionOneCapacity;

	private Integer dimensionTwoCapacity;

	private String dimensionOneLabelingScheme;

	private String dimensionTwoLabelingScheme;
	
	private List<Integer> occupiedPositions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Double getTempratureInCentigrade() {
		return tempratureInCentigrade;
	}

	public void setTempratureInCentigrade(Double tempratureInCentigrade) {
		this.tempratureInCentigrade = tempratureInCentigrade;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Set<String> getAllowedCollectionProtocols() {
		return allowedCollectionProtocols;
	}

	public void setAllowedCollectionProtocols(Set<String> allowedCollectionProtocols) {
		this.allowedCollectionProtocols = allowedCollectionProtocols;
	}

	public Set<String> getAllowedSpecimenTypes() {
		return allowedSpecimenTypes;
	}

	public void setAllowedSpecimenTypes(Set<String> allowedSpecimenTypes) {
		this.allowedSpecimenTypes = allowedSpecimenTypes;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getDimensionOneCapacity() {
		return dimensionOneCapacity;
	}

	public void setDimensionOneCapacity(Integer dimensionOneCapacity) {
		this.dimensionOneCapacity = dimensionOneCapacity;
	}

	public Integer getDimensionTwoCapacity() {
		return dimensionTwoCapacity;
	}

	public void setDimensionTwoCapacity(Integer dimensionTwoCapacity) {
		this.dimensionTwoCapacity = dimensionTwoCapacity;
	}

	public String getDimensionOneLabelingScheme() {
		return dimensionOneLabelingScheme;
	}

	public void setDimensionOneLabelingScheme(String dimensionOneLabelingScheme) {
		this.dimensionOneLabelingScheme = dimensionOneLabelingScheme;
	}

	public String getDimensionTwoLabelingScheme() {
		return dimensionTwoLabelingScheme;
	}

	public void setDimensionTwoLabelingScheme(String dimensionTwoLabelingScheme) {
		this.dimensionTwoLabelingScheme = dimensionTwoLabelingScheme;
	}

	public List<Integer> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(List<Integer> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}

	public static StorageContainerDetail from(StorageContainer storageContainer, boolean includeOccupiedPostions) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setId(storageContainer.getId());
		detail.setActivityStatus(storageContainer.getActivityStatus());
		detail.setBarcode(storageContainer.getBarcode());
		detail.setAllowedCollectionProtocols(getAllowedCollectionProtocols(storageContainer.getCollectionProtocolCollection()));
		detail.setAllowedSpecimenTypes(new HashSet<String>(storageContainer.getHoldsSpecimenTypeCollection()));
		detail.setComments(storageContainer.getComment());
		detail.setName(storageContainer.getName());
		detail.setTempratureInCentigrade(storageContainer.getTempratureInCentigrade());
		detail.setSiteName(storageContainer.getSite().getName());
				
		detail.setDimensionOneCapacity(storageContainer.getCapacity().getOneDimensionCapacity());
		detail.setDimensionTwoCapacity(storageContainer.getCapacity().getTwoDimensionCapacity());		
		detail.setDimensionOneLabelingScheme(storageContainer.getOneDimensionLabellingScheme());
		detail.setDimensionTwoLabelingScheme(storageContainer.getTwoDimensionLabellingScheme());
		
		if (includeOccupiedPostions) {
			detail.setOccupiedPositions(getOccupiedPositions(storageContainer));
		}
				
		return detail;
	}

	private static Set<String> getAllowedCollectionProtocols(Collection<CollectionProtocol> collectionProtocols) {
		Set<String> cps = new HashSet<String>();
		
		for (CollectionProtocol cp : collectionProtocols) {
			cps.add(cp.getTitle());
		}
		
		return cps;
	}
	
	private static List<Integer> getOccupiedPositions(StorageContainer container) {
		Integer dimensionOneCapacity = container.getCapacity().getOneDimensionCapacity();
		
		List<Integer> occupiedPositions = new ArrayList<Integer>();
		for (AbstractPosition ap : container.getSpecimenPositionCollection()) {
			Integer position = (ap.getPositionDimensionTwo() - 1) * dimensionOneCapacity + ap.getPositionDimensionOne();
			occupiedPositions.add(position);
		}
		
		for (AbstractPosition ap : container.getOccupiedPositions()) {
			Integer position = (ap.getPositionDimensionTwo() - 1) * dimensionOneCapacity + ap.getPositionDimensionOne();
			occupiedPositions.add(position);
		}
		
		Collections.sort(occupiedPositions);
		return occupiedPositions;
	}
}
