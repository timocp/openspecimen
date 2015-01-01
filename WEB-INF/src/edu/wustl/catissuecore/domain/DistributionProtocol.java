/**
 * <p>Title: DistributionProtocol Class</p>
 * <p>Description: An abbreviated set of written procedures that describe how a
 * previously collected specimen will be utilized.  Note that specimen may be
 * collected with one collection protocol and then later utilized by multiple
 * different studies (Distribution protocol).</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * An abbreviated set of written procedures that describe how a previously collected
 * specimen will be utilized.  Note that specimen may be collected with one collection
 * protocol and then later utilized by multiple different studies (Distribution protocol).
 * @hibernate.joined-subclass table="CATISSUE_DISTRIBUTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Mandar Deshmukh
 */
public class DistributionProtocol extends SpecimenProtocol
		implements
			java.io.Serializable,
			IActivityStatus
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DistributionProtocol.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Collection of specimenRequirements associated with the DistributionProtocol.
	 */
	protected Collection<DistributionSpecimenRequirement> distributionSpecimenRequirementCollection = new HashSet<DistributionSpecimenRequirement>();

	/**
	 * Collection of protocols(CollectionProtocols) associated with the DistributionProtocol.
	 */
	protected Collection collectionProtocolCollection = new HashSet();

	/**
	 * Default Constructor.
	 */
	public DistributionProtocol()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public DistributionProtocol(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Returns the collection of SpecimenRequirements for this Protocol.
	 * @hibernate.set name="specimenRequirementCollection" table="CATISSUE_DISTRIBUTION_SPEC_REQ"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.
	 * SpecimenRequirement" column="DISTRIBUTION_SPECIMEN_REQ_ID"
	 * @return Returns the collection of SpecimenRequirements for this Protocol.
	 */
	public Collection<DistributionSpecimenRequirement> getDistributionSpecimenRequirementCollection()
	{
		return this.distributionSpecimenRequirementCollection;
	}

	/**
	 * @param distributionSpecimenRequirementCollection - Collection of
	 * DistributionSpecimenRequirement.
	 * The specimenRequirementCollection to set.
	 */
	public void setDistributionSpecimenRequirementCollection(
			Collection<DistributionSpecimenRequirement> distributionSpecimenRequirementCollection)
	{
		this.distributionSpecimenRequirementCollection = distributionSpecimenRequirementCollection;
	}

	/**
	 * Returns the collection of Collectionprotocols for this DistributionProtocol.
	 * @hibernate.set name="collectionProtocolCollection"
	 * table="CATISSUE_COLL_DISTRIBUTION_REL"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.
	 * CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
	 * @return Returns the collection of Collectionprotocols for this DistributionProtocol.
	 */
	public Collection getCollectionProtocolCollection()
	{
		return this.collectionProtocolCollection;
	}

	/**
	 * @param protocolCollection Collection.
	 * The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolCollection(Collection protocolCollection)
	{
		this.collectionProtocolCollection = protocolCollection;
	}

	/**
	* This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
	* @param abstractForm An CollectionProtocolForm object containing the
	* information about the CollectionProtocol.
	* @throws AssignDataException : AssignDataException
	* */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			super.setAllValues(abstractForm);

			if (SearchUtil.isNullobject(this.principalInvestigator))
			{
				this.principalInvestigator = new User();
			}

			final SpecimenProtocolForm spForm = (SpecimenProtocolForm) abstractForm;

			this.title = spForm.getTitle();
			this.shortTitle = spForm.getShortTitle();
			this.irbIdentifier = spForm.getIrbID();
//			this.generateLabel=spForm.isGenerateLabel();
			this.specimenLabelFormat= spForm.getSpecimenLabelFormat();
			this.startDate = CommonUtilities.parseDate(spForm.getStartDate(), CommonUtilities
					.datePattern(spForm.getStartDate()));
			this.endDate = CommonUtilities.parseDate(spForm.getEndDate(), CommonUtilities
					.datePattern(spForm.getEndDate()));

			if (spForm.getEnrollment() != null && spForm.getEnrollment().trim().length() > 0)
			{
				this.enrollment = Integer.valueOf(spForm.getEnrollment());
			}

			this.descriptionURL = spForm.getDescriptionURL();
			this.activityStatus = spForm.getActivityStatus();

			this.principalInvestigator = new User();
			this.principalInvestigator.setId(Long.valueOf(spForm.getPrincipalInvestigatorId()));
			this.setPpidFormat(spForm.getPpidFormat());
			final DistributionProtocolForm dpForm = (DistributionProtocolForm) abstractForm;

			final Map map = dpForm.getValues();
			//map = fixMap(map);
			logger.debug("MAP " + map);
			final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
			this.distributionSpecimenRequirementCollection = new HashSet(parser.generateData(map));
			logger.debug("specimenRequirementCollection "
					+ this.distributionSpecimenRequirementCollection);
		}
		catch (final Exception excp)
		{
			DistributionProtocol.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "DisposalEventParameters.java :");
		}
	}

	/**
	 * To String.
	 * @return String.
	 */
	@Override
	public String toString()
	{
		return this.title + " " + this.distributionSpecimenRequirementCollection;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	@Override
	public String getMessageLabel()
	{
		return this.title;
	}
	
	protected User principalInvestigator;

	/**
	 * Full title assigned to the protocol.
	 */
	protected String title;

	/**
	 * Abbreviated title assigned to the protocol.
	 */
	protected String shortTitle;

	/**
	 * IRB approval number.
	 */
	protected String irbIdentifier;

	/**
	 * Date on which the protocol is activated.
	 */
	protected Date startDate;

	/**
	 * Date on which the protocol is marked as closed.
	 */
	protected Date endDate;

	/**
	 * Number of anticipated cases need for the protocol.
	 */
	protected Integer enrollment;

	/**
	 * URL to the document that describes detailed information for the biospecimen protocol.
	 */
	protected String descriptionURL;

	/**
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not
	 * queried (Inactive) by any actor.
	 */
	protected String activityStatus;


	/**
	 * Label format for specimens associated with this CP.
	 */
	protected String specimenLabelFormat;

	protected String derivativeLabelFormat;

	protected String aliquotLabelFormat;
	
	/*
	 * Label format for participant protocol id
	 * */
	protected String ppidFormat;
	
	/*
	 * Return participant protocol id format 
	 * @return participant protocol id format 
	 * */
	public String getPpidFormat() {
		return ppidFormat;
	}


	/**
	 * Set participant protocol id format
	 * @param participant protocol id format 
	 */
	public void setPpidFormat(String ppidFormat) {
		this.ppidFormat = ppidFormat;
	}



	/**
	 * Return the label format
	 * @return the label format
	 */
	public String getSpecimenLabelFormat()
	{
		return specimenLabelFormat;
	}



	/**
	 * Set the label format
	 * @param labelFormat
	 */
	public void setSpecimenLabelFormat(String labelFormat)
	{
		this.specimenLabelFormat = labelFormat;
	}



	public String getDerivativeLabelFormat()
	{
		return derivativeLabelFormat;
	}




	public void setDerivativeLabelFormat(String derivativeLabelFormat)
	{
		this.derivativeLabelFormat = derivativeLabelFormat;
	}




	public String getAliquotLabelFormat()
	{
		return aliquotLabelFormat;
	}




	public void setAliquotLabelFormat(String aliquotLabelFormat)
	{
		this.aliquotLabelFormat = aliquotLabelFormat;
	}
	
	public User getPrincipalInvestigator()
	{
		return this.principalInvestigator;
	}

	/**
	 * @param principalInvestigator The principalInvestigator to set.
	 */
	public void setPrincipalInvestigator(User principalInvestigator)
	{
		this.principalInvestigator = principalInvestigator;
	}

	/**
	 * Returns the title of the protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="255" not-null="true" unique="true"
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the short title of the protocol.
	 * @hibernate.property name="shortTitle" type="string" column="SHORT_TITLE"
	 * length="255"
	 * @return Returns the shortTitle.
	 */
	public String getShortTitle()
	{
		return this.shortTitle;
	}

	/**
	 * @param shortTitle The shortTitle to set.
	 */
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}

	/**
	 * Returns the irb id of the protocol.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="255"
	 * @return Returns the irbIdentifier.
	 */
	public String getIrbIdentifier()
	{
		return this.irbIdentifier;
	}

	/**
	 * @param irbIdentifier The irbIdentifier to set.
	 */
	public void setIrbIdentifier(String irbIdentifier)
	{
		this.irbIdentifier = irbIdentifier;
	}

	/**
	 * Returns the startdate of the protocol.
	 * @hibernate.property name="startDate" type="date" column="START_DATE" length="50"
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return this.startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Returns the enddate of the protocol.
	 * @hibernate.property name="endDate" type="date" column="END_DATE" length="50"
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return this.endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Returns the enrollment.
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return Returns the enrollment.
	 */
	public Integer getEnrollment()
	{
		return this.enrollment;
	}

	/**
	 * @param enrollment The enrollment to set.
	 */
	public void setEnrollment(Integer enrollment)
	{
		this.enrollment = enrollment;
	}

	/**
	 * Returns the descriptionURL.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="255"
	 * @return Returns the descriptionURL.
	 */
	public String getDescriptionURL()
	{
		return this.descriptionURL;
	}

	/**
	 * @param descriptionURL The descriptionURL to set.
	 */
	public void setDescriptionURL(String descriptionURL)
	{
		this.descriptionURL = descriptionURL;
	}

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}


}