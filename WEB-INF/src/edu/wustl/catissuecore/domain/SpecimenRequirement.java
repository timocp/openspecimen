
package edu.wustl.catissuecore.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.common.util.global.Validator;

/**
 * SpecimenRequirement class.
 */
public class SpecimenRequirement extends AbstractSpecimen
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -34444448799655L;

	protected AbstractSpecimen parentSpecimen;
	/**
	 * Collection of childSpecimenCollection derived from this specimen.
	 */
	protected Collection<AbstractSpecimen> childSpecimenCollection = new LinkedHashSet<AbstractSpecimen>();

	/**
	 * The anatomical site from which a specimen is derived.
	 */
	protected String tissueSite;

	/**
	 * For bilateral sites, left or right.
	 */
	protected String tissueSide;

	/**
	 * Collection of Specimen Event Parameters associated with this specimen.
	 */
	protected Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
	/**
	 * pathologicalStatus - Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologicalStatus;
	/**
	 * lineage - A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot
	 */
	protected String lineage;
	/**
	 * label - A label name of this specimen.
	 */
	protected String label;
	/**
	 * initialQuantity - The quantity of a specimen.
	 */
	protected Double initialQuantity;
	/**
	 * specimenClass - Tissue, Molecular,Fluid and Cell.
	 */
	protected String specimenClass;
	/**
	 * specimenType - Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;
	private Date collectionTimestamp;

	/**
	 * User who performs the event.
	 */
	private User collector;

	/**
	 * Text comment on event.
	 */
	private String collectionComments;

	private String collectionProcedure;

	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	private String collectionContainer;

	private String receivedQuality;

	private Date receivedTimestamp;
	/**
	 * User who performs the event.
	 */
	private User receiver;
	/**
	 * Text comment on event.
	 */
	private String receivedComments;

	private String storageType;

	/**
	 * collectionProtocolEvent.
	 */
	private CollectionProtocolEvent collectionProtocolEvent;

	/**
	 * specimenCollection.
	 */
	private Collection<Specimen> specimenCollection;

	/**
	 * labelFormat.
	 */
	private String labelFormat;

	private String specimenRequirementLabel;

	/**
	 * Defines the status of Specimen Requirement.
	 */
	private String activityStatus;

	private  Long id;
	
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the identifier.
	 * @param identifier which is of Long type.
	 */
	@Override
	public void setId(final Long identifier)
	{
		this.id = identifier;
	}
	
	public Date getCollectionTimestamp()
	{
		return collectionTimestamp;
	}

	public void setCollectionTimestamp(Date collectionTimestamp)
	{
		this.collectionTimestamp = collectionTimestamp;
	}

	public User getCollector()
	{
		return collector;
	}

	public void setCollector(User collector)
	{
		this.collector = collector;
	}

	public String getCollectionComments()
	{
		return collectionComments;
	}

	public void setCollectionComments(String collectionComments)
	{
		this.collectionComments = collectionComments;
	}
	
	public String getCollectionProcedure()
	{
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	public String getCollectionContainer()
	{
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer)
	{
		this.collectionContainer = collectionContainer;
	}

	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	public Date getReceivedTimestamp()
	{
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(Date receivedTimestamp)
	{
		this.receivedTimestamp = receivedTimestamp;
	}

	public User getReceiver()
	{
		return receiver;
	}

	public void setReceiver(User receiver)
	{
		this.receiver = receiver;
	}

	public String getReceivedComments()
	{
		return receivedComments;
	}

	public void setReceivedComments(String receiverComments)
	{
		this.receivedComments = receiverComments;
	}

	public String getSpecimenRequirementLabel()
	{
		return specimenRequirementLabel;
	}

	public void setSpecimenRequirementLabel(String title)
	{
		this.specimenRequirementLabel = title;
	}

	/**
	 * Get CollectionProtocolEvent.
	 * @return CollectionProtocolEvent.
	 */
	public CollectionProtocolEvent getCollectionProtocolEvent()
	{
		return this.collectionProtocolEvent;
	}

	/**
	 * @param collectionProtocolEvent CollectionProtocolEvent.
	 */
	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	/**
	 * Get StorageType.
	 * @return String.
	 */
	public String getStorageType()
	{
		return this.storageType;
	}

	/**
	 * Set StorageType.
	 * @param storageType String.
	 */
	public void setStorageType(String storageType)
	{
		this.storageType = storageType;
	}

	/**
	 * Get SpecimenCollection.
	 * @return Collection of Specimen type.
	 */
	public Collection<Specimen> getSpecimenCollection()
	{
		return this.specimenCollection;
	}

	/**
	 * Set SpecimenCollection.
	 * @param specimenCollection of Collection containing specimen.
	 */
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * Get LabelFormat.
	 * @return String.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * Set LabelFormat.
	 * @param labelFormat of String type.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

	/**
	 * Returns whether this specimen requirement is active or disabled.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @see #setActivityStatus(String)
	 */
	@Override
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets activity status of the specimen requirement.
	 * @param activityStatus "Active" if this specimen requirement is not deleted else disabled.
	 * @see #getActivityStatus()
	 */
	@Override
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	@Override
	public Collection<SpecimenEventParameters> getSpecimenEventCollection()
	{
		if(this.collector == null && this.receiver == null && Validator.isEmpty(this.collectionContainer) && Validator.isEmpty(this.collectionProcedure)
				&& Validator.isEmpty(this.receivedQuality))
		{
			return new ArrayList<SpecimenEventParameters>();
		}
		CollectionEventParameters collectionEventParam = new CollectionEventParameters();
		collectionEventParam.setComment(this.getCollectionComments());
		collectionEventParam.setSpecimen(this);
		collectionEventParam.setTimestamp(this.getCollectionTimestamp());
		collectionEventParam.setUser(this.getCollector());
		collectionEventParam.setContainer(this.getCollectionContainer());
		collectionEventParam.setCollectionProcedure(this.getCollectionProcedure());

		ReceivedEventParameters receivedEventParam = new ReceivedEventParameters();
		receivedEventParam.setComment(this.getReceivedComments());
		receivedEventParam.setReceivedQuality(this.getReceivedQuality());
		receivedEventParam.setSpecimen(this);
		receivedEventParam.setTimestamp(this.getReceivedTimestamp());
		receivedEventParam.setUser(this.getReceiver());
		List<SpecimenEventParameters> eventParameters = new ArrayList<SpecimenEventParameters>();
		eventParameters.add(receivedEventParam);
		eventParameters.add(collectionEventParam);
		return eventParameters;
	}

	/**
	 * Sets the collection of Specimen Event Parameters associated with this specimen.
	 * @param specimenEventCollection the collection of Specimen Event Parameters
	 * associated with this specimen.
	 * @see #getSpecimenEventCollection()
	 */
	@Override
	public void setSpecimenEventCollection(final Collection specimenEventCollection)
	{
		for (Object event : specimenEventCollection)
		{
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) event;
			if (specimenEventParameters instanceof CollectionEventParameters)
			{
				this.collectionComments = specimenEventParameters.getComment();
				this.collectionContainer = ((CollectionEventParameters) specimenEventParameters).getContainer();
				this.collectionProcedure = ((CollectionEventParameters) specimenEventParameters).getCollectionProcedure();
				this.collectionTimestamp = specimenEventParameters.getTimestamp();
				this.collector = specimenEventParameters.getUser();
			}
			else if (specimenEventParameters instanceof ReceivedEventParameters)
			{
				this.receivedQuality = ((ReceivedEventParameters) specimenEventParameters).getReceivedQuality();
				this.receivedTimestamp = specimenEventParameters.getTimestamp();
				this.receiver = specimenEventParameters.getUser();
				this.receivedComments = specimenEventParameters.getComment();
			}
		}
	}

	public String getTissueSite() {
		return tissueSite;
	}

	
	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}

	
	public String getTissueSide() {
		return tissueSide;
	}

	
	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}

	/**
	 * Get the pathological status.
	 * @return pathological status in String type.
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * Set the pathological status.
	 * @param pathologicalStatus of type String.
	 */
	public void setPathologicalStatus(final String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * Get the lineage.
	 * @return String.
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * Set the lineage.
	 * @param lineage of type String.
	 */
	public void setLineage(final String lineage)
	{
		this.lineage = lineage;
	}
	
	/**
	 * Get the initial quantity.
	 * @return initial quantity in double.
	 */
	public Double getInitialQuantity()
	{
		return this.initialQuantity;
	}

	/**
	 * Set the initial quantity.
	 * @param initialQuantity which is of Double type.
	 */
	public void setInitialQuantity(final Double initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	/**
	 * Returns the type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @return The type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @see #getType(String)
	 */
	public String getSpecimenType()
	{
		return this.specimenType;
	}

	/**
	 * Set the specimen type.
	 * @param specimenType SpecimenType)
	 */
	public void setSpecimenType(final String specimenType)
	{
		this.specimenType = specimenType;
	}

	/**
	 * Returns the parent specimen from which this specimen is derived.
	 * @hibernate.many-to-one column="PARENT_SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @return the parent specimen from which this specimen is derived.
	 * @see #setParentSpecimen(SpecimenNew)
	 */
	public AbstractSpecimen getParentSpecimen()
	{
		return this.parentSpecimen;
	}

	/**
	 * Sets the parent specimen from which this specimen is derived.
	 * @param parentSpecimen the parent specimen from which this specimen is derived.
	 * @see #getParentSpecimen()
	 */
	public void setParentSpecimen(final AbstractSpecimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	 * Returns the collection of children specimens derived from this specimen.
	 * @hibernate.set name="childrenSpecimen" table="CATISSUE_SPECIMEN"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PARENT_SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the collection of children specimens derived from this specimen.
	 * @see #setChildrenSpecimen(Set)
	 */
	public Collection<AbstractSpecimen> getChildSpecimenCollection()
	{
		return this.childSpecimenCollection;
	}

	/**
	 * Sets the collection of children specimens derived from this specimen.
	 * @param childrenSpecimen the collection of children specimens
	 * derived from this specimen.
	 * @see #getChildrenSpecimen()
	 */
	public void setChildSpecimenCollection(final Collection<AbstractSpecimen> childrenSpecimen)
	{
		this.childSpecimenCollection = childrenSpecimen;
	}

	/**
	 * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
	 * @return String className.
	 */
	public String getClassName()
	{
		
		return this.specimenClass;
	}

	/**
	 * Get the specimen class.
	 * @return String type "Specimen Class".
	 */
	public String getSpecimenClass()
	{
		return this.specimenClass;
	}

	/**
	 * Set the specimen class.
	 * @param specimenClass SpecimenClass.
	 */
	public void setSpecimenClass(final String specimenClass)
	{
		this.specimenClass = specimenClass;
	}
	
	protected Double concentrationInMicrogramPerMicroliter; 
	public Double getConcentrationInMicrogramPerMicroliter() {
		return concentrationInMicrogramPerMicroliter;
	}

	public void setConcentrationInMicrogramPerMicroliter(
			Double concentrationInMicrogramPerMicroliter) {
		this.concentrationInMicrogramPerMicroliter = concentrationInMicrogramPerMicroliter;
	}
}