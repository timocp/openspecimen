
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * An abstract base class for specimen collection group
 * and requirement group.
 * @hibernate.class table="CATISSUE_ABS_SPECI_COLL_GROUP"
 * @author abhijit_naik
 */
public abstract class AbstractSpecimenCollectionGroup extends AbstractDomainObject
		implements
			Serializable,
			IActivityStatus
{

	/**
	 * It is serial version ID for the class.
	 */
	private static final long serialVersionUID = -8771880333931001152L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger.getCommonLogger(AbstractSpecimenCollectionGroup.class);

	
	/**
	 * An abstract function should be overridden by the child
	 * classes.
	 * @return name of the Specimen collection group
	 */
	public abstract String getGroupName();

	/**
	 * Get Collection Protocol Registration.
	 * @return CollectionProtocolRegistration object.
	 */
	public abstract CollectionProtocolRegistration getCollectionProtocolRegistration();

	/**
	 * An abstract function should be overridden by the child
	 * classes.
	 * @param name Specimen collection group.
	 * @throws BizLogicException bizLogicException.
	 */
	protected abstract void setGroupName(String name) throws BizLogicException;

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_COLL_GRP_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 */
	public abstract Long getId();
	

	/**
	 * Method overidden from its extended class.
	 * @param valueObject IValueObject.
	 * @throws AssignDataException assignDataException.
	 */
	@Override
	public void setAllValues(final IValueObject valueObject) throws AssignDataException
	{
		logger.debug("Inside setAllValues");
		
		

	}
}