/**
 * <p>Title: AbstractSpecimen Class>
 * <p>Description:  A single unit of tissue, body fluid, or derivative
 *                  biological macromolecule that is collected or created from a Participant </p>
 * Company: Washington University, School of Medicine, St. Louis.
 * @version caTissueSuite V1.1
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.logger.Logger;

/**
 * @hibernate.class table="CATISSUE_ABSTRACT_SPECIMEN"
 * @author virender_mehta
 */

public abstract class AbstractSpecimen extends AbstractDomainObject
		implements
			Serializable,
			IActivityStatus
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(AbstractSpecimen.class);
	/**
	 * This is the serial version ID generated for the class.
	 */
	private static final long serialVersionUID = 156565234567890L;

	/**
	 * Overidden from AbstractDomainObject class.
	 * @param valueObject IValueObject.
	 * @throws AssignDataException assignDataException.
	 */
	@Override
	public void setAllValues(final IValueObject valueObject) throws AssignDataException
	{
		logger.debug("Empty implementation of setAllValue");
	}

	public abstract Double getConcentrationInMicrogramPerMicroliter();

	public abstract void setConcentrationInMicrogramPerMicroliter(
			Double concentrationInMicrogramPerMicroliter);
	/**
	 * It would return the Activity Status.
	 * @return activity status of String type.
	 */
	public String getActivityStatus()
	{
		logger.debug("Empty implementation of getActivityStatus");
		return null;
	}

	/**
	 * Abstract method which would set the Activity Status.
	 * @param activityStatus of String type
	 */
	public void setActivityStatus(final String activityStatus)
	{
		logger.debug("Empty implementation of setActivityStatus");
	}

	/**
	 * It returns the identifier.
	 * @return identifier of type Long.
	 */
	@Override
	public abstract Long getId();

	/**
	 * Set the identifier.
	 * @param identifier which is of Long type.
	 */
	@Override
	public abstract void setId(final Long identifier);

	
	
	public abstract String getTissueSite();

	
	public abstract void setTissueSite(String tissueSite);

	
	public abstract String getTissueSide();

	
	public abstract void setTissueSide(String tissueSide);

	/**
	 * Get the pathological status.
	 * @return pathological status in String type.
	 */
	public abstract String getPathologicalStatus();

	/**
	 * Set the pathological status.
	 * @param pathologicalStatus of type String.
	 */
	public abstract void setPathologicalStatus(String pathologicalStatus);

	/**
	 * Get the lineage.
	 * @return String.
	 */
	public abstract String getLineage();

	/**
	 * Set the lineage.
	 * @param lineage of type String.
	 */
	public abstract void setLineage(final String lineage);

	/**
	 * Get the label.
	 * @return label of String type.
	 */
	public String getLabel()
	{
		return Constants.DOUBLE_QUOTES;
	}

	/**
	 * Get the initial quantity.
	 * @return initial quantity in double.
	 */
	public abstract Double getInitialQuantity();

	/**
	 * Set the initial quantity.
	 * @param initialQuantity which is of Double type.
	 */
	public abstract void setInitialQuantity(final Double initialQuantity);

	/**
	 * Returns the type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @return The type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 * @see #getType(String)
	 */
	public abstract String getSpecimenType();

	/**
	 * Set the specimen type.
	 * @param specimenType SpecimenType)
	 */
	public abstract void setSpecimenType(final String specimenType);

	/**
	 * Returns the parent specimen from which this specimen is derived.
	 * @hibernate.many-to-one column="PARENT_SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @return the parent specimen from which this specimen is derived.
	 * @see #setParentSpecimen(SpecimenNew)
	 */
	public abstract AbstractSpecimen getParentSpecimen();

	/**
	 * Sets the parent specimen from which this specimen is derived.
	 * @param parentSpecimen the parent specimen from which this specimen is derived.
	 * @see #getParentSpecimen()
	 */
	public abstract void setParentSpecimen(final AbstractSpecimen parentSpecimen);

	/**
	 * Returns the collection of children specimens derived from this specimen.
	 * @hibernate.set name="childrenSpecimen" table="CATISSUE_SPECIMEN"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PARENT_SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the collection of children specimens derived from this specimen.
	 * @see #setChildrenSpecimen(Set)
	 */
	public abstract Collection<AbstractSpecimen> getChildSpecimenCollection();

	/**
	 * Sets the collection of children specimens derived from this specimen.
	 * @param childrenSpecimen the collection of children specimens
	 * derived from this specimen.
	 * @see #getChildrenSpecimen()
	 */
	public abstract void setChildSpecimenCollection(final Collection<AbstractSpecimen> childrenSpecimen);

	/**
	 * Returns the collection of Specimen Event Parameters associated with this specimen.
	 * @hibernate.set name="specimenEventCollection" table="CATISSUE_SPECIMEN_EVENT"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenEventParameters"
	 * @return the collection of Specimen Event Parameters associated with this specimen.
	 * @see #setEventCollection(Set)
	 */
	public abstract Collection<SpecimenEventParameters> getSpecimenEventCollection();

	/**
	 * Sets the collection of Specimen Event Parameters associated with this specimen.
	 * @param specimenEventCollection the collection of Specimen Event Parameters
	 * associated with this specimen.
	 * @see #getSpecimenEventCollection()
	 */
	public abstract void setSpecimenEventCollection(final Collection specimenEventCollection);

	/**
	 * This function returns the actual type of the specimen i.e Cell / Fluid / Molecular / Tissue.
	 * @return String className.
	 */
	public abstract String getClassName();

	/**
	 * Get the specimen class.
	 * @return String type "Specimen Class".
	 */
	public abstract String getSpecimenClass();

	/**
	 * Set the specimen class.
	 * @param specimenClass SpecimenClass.
	 */
	public abstract void setSpecimenClass(final String specimenClass);
}
