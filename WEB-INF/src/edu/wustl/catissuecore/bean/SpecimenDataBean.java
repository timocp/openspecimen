
package edu.wustl.catissuecore.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;

public class SpecimenDataBean implements GenericSpecimen
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -593612220432189911L;
	public String uniqueId;
	protected Long id;

	protected String className;
	/**
	 * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String type;

	/**
	 * Reference to dimensional position one of the specimen in Storage Container.
	 */
	protected String positionDimensionOne = "";

	/**
	 * Reference to dimensional position two of the specimen in Storage Container.
	 */
	protected String positionDimensionTwo = "";

	/**
	 * Barcode assigned to the specimen.
	 */
	protected String barCode;

	/**
	 * Comment on specimen.
	 */
	protected String comment;

	/**
	 * Parent specimen from which this specimen is derived. 
	 */
	protected Specimen parentSpecimen;

	/**
	 * Collection of attributes of a Specimen that renders it potentially harmful to a User.
	 */
	protected Collection<Biohazard> biohazardCollection = new HashSet<Biohazard>();

	/**
	 * Collection of Specimen Event Parameters associated with this specimen. 
	 */
	protected Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();

	/**
	 * Collection of a pre-existing, externally defined id associated with a specimen.
	 */
	protected Collection<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	/**
	 * An event that results in the collection of one or more specimen from a participant.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	protected String pathologicalStatus;
	protected String tissueSite;
	protected String tissueSide;
	/**
	 * A historical information about the specimen i.e. whether the specimen is a new specimen
	 * or a derived specimen or an aliquot.
	 */
	protected String lineage;

	/**
	 * A label name of this specimen.
	 */
	protected String label;

	//Change for API Search   --- Ashwin 04/10/2006
	/**
	 * The quantity of a specimen.
	 */
	protected String initialQuantity;

	protected String concentration;

	public LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection = new LinkedHashMap<String, GenericSpecimen>();
	private String parentName;
	private String storageType;
	private String selectedContainerName;
	private String containerId;
	private boolean checkedSpecimen = true;
	private boolean printSpecimen = false;//janhavi
	private Specimen corresSpecimen;
	private Long collectionProtocolId = -1l;
	private GenericSpecimen formSpecimenVo;
	private boolean showBarcode = true;
	private boolean showLabel = true;

	private boolean readOnly = false;

	public GenericSpecimen getFormSpecimenVo()
	{
		return this.formSpecimenVo;
	}

	public void setFormSpecimenVo(GenericSpecimen formSpecimenVo)
	{
		this.formSpecimenVo = formSpecimenVo;
	}

	/**
	 * @return Returns the biohazardCollection.
	 */
	public Collection<Biohazard> getBiohazardCollection()
	{
		return this.biohazardCollection;
	}

	/**
	 * @param biohazardCollection The biohazardCollection to set.
	 */
	public void setBiohazardCollection(Collection<Biohazard> biohazardCollection)
	{
		this.biohazardCollection = biohazardCollection;
	}

	public Specimen getCorresSpecimen()
	{
		return this.corresSpecimen;
	}

	public void setCorresSpecimen(Specimen corresSpecimen)
	{
		this.corresSpecimen = corresSpecimen;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return Returns the concentration.
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * @param concentration The concentration to set.
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return Returns the externalIdentifierCollection.
	 */
	public Collection<ExternalIdentifier> getExternalIdentifierCollection()
	{
		return this.externalIdentifierCollection;
	}

	/**
	 * @param externalIdentifierCollection The externalIdentifierCollection to set.
	 */
	public void setExternalIdentifierCollection(Collection<ExternalIdentifier> externalIdentifierCollection)
	{
		this.externalIdentifierCollection = externalIdentifierCollection;
	}

	/**
	 * @return Returns the id.
	 */
	public long getId()
	{
		long num = -1;
		if (this.id == null)
		{
			num = -1;
		}
		else
		{
			num = this.id.longValue();
		}
		return num;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return Returns the lineage.
	 */
	public String getLineage()
	{
		return this.lineage;
	}

	/**
	 * @param lineage The lineage to set.
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * @return Returns the parentSpecimen.
	 */
	public Specimen getParentSpecimen()
	{
		return this.parentSpecimen;
	}

	/**
	 * @param parentSpecimen The parentSpecimen to set.
	 */
	public void setParentSpecimen(Specimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}

	/**
	 * @return Returns the pathologicalStatus.
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * @param pathologicalStatus The pathologicalStatus to set.
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * @return Returns the positionDimensionOne.
	 */
	public String getPositionDimensionOne()
	{
		return String.valueOf(this.positionDimensionOne);
	}

	/**
	 * @return Returns the positionDimensionTwo.
	 */
	public String getPositionDimensionTwo()
	{
		return String.valueOf(this.positionDimensionTwo);
	}

	/**
	 * @return Returns the specimenCollectionGroup.
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return this.specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup The specimenCollectionGroup to set.
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * @return Returns the specimenEventCollection.
	 */
	public Collection<SpecimenEventParameters> getSpecimenEventCollection()
	{
		return this.specimenEventCollection;
	}

	/**
	 * @param specimenEventCollection The specimenEventCollection to set.
	 */
	public void setSpecimenEventCollection(Collection<SpecimenEventParameters> specimenEventCollection)
	{
		this.specimenEventCollection = specimenEventCollection;
	}

	/**
	 * @return Returns the tissueSide.
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * @return Returns the tissueSite.
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return Returns the deriveSpecimenCollection.
	 */
	public LinkedHashMap<String, GenericSpecimen> getDeriveSpecimenCollection()
	{
		return this.deriveSpecimenCollection;
	}

	/**
	 * @param deriveSpecimenCollection The deriveSpecimenCollection to set.
	 */
	public void setDeriveSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> deriveSpecimenCollection)
	{
		this.deriveSpecimenCollection = deriveSpecimenCollection;
	}

	/* getters and setter for attributes finish */

	/* overide methods */
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getAliquotSpecimenCollection()
	 */
	public LinkedHashMap<String, GenericSpecimen> getAliquotSpecimenCollection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getDisplayName()
	 */
	public String getDisplayName()
	{
		// TODO Auto-generated method stub
		return this.label;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getParentName()
	 */
	public String getParentName()
	{
		// TODO Auto-generated method stub
		return this.parentName;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getQuantity()
	 */
	public String getQuantity()
	{
		// TODO Auto-generated method stub
		return this.initialQuantity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getStorageContainerForSpecimen()
	 */
	public String getStorageContainerForSpecimen()
	{
		// TODO Auto-generated method stub
		return this.storageType;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bean.GenericSpecimen#getUniqueIdentifier()
	 */
	public String getUniqueIdentifier()
	{
		// TODO Auto-generated method stub
		return this.uniqueId;
	}

	public String getBarCode()
	{
		// TODO Auto-generated method stub
		return this.barCode;
	}

	public boolean getCheckedSpecimen()
	{
		// TODO Auto-generated method stub
		return this.checkedSpecimen;
	}

	public String getContainerId()
	{
		// TODO Auto-generated method stub
		return this.containerId;
	}

	public boolean getReadOnly()
	{
		// TODO Auto-generated method stub
		return this.readOnly;
	}

	public String getSelectedContainerName()
	{
		// TODO Auto-generated method stub
		return this.selectedContainerName;
	}

	public void setAliquotSpecimenCollection(
			LinkedHashMap<String, GenericSpecimen> aliquotSpecimenCollection)
	{
		// TODO Auto-generated method stub

	}

	public void setBarCode(String barCode)
	{
		// TODO Auto-generated method stub
		this.barCode = barCode;
	}

	public void setCheckedSpecimen(boolean checkedSpecimen)
	{
		this.checkedSpecimen = checkedSpecimen;

	}

	public void setContainerId(String containerId)
	{

		this.containerId = containerId;
	}

	public void setDisplayName(String displayName)
	{
		// TODO Auto-generated method stub
		this.label = displayName;
	}

	public void setId(long id)
	{
		// TODO Auto-generated method stub
		this.id = id;
	}

	public void setParentName(String parentName)
	{

		this.parentName = parentName;

	}

	public void setPositionDimensionOne(String positionDimensionOne)
	{

		this.positionDimensionOne = positionDimensionOne;

	}

	public void setPositionDimensionTwo(String positionDimensionTwo)
	{

		this.positionDimensionTwo = positionDimensionTwo;
	}

	public void setQuantity(String quantity)
	{

		this.initialQuantity = quantity;
	}

	public void setReadOnly(boolean readOnly)
	{
		// TODO Auto-generated method stub
		this.readOnly = readOnly;
	}

	public void setSelectedContainerName(String selectedContainerName)
	{

		this.selectedContainerName = selectedContainerName;

	}

	public void setStorageContainerForSpecimen(String storageContainerForSpecimen)
	{

		this.storageType = storageContainerForSpecimen;

	}

	public void setUniqueIdentifier(String uniqueIdentifier)
	{

		this.uniqueId = uniqueIdentifier;
	}

	/**
	 * @return Returns the collectionProtocolId.
	 */
	public Long getCollectionProtocolId()
	{
		return this.collectionProtocolId;
	}

	/**
	 * @param collectionProtocolId The collectionProtocolId to set.
	 */
	public void setCollectionProtocolId(Long collectionProtocolId)
	{
		this.collectionProtocolId = collectionProtocolId;
	}

	public boolean getShowBarcode()
	{
		// TODO Auto-generated method stub
		return this.showBarcode;
	}

	public boolean getShowLabel()
	{
		// TODO Auto-generated method stub
		return this.showLabel;
	}

	public void setShowBarcode(boolean showBarcode)
	{
		this.showBarcode = showBarcode;

	}

	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;

	}

	public boolean getPrintSpecimen()
	{
		// TODO Auto-generated method stub
		return this.printSpecimen;
	}

	public void setPrintSpecimen(boolean printSpecimen)
	{
		this.printSpecimen = printSpecimen;
	}

	/* override methods finish */

}
