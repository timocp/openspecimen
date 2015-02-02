package com.krishagni.catissueplus.core.administrative.events;

import edu.wustl.catissuecore.domain.SpecimenPosition;

public class SpecimenPositionDetail {
	private Long id;
	
	private Long storageContainerId;
	
	private String posX;
	
	private String posY;
	
	private Integer posXOrdinal;
	
	private Integer posYOrdinal;
	
	private Long specimenId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStorageContainerId() {
		return storageContainerId;
	}

	public void setStorageContainerId(Long storageContainerId) {
		this.storageContainerId = storageContainerId;
	}

	public String getPosX() {
		return posX;
	}

	public void setPosX(String posX) {
		this.posX = posX;
	}

	public String getPosY() {
		return posY;
	}

	public void setPosY(String posY) {
		this.posY = posY;
	}

	public Integer getPosXOrdinal() {
		return posXOrdinal;
	}

	public void setPosXOrdinal(Integer posXOrdinal) {
		this.posXOrdinal = posXOrdinal;
	}

	public Integer getPosYOrdinal() {
		return posYOrdinal;
	}

	public void setPosYOrdinal(Integer posYOrdinal) {
		this.posYOrdinal = posYOrdinal;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}
	
	public static SpecimenPositionDetail from(SpecimenPosition position) {
		SpecimenPositionDetail result = new SpecimenPositionDetail();
		if (position == null) {
			return result;
		}
		
		result.setId(position.getId());
		result.setPosX(position.getPositionDimensionOneString());
		result.setPosY(position.getPositionDimensionTwoString());
		result.setPosXOrdinal(position.getPositionDimensionOne());
		result.setPosYOrdinal(position.getPositionDimensionTwo());
		result.setSpecimenId(position.getSpecimen().getId());
		result.setStorageContainerId(position.getStorageContainer().getId());
		return result;
	}
}
