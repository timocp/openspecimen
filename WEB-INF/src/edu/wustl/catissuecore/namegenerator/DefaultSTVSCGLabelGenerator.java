package edu.wustl.catissuecore.namegenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;


public class DefaultSTVSCGLabelGenerator implements LabelGenerator{

	/**
	 * Current label.
	 */
	protected Long currentLabel;

	/**
	 * Data source Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSTVSCGLabelGenerator() throws ApplicationException
	{
		super();
		this.init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Database Name and then set function name that will convert
	 * label from integer to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		this.currentLabel = 1l;//this.getLastAvailableSCGLabel(null);
	}

//	/**
//	 * @param databaseConstant databaseConstant
//	 * @return noOfRecords
//	 * @throws ApplicationException Application Exception
//	 */
//	private Long getLastAvailableSCGLabel(String databaseConstant) throws ApplicationException
//	{
//		Long noOfRecords = new Long("0");
//		final String sql = "Select max(IDENTIFIER) as MAX_IDENTIFIER from CATISSUE_SPECIMEN_COLL_GROUP";
//		noOfRecords = AppUtility.getLastAvailableValue(sql);
//		return noOfRecords;
//	}

	/**
	 * Set label.
	 * @param obj SCG bar code
	 */
	public void setLabel(Object obj)
	{
		final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) obj;
		final CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
		String ppId = cpr.getProtocolParticipantIdentifier();
		String datePattern = ApplicationProperties.getValue("date.pattern");
		DateFormat df = new SimpleDateFormat(datePattern);
		String coll_date = df.format(scg.getCollectionTimestamp());
		StringBuilder label = new StringBuilder(50);
		label.append(ppId).append("_").append(coll_date);
		scg.setName(label.toString());
	}

	/**
	 * Setting label.
	 * @param objSpecimenList SCG object list
	 */
	public void setLabel(List objSpecimenList)
	{
		//Not required in case of SCG -As only individual SCG will be labeled.
	}

	/**
	 * Returns label for the given domain object.
	 * @return SCG name
	 * @param obj SCG Object
	 */
	public String getLabel(Object obj)
	{
		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		this.setLabel(specimenCollectionGroup);
		return specimenCollectionGroup.getName();
	}

	public void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		// TODO Auto-generated method stub

	}

	public void setLabel(Object object, boolean ignoreCollectedStatus) throws LabelGenException
	{
		// TODO Auto-generated method stub
		
	}

}
