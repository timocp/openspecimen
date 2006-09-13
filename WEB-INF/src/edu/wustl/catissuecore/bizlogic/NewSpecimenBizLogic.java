/**
 * <p>Title: NewSpecimenHDAO Class>
 * <p>Description:	NewSpecimenBizLogicHDAO is used to add new specimen information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 21, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.QuantityInCount;
import edu.wustl.catissuecore.domain.QuantityInGram;
import edu.wustl.catissuecore.domain.QuantityInMicrogram;
import edu.wustl.catissuecore.domain.QuantityInMilliliter;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.integration.IntegrationManager;
import edu.wustl.catissuecore.integration.IntegrationManagerFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenHDAO is used to add new specimen information into the database using Hibernate.
 * @author aniruddha_phadnis
 */
public class NewSpecimenBizLogic extends IntegrationBizLogic
{

	/**
	 * Saves the storageType object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		try
		{
			Set protectionObjects = new HashSet();
			Specimen specimen = (Specimen) obj;

			setSpecimenAttributes(dao, specimen, sessionDataBean);
			specimen.getStorageContainer();
			dao.insert(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true);
			specimen.setLineage(Constants.NEW_SPECIMEN);
			dao.insert(specimen, sessionDataBean, true, true);
			protectionObjects.add(specimen);

			if (specimen.getSpecimenCharacteristics() != null)
			{
				protectionObjects.add(specimen.getSpecimenCharacteristics());
			}

			Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();

			if (externalIdentifierCollection != null)
			{
				if (externalIdentifierCollection.isEmpty()) //Dummy entry added for query
				{
					ExternalIdentifier exId = new ExternalIdentifier();

					exId.setName(null);
					exId.setValue(null);

					externalIdentifierCollection.add(exId);
				}

				Iterator it = externalIdentifierCollection.iterator();
				while (it.hasNext())
				{
					ExternalIdentifier exId = (ExternalIdentifier) it.next();
					exId.setSpecimen(specimen);
					dao.insert(exId, sessionDataBean, true, true);
				}
			}

			//Mandar : 17-july-06 : autoevents start
			Logger.out.debug("17-july-06: 1 ");
			Collection specimenEventsCollection = specimen.getSpecimenEventCollection();
			Iterator specimenEventsCollectionIterator = specimenEventsCollection.iterator();
			Logger.out
					.debug("specimenEventsCollection.size() : " + specimenEventsCollection.size());
			while (specimenEventsCollectionIterator.hasNext())
			{
				Logger.out.debug("17-july-06: 2 ");
				Object eventObject = specimenEventsCollectionIterator.next();

				if (eventObject instanceof CollectionEventParameters)
				{
					CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
					collectionEventParameters.setSpecimen(specimen);
					Logger.out.debug("17-july-06: 3 ");
					dao.insert(collectionEventParameters, sessionDataBean, true, true);
					Logger.out.debug("17-july-06: 4 ");
				}
				else if (eventObject instanceof ReceivedEventParameters)
				{
					ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
					receivedEventParameters.setSpecimen(specimen);
					Logger.out.debug("17-july-06: 3 ");
					dao.insert(receivedEventParameters, sessionDataBean, true, true);
					Logger.out.debug("17-july-06: 4 ");
				}

			}
			Logger.out.debug("17-july-06: Events Collection inserted");
			//Mandar : 17-july-06 : autoevents end

			//Inserting data for Authorization

			SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,
					protectionObjects, getDynamicGroups(specimen));
		}
		catch (SMException e)
		{
			throw handleSMException(e);
		}
	}

	private String[] getDynamicGroups(AbstractDomainObject obj) throws SMException
	{
		Specimen specimen = (Specimen) obj;
		String[] dynamicGroups = new String[1];

		dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
				specimen.getSpecimenCollectionGroup(), Constants.getCollectionProtocolPGName(null));
		Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]);
		return dynamicGroups;
	}

	private SpecimenCollectionGroup loadSpecimenCollectionGroup(Long specimenID, DAO dao)
			throws DAOException
	{
		//get list of Participant's names
		String sourceObjectName = Specimen.class.getName();
		String[] selectedColumn = {"specimenCollectionGroup." + Constants.SYSTEM_IDENTIFIER};
		String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object whereColumnValue[] = {specimenID};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			Long specimenCollectionGroupId = (Long) list.get(0);
			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
			specimenCollectionGroup.setId(specimenCollectionGroupId);
			return specimenCollectionGroup;
		}
		return null;
	}

	private SpecimenCharacteristics loadSpecimenCharacteristics(Long specimenID, DAO dao)
			throws DAOException
	{
		//get list of Participant's names
		String sourceObjectName = Specimen.class.getName();
		String[] selectedColumn = {"specimenCharacteristics." + Constants.SYSTEM_IDENTIFIER};
		String whereColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		String whereColumnCondition[] = {"="};
		Object whereColumnValue[] = {specimenID};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectedColumn, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);
		if (!list.isEmpty())
		{
			Long specimenCharacteristicsId = (Long) list.get(0);
			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setId(specimenCharacteristicsId);
			return specimenCharacteristics;

			//return (SpecimenCharacteristics)list.get(0);
		}
		return null;
	}

	private void setAvailableQuantity(Specimen obj, Specimen oldObj) throws DAOException
	{
		if (obj instanceof TissueSpecimen)
		{
			Logger.out.debug("In TissueSpecimen");
			TissueSpecimen tissueSpecimenObj = (TissueSpecimen) obj;
			TissueSpecimen tissueSpecimenOldObj = (TissueSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(tissueSpecimenObj.getQuantity().toString());//tissueSpecimenObj.getQuantityInGram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(tissueSpecimenOldObj.getQuantity().toString());//tissueSpecimenOldObj.getQuantityInGram().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(tissueSpecimenOldObj.getAvailableQuantity()
					.toString());//tissueSpecimenOldObj.getAvailableQuantityInGram().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty
						+ "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				tissueSpecimenObj.setAvailableQuantity(new QuantityInGram(newAvailableQty));//tissueSpecimenObj.setAvailableQuantityInGram(new Double(newAvailableQty));
			}

		}
		else if (obj instanceof MolecularSpecimen)
		{
			Logger.out.debug("In MolecularSpecimen");
			MolecularSpecimen molecularSpecimenObj = (MolecularSpecimen) obj;
			MolecularSpecimen molecularSpecimenOldObj = (MolecularSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(molecularSpecimenObj.getQuantity().toString());//molecularSpecimenObj.getQuantityInMicrogram().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(molecularSpecimenOldObj.getQuantity().toString());//molecularSpecimenOldObj.getQuantityInMicrogram().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(molecularSpecimenOldObj
					.getAvailableQuantity().toString());//molecularSpecimenOldObj.getAvailableQuantityInMicrogram().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty
						+ "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				molecularSpecimenObj.setAvailableQuantity(new QuantityInMicrogram(newAvailableQty));//molecularSpecimenObj.setAvailableQuantityInMicrogram(new Double(newAvailableQty));
			}
		}
		else if (obj instanceof CellSpecimen)
		{
			Logger.out.debug("In CellSpecimen");
			CellSpecimen cellSpecimenObj = (CellSpecimen) obj;
			CellSpecimen cellSpecimenOldObj = (CellSpecimen) oldObj;
			// get new qunatity modifed by user
			int newQty = (int) Double.parseDouble(cellSpecimenObj.getQuantity().toString());//cellSpecimenObj.getQuantityInCellCount().intValue();
			// get old qunatity from database
			int oldQty = (int) Double.parseDouble(cellSpecimenOldObj.getQuantity().toString());//cellSpecimenOldObj.getQuantityInCellCount().intValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			int oldAvailableQty = (int) Double.parseDouble(cellSpecimenOldObj
					.getAvailableQuantity().toString());//cellSpecimenOldObj.getAvailableQuantityInCellCount().intValue();

			int distQty = 0;
			int newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty
						+ "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				// set new available quantity
				cellSpecimenObj.setAvailableQuantity(new QuantityInCount(newAvailableQty));//cellSpecimenObj.setAvailableQuantityInCellCount(new Integer(newAvailableQty));
			}
		}
		else if (obj instanceof FluidSpecimen)
		{
			Logger.out.debug("In FluidSpecimen");
			FluidSpecimen fluidSpecimenObj = (FluidSpecimen) obj;
			FluidSpecimen fluidSpecimenOldObj = (FluidSpecimen) oldObj;
			// get new qunatity modifed by user
			double newQty = Double.parseDouble(fluidSpecimenObj.getQuantity().toString());//fluidSpecimenObj.getQuantityInMilliliter().doubleValue();
			// get old qunatity from database
			double oldQty = Double.parseDouble(fluidSpecimenOldObj.getQuantity().toString());//fluidSpecimenOldObj.getQuantityInMilliliter().doubleValue();
			Logger.out.debug("New Qty: " + newQty + " Old Qty: " + oldQty);
			// get Available qty
			double oldAvailableQty = Double.parseDouble(fluidSpecimenOldObj.getAvailableQuantity()
					.toString());//fluidSpecimenOldObj.getAvailableQuantityInMilliliter().doubleValue();

			double distQty = 0;
			double newAvailableQty = 0;
			// Distributed Qty = Old_Qty - Old_Available_Qty
			distQty = oldQty - oldAvailableQty;

			// New_Available_Qty = New_Qty - Distributed_Qty
			newAvailableQty = newQty - distQty;
			Logger.out.debug("Dist Qty: " + distQty + " New Available Qty: " + newAvailableQty);
			if (newAvailableQty < 0)
			{
				throw new DAOException("Newly modified Quantity '" + newQty
						+ "' should not be less than current Distributed Quantity '" + distQty
						+ "'");
			}
			else
			{
				fluidSpecimenObj.setAvailableQuantity(new QuantityInMilliliter(newAvailableQty));//fluidSpecimenObj.setAvailableQuantityInMilliliter(new Double(newAvailableQty));
			}
		}
	}

	/**
	 * Updates the persistent object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 * @throws HibernateException Exception thrown during hibernate operations.
	 */
	public void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws DAOException, UserNotAuthorizedException
	{
		Specimen specimen = (Specimen) obj;
		Specimen specimenOld = (Specimen) oldObj;

		//commented by vaishali - no ore required 
		/*if (isStoragePositionChanged(specimenOld, specimen))
		{
			throw new DAOException(
					"Storage Position should not be changed while updating the specimen");
		}*/

		setAvailableQuantity(specimen, specimenOld);

		if (specimen.isParentChanged())
		{
			//Check whether continer is moved to one of its sub container.
			if (isUnderSubSpecimen(specimen, specimen.getParentSpecimen().getId()))
			{
				throw new DAOException(ApplicationProperties
						.getValue("errors.specimen.under.subspecimen"));
			}
			Logger.out.debug("Loading ParentSpecimen: " + specimen.getParentSpecimen().getId());

			// check for closed ParentSpecimen
			checkStatus(dao, specimen.getParentSpecimen(), "Parent Specimen");

			SpecimenCollectionGroup scg = loadSpecimenCollectionGroup(specimen.getParentSpecimen()
					.getId(), dao);

			specimen.setSpecimenCollectionGroup(scg);
			SpecimenCharacteristics sc = loadSpecimenCharacteristics(specimen.getParentSpecimen()
					.getId(), dao);

			if (!Constants.ALIQUOT.equals(specimen.getLineage()))//specimen instanceof OriginalSpecimen)
			{
				specimen.setSpecimenCharacteristics(sc);
			}
		}
		//check for closed Specimen Collection Group
		if (!specimen.getSpecimenCollectionGroup().getId().equals(
				specimenOld.getSpecimenCollectionGroup().getId()))
			checkStatus(dao, specimen.getSpecimenCollectionGroup(), "Specimen Collection Group");

		setSpecimenGroupForSubSpecimen(specimen, specimen.getSpecimenCollectionGroup(), specimen
				.getSpecimenCharacteristics());

		if (!Constants.ALIQUOT.equals(specimen.getLineage()))//specimen instanceof OriginalSpecimen)
		{
			dao.update(specimen.getSpecimenCharacteristics(), sessionDataBean, true, true, false);
		}

		dao.update(specimen, sessionDataBean, true, false, false);//dao.update(specimen, sessionDataBean, true, true, false);

		//Audit of Specimen.
		dao.audit(obj, oldObj, sessionDataBean, true);

		//Audit of Specimen Characteristics.
		dao.audit(specimen.getSpecimenCharacteristics(), specimenOld.getSpecimenCharacteristics(),
				sessionDataBean, true);

		Collection oldExternalIdentifierCollection = specimenOld.getExternalIdentifierCollection();

		Collection externalIdentifierCollection = specimen.getExternalIdentifierCollection();
		if (externalIdentifierCollection != null)
		{
			Iterator it = externalIdentifierCollection.iterator();
			while (it.hasNext())
			{
				ExternalIdentifier exId = (ExternalIdentifier) it.next();
				exId.setSpecimen(specimen);
				dao.update(exId, sessionDataBean, true, true, false);

				ExternalIdentifier oldExId = (ExternalIdentifier) getCorrespondingOldObject(
						oldExternalIdentifierCollection, exId.getId());
				dao.audit(exId, oldExId, sessionDataBean, true);
			}
		}

		//Disable functionality
		Logger.out.debug("specimen.getActivityStatus() " + specimen.getActivityStatus());
		if (specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
		{
			setDisableToSubSpecimen(specimen);
			Logger.out.debug("specimen.getActivityStatus() " + specimen.getActivityStatus());
			Long specimenIDArr[] = new Long[1];
			specimenIDArr[0] = specimen.getId();

			disableSubSpecimens(dao, specimenIDArr);
		}
	}

	private boolean isUnderSubSpecimen(Specimen specimen, Long parentSpecimenID)
	{
		if (specimen != null)
		{
			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				//Logger.out.debug("SUB CONTINER container "+parentContainerID.longValue()+" "+container.getId().longValue()+"  "+(parentContainerID.longValue()==container.getId().longValue()));
				if (parentSpecimenID.longValue() == childSpecimen.getId().longValue())
					return true;
				if (isUnderSubSpecimen(childSpecimen, parentSpecimenID))
					return true;
			}
		}
		return false;
	}

	private void setSpecimenGroupForSubSpecimen(Specimen specimen,
			SpecimenCollectionGroup specimenCollectionGroup,
			SpecimenCharacteristics specimenCharacteristics)
	{
		if (specimen != null)
		{
			Logger.out.debug("specimen() " + specimen.getId());
			Logger.out.debug("specimen.getChildrenContainerCollection() "
					+ specimen.getChildrenSpecimen().size());

			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
				//((OriginalSpecimen)childSpecimen).setSpecimenCharacteristics(specimenCharacteristics);

				setSpecimenGroupForSubSpecimen(childSpecimen, specimenCollectionGroup,
						specimenCharacteristics);
			}
		}
	}

	//  TODO TO BE REMOVED 
	private void setDisableToSubSpecimen(Specimen specimen)
	{
		if (specimen != null)
		{
			Iterator iterator = specimen.getChildrenSpecimen().iterator();
			while (iterator.hasNext())
			{
				Specimen childSpecimen = (Specimen) iterator.next();
				childSpecimen.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				setDisableToSubSpecimen(childSpecimen);
			}
		}
	}

	private void setSpecimenAttributes(DAO dao, Specimen specimen, SessionDataBean sessionDataBean)
			throws DAOException, SMException
	{
		//Load & set Specimen Collection Group if present
		if (specimen.getSpecimenCollectionGroup() != null)
		{
			Object specimenCollectionGroupObj = dao.retrieve(SpecimenCollectionGroup.class
					.getName(), specimen.getSpecimenCollectionGroup().getId());
			if (specimenCollectionGroupObj != null)
			{
				SpecimenCollectionGroup spg = (SpecimenCollectionGroup) specimenCollectionGroupObj;

				checkStatus(dao, spg, "Specimen Collection Group");

				specimen.setSpecimenCollectionGroup(spg);
			}
		}

		//Load & set Parent Specimen if present
		if (specimen.getParentSpecimen() != null)
		{
			Object parentSpecimenObj = dao.retrieve(Specimen.class.getName(), specimen
					.getParentSpecimen().getId());
			if (parentSpecimenObj != null)
			{
				Specimen parentSpecimen = (Specimen) parentSpecimenObj;

				// check for closed Parent Specimen
				checkStatus(dao, parentSpecimen, "Parent Specimen");

				specimen.setParentSpecimen(parentSpecimen);
			}
		}

		//Load & set Storage Container
		if (specimen.getStorageContainer() != null)
		{
			Object containerObj = dao.retrieve(StorageContainer.class.getName(), specimen
					.getStorageContainer().getId());
			if (containerObj != null)
			{
				StorageContainer container = (StorageContainer) containerObj;
				// check for closed Storage Container
				checkStatus(dao, container, "Storage Container");

				StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic) BizLogicFactory
						.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

				// --- check for all validations on the storage container.
				storageContainerBizLogic.checkContainer(dao, container.getId().toString(), specimen
						.getPositionDimensionOne().toString(), specimen.getPositionDimensionTwo()
						.toString(), sessionDataBean);

				specimen.setStorageContainer(container);
			}
		}

		//Setting the Biohazard Collection
		Set set = new HashSet();
		Collection biohazardCollection = specimen.getBiohazardCollection();
		if (biohazardCollection != null)
		{
			Iterator it = biohazardCollection.iterator();
			while (it.hasNext())
			{
				Biohazard hazard = (Biohazard) it.next();
				Logger.out.debug("hazard.getId() " + hazard.getId());
				Object bioObj = dao.retrieve(Biohazard.class.getName(), hazard.getId());
				if (bioObj != null)
				{
					Biohazard hazardObj = (Biohazard) bioObj;
					set.add(hazardObj);
				}
			}
		}
		specimen.setBiohazardCollection(set);
	}

	public void disableRelatedObjectsForSpecimenCollectionGroup(DAO dao,
			Long specimenCollectionGroupArr[]) throws DAOException
	{
		Logger.out.debug("disableRelatedObjects NewSpecimenBizLogic");
		List listOfSpecimenId = super.disableObjects(dao, Specimen.class,
				"specimenCollectionGroup", "CATISSUE_SPECIMEN", "SPECIMEN_COLLECTION_GROUP_ID",
				specimenCollectionGroupArr);
		if (!listOfSpecimenId.isEmpty())
		{
			disableSubSpecimens(dao, Utility.toLongArray(listOfSpecimenId));
		}
	}

	//    public void disableRelatedObjectsForStorageContainer(DAO dao, Long storageContainerIdArr[])throws DAOException 
	//    {
	//    	Logger.out.debug("disableRelatedObjectsForStorageContainer NewSpecimenBizLogic");
	//    	List listOfSpecimenId = super.disableObjects(dao, Specimen.class, "storageContainer", 
	//    			"CATISSUE_SPECIMEN", "STORAGE_CONTAINER_IDENTIFIER", storageContainerIdArr);
	//    	if(!listOfSpecimenId.isEmpty())
	//    	{
	//    		disableSubSpecimens(dao,Utility.toLongArray(listOfSpecimenId));
	//    	}
	//    }

	private void disableSubSpecimens(DAO dao, Long speIDArr[]) throws DAOException
	{
		List listOfSubElement = super.disableObjects(dao, Specimen.class, "parentSpecimen",
				"CATISSUE_SPECIMEN", "PARENT_SPECIMEN_ID", speIDArr);

		if (listOfSubElement.isEmpty())
			return;
		disableSubSpecimens(dao, Utility.toLongArray(listOfSubElement));
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param longs
	 * @param userId
	 * @throws DAOException
	 * @throws SMException
	 */
	public void assignPrivilegeToRelatedObjectsForSCG(DAO dao, String privilegeName,
			Long[] specimenCollectionGroupArr, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		Logger.out.debug("assignPrivilegeToRelatedObjectsForSCG NewSpecimenBizLogic");
		List listOfSpecimenId = super.getRelatedObjects(dao, Specimen.class,
				"specimenCollectionGroup", specimenCollectionGroupArr);
		if (!listOfSpecimenId.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Specimen.class, Utility
					.toLongArray(listOfSpecimenId), userId, roleId, assignToUser, assignOperation);
			List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class,
					new String[]{"specimenCharacteristics." + Constants.SYSTEM_IDENTIFIER},
					new String[]{Constants.SYSTEM_IDENTIFIER}, Utility
							.toLongArray(listOfSpecimenId));
			super.setPrivilege(dao, privilegeName, Address.class, Utility
					.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
					assignOperation);

			assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, Utility
					.toLongArray(listOfSpecimenId), userId, roleId, assignToUser, assignOperation);
		}
	}

	/**
	 * @param dao
	 * @param privilegeName
	 * @param class1
	 * @param longs
	 * @param userId
	 * @throws DAOException
	 * @throws SMException
	 */
	private void assignPrivilegeToSubSpecimens(DAO dao, String privilegeName, Class class1,
			Long[] speIDArr, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		List listOfSubElement = super.getRelatedObjects(dao, Specimen.class, "parentSpecimen",
				speIDArr);

		if (listOfSubElement.isEmpty())
			return;
		super.setPrivilege(dao, privilegeName, Specimen.class, Utility
				.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
		List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class,
				new String[]{"specimenCharacteristics." + Constants.SYSTEM_IDENTIFIER},
				new String[]{Constants.SYSTEM_IDENTIFIER}, Utility.toLongArray(listOfSubElement));
		super.setPrivilege(dao, privilegeName, Address.class, Utility
				.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, Utility
				.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
	}

	public void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds,
			Long userId, String roleId, boolean assignToUser, boolean assignOperation)
			throws SMException, DAOException
	{
		super.setPrivilege(dao, privilegeName, objectType, objectIds, userId, roleId, assignToUser,
				assignOperation);
		List specimenCharacteristicsIds = super.getRelatedObjects(dao, Specimen.class,
				new String[]{"specimenCharacteristics." + Constants.SYSTEM_IDENTIFIER},
				new String[]{Constants.SYSTEM_IDENTIFIER}, objectIds);
		super.setPrivilege(dao, privilegeName, Address.class, Utility
				.toLongArray(specimenCharacteristicsIds), userId, roleId, assignToUser,
				assignOperation);

		assignPrivilegeToSubSpecimens(dao, privilegeName, Specimen.class, objectIds, userId,
				roleId, assignToUser, assignOperation);
	}

	// validation code here
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#setPrivilege(DAO, String, Class, Long[], Long, String, boolean)
	 * @param dao
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @param roleId
	 * @param assignToUser
	 * @throws SMException
	 * @throws DAOException
	 */
	public void assignPrivilegeToRelatedObjectsForDistributedItem(DAO dao, String privilegeName,
			Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		String[] selectColumnNames = {"specimen.id"};
		String[] whereColumnNames = {"id"};
		List listOfSubElement = super.getRelatedObjects(dao, DistributedItem.class,
				selectColumnNames, whereColumnNames, objectIds);
		if (!listOfSubElement.isEmpty())
		{
			super.setPrivilege(dao, privilegeName, Specimen.class, Utility
					.toLongArray(listOfSubElement), userId, roleId, assignToUser, assignOperation);
		}
	}

	/**
	 * Overriding the parent class's method to validate the enumerated attribute values
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		Specimen specimen = (Specimen) obj;

		if (Constants.ALIQUOT.equals(specimen.getLineage()))
		{
			return true;
		}

		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		String specimenClass = Utility.getSpecimenClassName(specimen);

		if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
		}

		if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimen
				.getType()))
		{
			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
		}

		SpecimenCharacteristics characters = specimen.getSpecimenCharacteristics();

		if (characters == null)
		{
			throw new DAOException(ApplicationProperties
					.getValue("specimen.characteristics.errMsg"));
		}
		else
		{
			if (specimen.getSpecimenCollectionGroup() != null)
			{
				//				NameValueBean undefinedVal = new NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);
				List tissueSiteList = CDEManager.getCDEManager().getPermissibleValueList(
						Constants.CDE_NAME_TISSUE_SITE, null);

				if (!Validator.isEnumeratedValue(tissueSiteList, characters.getTissueSite()))
				{
					throw new DAOException(ApplicationProperties
							.getValue("protocol.tissueSite.errMsg"));
				}

				//		    	NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
				List tissueSideList = CDEManager.getCDEManager().getPermissibleValueList(
						Constants.CDE_NAME_TISSUE_SIDE, null);

				if (!Validator.isEnumeratedValue(tissueSideList, characters.getTissueSide()))
				{
					throw new DAOException(ApplicationProperties
							.getValue("specimen.tissueSide.errMsg"));
				}

				List pathologicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
						Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);

				if (!Validator.isEnumeratedValue(pathologicalStatusList, specimen
						.getPathologicalStatus()))
				{
					throw new DAOException(ApplicationProperties
							.getValue("protocol.pathologyStatus.errMsg"));
				}
			}
		}

		if (operation.equals(Constants.ADD))
		{
			if (!specimen.getAvailable().booleanValue())
			{
				throw new DAOException(ApplicationProperties.getValue("specimen.available.errMsg"));
			}

			if (!Constants.ACTIVITY_STATUS_ACTIVE.equals(specimen.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties
						.getValue("activityStatus.active.errMsg"));
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, specimen
					.getActivityStatus()))
			{
				throw new DAOException(ApplicationProperties.getValue("activityStatus.errMsg"));
			}
		}

		return true;
	}

	/* This function checks whether the storage position of a specimen is changed or not 
	 * & returns the status accordingly.
	 */
	private boolean isStoragePositionChanged(Specimen oldSpecimen, Specimen newSpecimen)
	{
		StorageContainer oldContainer = oldSpecimen.getStorageContainer();
		StorageContainer newContainer = newSpecimen.getStorageContainer();

		if (oldContainer.getId().longValue() == newContainer.getId().longValue())
		{
			if (oldSpecimen.getPositionDimensionOne().intValue() == newSpecimen
					.getPositionDimensionOne().intValue())
			{
				if (oldSpecimen.getPositionDimensionTwo().intValue() == newSpecimen
						.getPositionDimensionTwo().intValue())
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * This method fetches linked data from integrated application i.e. CAE/caTies.
	 */
	public List getLinkedAppData(Long id, String applicationID)
	{
		Logger.out.debug("In getIntegrationData() of SpecimenBizLogic ");

		Logger.out.debug("ApplicationName in getIntegrationData() of SCGBizLogic==>"
				+ applicationID);

		long identifiedPathologyReportId = 0;

		try
		{
			//JDBC call to get matching identifier from database
			Class.forName("org.gjt.mm.mysql.Driver");

			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/catissuecore", "catissue_core", "catissue_core");

			Statement stmt = connection.createStatement();

			String specimenCollectionGroupQuery = "select SPECIMEN_COLLECTION_GROUP_ID from CATISSUE_SPECIMEN where IDENTIFIER="
					+ id;

			ResultSet specimenCollectionGroupResultSet = stmt
					.executeQuery(specimenCollectionGroupQuery);

			long specimenCollectionGroupId = 0;
			while (specimenCollectionGroupResultSet.next())
			{
				specimenCollectionGroupId = specimenCollectionGroupResultSet.getLong(1);
				break;
			}
			Logger.out.debug("SpecimenCollectionGroupId==>" + specimenCollectionGroupId);
			if (specimenCollectionGroupId == 0)
			{
				List exception = new ArrayList();
				exception.add("SpecimenCollectionGroupId is not available for Specimen");
				return exception;
			}

			String clinicalReportQuery = "select CLINICAL_REPORT_ID from CATISSUE_SPECIMEN_COLL_GROUP where IDENTIFIER="
					+ specimenCollectionGroupId;

			ResultSet clinicalReportResultSet = stmt.executeQuery(clinicalReportQuery);

			long clinicalReportId = 0;
			while (clinicalReportResultSet.next())
			{
				clinicalReportId = clinicalReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("ClinicalReportId==>" + clinicalReportId);
			clinicalReportResultSet.close();
			if (clinicalReportId == 0)
			{
				List exception = new ArrayList();
				exception.add("ClinicalReportId is not available for SpecimenCollectionGroup");
				return exception;
			}

			String identifiedPathologyReportIdQuery = "select IDENTIFIER from CATISSUE_IDENTIFIED_PATHOLOGY_REPORT where CLINICAL_REPORT_ID="
					+ clinicalReportId;

			ResultSet identifiedPathologyReportResultSet = stmt
					.executeQuery(identifiedPathologyReportIdQuery);

			while (identifiedPathologyReportResultSet.next())
			{
				identifiedPathologyReportId = identifiedPathologyReportResultSet.getLong(1);
				break;
			}
			Logger.out.debug("IdentifiedPathologyReportId==>" + identifiedPathologyReportId);
			identifiedPathologyReportResultSet.close();
			if (identifiedPathologyReportId == 0)
			{
				List exception = new ArrayList();
				exception
						.add("IdentifiedPathologyReportId is not available for linked ClinicalReportId");
				return exception;
			}

			stmt.close();

			connection.close();
		}
		catch (Exception e)
		{
			Logger.out.debug("JDBC Exception==>" + e.getMessage());
		}

		IntegrationManager integrationManager = IntegrationManagerFactory
				.getIntegrationManager(applicationID);

		return integrationManager.getLinkedAppData(new Specimen(), new Long(
				identifiedPathologyReportId));
	}

	public String getPageToShow()
	{
		return new String();
	}

	public List getMatchingObjects()
	{
		return new ArrayList();
	}
}