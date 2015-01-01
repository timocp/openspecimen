/**
 * <p>Title: CollectionProtocolEvent Class</p>
 * <p>Description: A required specimen collection event associated with a Collection Protocol. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;

/**
 * A required specimen collection event associated with a Collection Protocol.
 * @hibernate.class table="CATISSUE_COLL_PROT_EVENT"
 * @author Mandar Deshmukh
 */
public class CollectionProtocolEvent extends AbstractSpecimenCollectionGroup
		implements
			java.io.Serializable,
			Comparable
{

	private final String ACTIVITY_STATUS_ACTIVE = "Active";
	/**
	 * Serial Version id of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;
	/**
	 * Participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 */
	protected String clinicalDiagnosis;
	/**
	 * The clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 */
	protected String clinicalStatus;
	/**
	 * Defines whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;
	/**
	 * A physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 */
	protected Site specimenCollectionSite;


	/**
	 * Patch Id : FutureSCG_13
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Defines the required collectionPointLabel.
	 */
	protected String collectionPointLabel;

	/**
	 * Defines the relative time point in days, with respect to the registration date
	 * of participant on this protocol, when the specimen should be collected from
	 * participant.
	 */
	protected Double studyCalendarEventPoint;

	/**
	 * CollectionProtocol associated with the CollectionProtocolEvent.
	 */
	protected CollectionProtocol collectionProtocol;

	/**
	 * specimenRequirementCollection.
	 */
	protected Collection<SpecimenRequirement> specimenRequirementCollection = new LinkedHashSet<SpecimenRequirement>();

	/**
	 * specimenCollectionGroupCollection.
	 */
	protected Collection specimenCollectionGroupCollection;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	private String labelFormat;
	
	/**
	 * default collection site
	 */
	private Site defaultSite;

	/**
	 * Returns the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_COLL_PROT_EVENT_SEQ"
	 * @return Returns the id.
	 */

	/**
	 * Returns the studyCalendarEventPoint.
	 * @hibernate.property name="studyCalendarEventPoint" type="double"
	 * column="STUDY_CALENDAR_EVENT_POINT" length="50"
	 * @return Returns the studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	/**
	 * @param studyCalendarEventPoint The studyCalendarEventPoint to set.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	 * Returns the collectionProtocol.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_ID" class="edu.wustl.
	 * catissuecore.domain.CollectionProtocol"
	 * constrained="true"
	 * @return Returns the collectionProtocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return this.collectionProtocol;
	}

	/**
	 * @param collectionProtocol
	 *  The collectionProtocol to set.
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * Convert to String method.
	 * @return String type.
	 */
	@Override
	public String toString()
	{
		return "CPE: " + this.clinicalStatus + " | " + this.studyCalendarEventPoint + " | "
				+ this.getId();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues
	 * (edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException when some problem in assigning the data.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		final AbstractActionForm absForm = (AbstractActionForm) abstractForm;
		final SpecimenCollectionGroupForm form = (SpecimenCollectionGroupForm) absForm;
		this.setClinicalDiagnosis(form.getClinicalDiagnosis());
		this.setClinicalStatus(form.getClinicalStatus());
		this.setActivityStatus(form.getActivityStatus());
		this.specimenCollectionSite = new Site();
		this.specimenCollectionSite.setId(Long.valueOf(form.getSiteId()));
	}

	/**
	 * Compare objects.
	 * @param obj of Object type.
	 * @return int type.
	 */
	public int compareTo(Object obj)
	{
		int returnValue = 0;
		if (obj instanceof CollectionProtocolEvent)
		{
			final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) obj;
			if (this.studyCalendarEventPoint.doubleValue() < collectionProtocolEvent
					.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = -1;
			}
			else if (this.studyCalendarEventPoint.doubleValue() > collectionProtocolEvent
					.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = 1;
			}
			else
			{
				returnValue = 0;
			}
		}
		return returnValue;
	}

	/**
	 * Patch Id : FutureSCG_14
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Returns the collectionPointLabel.
	 * @hibernate.property name="collectionPointLabel" type="string"
	 * column="COLLECTION_POINT_LABEL" length="255"
	 * @return Returns the collectionPointLabel of the participant.
	 * @see #setCollectionPointLabel(String)
	 */
	public String getCollectionPointLabel()
	{
		return this.collectionPointLabel;
	}

	/**
	 * Sets collectionPointLabel.
	 * @param collectionPointLabel of String type.
	 * @see #getCollectionPointLabel()
	 */
	public void setCollectionPointLabel(String collectionPointLabel)
	{
		this.collectionPointLabel = collectionPointLabel;
	}

	/**
	 * Get SpecimenCollectionGroup Collection.
	 * @return Collection of SpecimenCollectionGroup.
	 */
	public Collection getSpecimenCollectionGroupCollection()
	{
		return this.specimenCollectionGroupCollection;
	}

	/**
	 * Set the SpecimenCollectionGroup Collection.
	 * @param specimenCollectionGroupCollection of Collectio type.
	 */
	public void setSpecimenCollectionGroupCollection(Collection specimenCollectionGroupCollection)
	{
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	/**
	 * Get SpecimenRequirement Collection.
	 * @return Collection of SpecimenRequirement objects.
	 */
	public Collection<SpecimenRequirement> getSpecimenRequirementCollection()
	{
		return this.specimenRequirementCollection;
	}

	/**
	 * Set the SpecimenRequirement Collection.
	 * @param requirementSpecimenCollection which is Collection of
	 * SpecimenRequirement objects.
	 */
	public void setSpecimenRequirementCollection(
			Collection<SpecimenRequirement> requirementSpecimenCollection)
	{
		this.specimenRequirementCollection = requirementSpecimenCollection;
	}

	/**
	 * Get the CollectionProtocolRegistration.
	 * @return CollectionProtocolRegistration type.
	 */
	@Override
	public CollectionProtocolRegistration getCollectionProtocolRegistration()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the Group Name.
	 * @return String type.
	 */
	@Override
	public String getGroupName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Set the Group Name.
	 * @param name of String type.
	 * @throws BizLogicException if some problem is setting the group name.
	 */
	@Override
	protected void setGroupName(String name) throws BizLogicException
	{
		//
	}

	/**
	 * Get tha label format.
	 * @return String type.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * Set the Label format.
	 * @param labelFormat of String type.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

	public Site getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(Site defaultSite) {
		this.defaultSite = defaultSite;
	}
	
	public boolean isActive()
	{
		return ACTIVITY_STATUS_ACTIVE.equals(this.getActivityStatus());
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the identifer.
	 * @param identifier as long.
	 */
	@Override
	public void setId(final Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @hibernate.property name="clinicalDiagnosis" type="string"
	 * column="CLINICAL_DIAGNOSIS" length="150"
	 * @return the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @see #setClinicalDiagnosis(String)
	 */
	public String getClinicalDiagnosis()
	{
		return this.clinicalDiagnosis;
	}

	/**
	 * Sets the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @param clinicalDiagnosis the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @see #getClinicalDiagnosis()
	 */
	public void setClinicalDiagnosis(final String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	/**
	 * Returns the clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 * @hibernate.property name="clinicalStatus" type="string"
	 * column="CLINICAL_STATUS" length="50"
	 * @return clinical status of the participant at the time of specimen collection.
	 * @see #setClinicalStatus(String)
	 */
	public String getClinicalStatus()
	{
		return this.clinicalStatus;
	}

	/**
	 * Sets the clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 * @param clinicalStatus the clinical status of the participant at the time of specimen collection.
	 * @see #getClinicalStatus()
	 */
	public void setClinicalStatus(final String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * Returns whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 * @hibernate.property name="activityStatus" type="string"
	 * column="ACTIVITY_STATUS" length="50"
	 * @return Active if this record can be queried else returns InActive.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 * @param activityStatus Active if this record can be queried else returns InActive.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(final String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @hibernate.many-to-one column="SITE_ID"
	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	 * @return the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @see #setSpecimenCollectionSite(Site)
	 */
	public Site getSpecimenCollectionSite()
	{
		return this.specimenCollectionSite;
	}

	/**
	 * Sets the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @param cpCollSite Site physical location associated with
	 * biospecimen collection, storage, processing, or utilization.
	 * @see #getSpecimenCollectionSite()
	 */
	public void setSpecimenCollectionSite(final Site cpCollSite)
	{
		this.specimenCollectionSite = cpCollSite;
	}
}