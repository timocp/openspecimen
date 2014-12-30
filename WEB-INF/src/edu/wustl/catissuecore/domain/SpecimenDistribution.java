
package edu.wustl.catissuecore.domain;

import java.util.Date;

import edu.wustl.common.domain.AbstractDomainObject;

public class SpecimenDistribution extends AbstractDomainObject implements java.io.Serializable {

	private Long id;

	private DistributionProtocol distributionProtocol;

	private Specimen specimen;

	private User requestor;

	private Site site;

	private String status;

	private Date sentDate;

	private String distributionTitle;

	private String comments;

	private Double quantity;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public DistributionProtocol getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocol distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	public User getRequestor() {
		return requestor;
	}

	public void setRequestor(User requestor) {
		this.requestor = requestor;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getDistributionTitle() {
		return distributionTitle;
	}

	public void setDistributionTitle(String distributionTitle) {
		this.distributionTitle = distributionTitle;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
