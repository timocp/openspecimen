/**
 * <p>Title: SpecimenProtocol Class</p>
 * <p>Description:  A set of procedures that govern the collection and/or distribution of biospecimens.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;

import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of procedures that govern the collection and/or distribution of biospecimens.
 * @author mandar_deshmukh
 */
public abstract class SpecimenProtocol extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenProtocol.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */

	//Change for API Search   --- Ashwin 04/10/2006
	
	/*
	 * Return participant protocol id format 
	 * @return participant protocol id format 
	 * */
	public abstract String getPpidFormat();


	/**
	 * Set participant protocol id format
	 * @param participant protocol id format 
	 */
	public abstract void setPpidFormat(String ppidFormat);

	/**
	 * Return the label format
	 * @return the label format
	 */
	public abstract String getSpecimenLabelFormat();

	/**
	 * Set the label format
	 * @param labelFormat
	 */
	public abstract void setSpecimenLabelFormat(String labelFormat);

	public abstract String getDerivativeLabelFormat();

	public abstract void setDerivativeLabelFormat(String derivativeLabelFormat);

	public abstract String getAliquotLabelFormat();

	public abstract void setAliquotLabelFormat(String aliquotLabelFormat);

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public SpecimenProtocol()
	{
		super();
		// Default Constructor, required for Hibernate
	}

	/**
	 * Returns the id of the protocol.
	 * @return Returns the id.
	 */
	@Override
	public abstract Long getId();

	/**
	 * @param identifier The id to set.
	 */
	@Override
	public abstract void setId(Long identifier);

	/**
	 * Returns the principal investigator of the protocol.
	 * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR_ID" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the principal investigator of the protocol.
	 * @see #setPrincipalInvestigator(User)
	 */
	public abstract User getPrincipalInvestigator();

	/**
	 * @param principalInvestigator The principalInvestigator to set.
	 */
	public abstract void setPrincipalInvestigator(User principalInvestigator);

	/**
	 * Returns the title of the protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="255" not-null="true" unique="true"
	 * @return Returns the title.
	 */
	public abstract String getTitle();

	/**
	 * @param title The title to set.
	 */
	public abstract void setTitle(String title);

	/**
	 * Returns the short title of the protocol.
	 * @hibernate.property name="shortTitle" type="string" column="SHORT_TITLE"
	 * length="255"
	 * @return Returns the shortTitle.
	 */
	public abstract String getShortTitle();

	/**
	 * @param shortTitle The shortTitle to set.
	 */
	public abstract void setShortTitle(String shortTitle);

	/**
	 * Returns the irb id of the protocol.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="255"
	 * @return Returns the irbIdentifier.
	 */
	public abstract String getIrbIdentifier();

	/**
	 * @param irbIdentifier The irbIdentifier to set.
	 */
	public abstract void setIrbIdentifier(String irbIdentifier);

	/**
	 * Returns the startdate of the protocol.
	 * @hibernate.property name="startDate" type="date" column="START_DATE" length="50"
	 * @return Returns the startDate.
	 */
	public abstract Date getStartDate();

	/**
	 * @param startDate The startDate to set.
	 */
	public abstract void setStartDate(Date startDate);

	/**
	 * Returns the enddate of the protocol.
	 * @hibernate.property name="endDate" type="date" column="END_DATE" length="50"
	 * @return Returns the endDate.
	 */
	public abstract Date getEndDate();

	/**
	 * @param endDate The endDate to set.
	 */
	public abstract void setEndDate(Date endDate);

	/**
	 * Returns the enrollment.
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return Returns the enrollment.
	 */
	public abstract Integer getEnrollment();

	/**
	 * @param enrollment The enrollment to set.
	 */
	public abstract void setEnrollment(Integer enrollment);

	/**
	 * Returns the descriptionURL.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="255"
	 * @return Returns the descriptionURL.
	 */
	public abstract String getDescriptionURL();

	/**
	 * @param descriptionURL The descriptionURL to set.
	 */
	public abstract void setDescriptionURL(String descriptionURL);

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public abstract String getActivityStatus();

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public abstract void setActivityStatus(String activityStatus);

	/**
	 * Set All Values in Form.
	 * @param abstractForm IValueObject.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		logger.debug("SpecimenProtocol: setAllValues ");
	}
}