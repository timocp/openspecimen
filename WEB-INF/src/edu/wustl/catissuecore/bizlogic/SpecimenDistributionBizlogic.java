
package edu.wustl.catissuecore.bizlogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dao.SpecimenDAO;
import edu.wustl.catissuecore.dao.SiteDAO;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenDistribution;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.OrderItemSubmissionDTO;
import edu.wustl.catissuecore.dto.OrderStatusDTO;
import edu.wustl.catissuecore.dto.OrderSubmissionDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class SpecimenDistributionBizlogic extends CatissueDefaultBizLogic {

	/**
	 * Logger object.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ParticipantBizLogic.class);

	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException {
		SpecimenDistribution distribution = (SpecimenDistribution) obj;
		if (Validator.isEmpty(distribution.getDistributionTitle())) {
			throw this.getBizLogicException(null, "errors.item.required", "distribution title");
		}
		validateSpecimen(distribution.getSpecimen(), (HibernateDAO) dao);
		validateDistributionProtocol(distribution.getDistributionProtocol(), dao);
		validateUser(distribution.getRequestor(), dao);
		validateSite(distribution.getSite(), dao);
		return true;
	}

	private void validateSite(Site site, DAO dao) throws BizLogicException {
		if (Validator.isEmpty(site.getName())) {
			throw this.getBizLogicException(null, "errors.item.required", "Site");
		}
	}

	private void validateUser(User requestor, DAO dao) throws BizLogicException {
		if (Validator.isEmpty(requestor.getLoginName())) {
			throw this.getBizLogicException(null, "errors.item.required", "User login name");
		}

	}

	private void validateDistributionProtocol(DistributionProtocol distributionProtocol, DAO dao)
			throws BizLogicException {
		if (Validator.isEmpty(distributionProtocol.getTitle())) {
			throw this.getBizLogicException(null, "errors.distributionprotocol.required", "");
		}
	}

	private void validateSpecimen(Specimen specimen, HibernateDAO dao) throws BizLogicException {
		if (Validator.isEmpty(specimen.getLabel()) && Validator.isEmpty(specimen.getBarcode())) {
			throw this.getBizLogicException(null, "errors.item.required", "Specimen label or barcode");
		}
	}

	protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException {
		try {
			SpecimenDistribution spDistri = (SpecimenDistribution) obj;
			Specimen origSpecimen = getSpecimen(spDistri.getSpecimen().getLabel(), spDistri.getSpecimen().getBarcode(),
					(HibernateDAO) dao);
			Site site = getSite(spDistri.getSite(), (HibernateDAO) dao);

			DistributionProtocol origDp = getDistributionProtocol(spDistri.getDistributionProtocol().getTitle(),
					(HibernateDAO) dao);
			User requestedBy = getUser(spDistri.getRequestor(), (HibernateDAO) dao);
			checkPrivileges(requestedBy, origSpecimen);
			OrderDetails orderDetails = getOrderDetails(spDistri.getDistributionTitle(), (HibernateDAO) dao);
			ExistingSpecimenOrderItem item = createItem(origSpecimen, spDistri);

			if (orderDetails == null) {
				orderDetails = new OrderDetails();
				orderDetails.setComment(spDistri.getComments());
				orderDetails.setRequestedBy(requestedBy);
				orderDetails.setDistributionProtocol(origDp);
				orderDetails.setName(spDistri.getDistributionTitle());
				orderDetails.setRequestedDate(spDistri.getSentDate());
				orderDetails.setStatus(spDistri.getStatus());
				orderDetails.setDistributorsComment(spDistri.getComments());
				//				Collection<OrderItem> items = new HashSet<OrderItem>();
				item.setOrderDetails(orderDetails);
				//				items.add(item);
				orderDetails.getOrderItemCollection().add(item);
				dao.insert(orderDetails);
			}
			else {
				item.setOrderDetails(orderDetails);
				dao.insert(item);
				//				Collection<OrderItem> items = new HashSet<OrderItem>();
				//				items.add(item);
				orderDetails.getOrderItemCollection().add(item);
			}
			OrderSubmissionDTO submissionDTO = new OrderSubmissionDTO();
			submissionDTO.setComments(orderDetails.getComment());
			submissionDTO.setDisptributionProtocolId(origDp.getId());
			submissionDTO.setDisptributionProtocolName(origDp.getTitle());
			submissionDTO.setDistributorsComment(spDistri.getComments());
			submissionDTO.setOrderName(orderDetails.getName());
			if (spDistri.getSentDate() == null) {
				submissionDTO.setRequestedDate(new Date());
			}
			else {
				submissionDTO.setRequestedDate(spDistri.getSentDate());
			}
			submissionDTO.setRequestorId(requestedBy.getId());
			submissionDTO.setId(orderDetails.getId());
			submissionDTO.setSite(site.getId());
			Collection<OrderItemSubmissionDTO> itemDtoList = new HashSet<OrderItemSubmissionDTO>();
			OrderItemSubmissionDTO itemDto = new OrderItemSubmissionDTO();
			//				for (Object item : orderDetails.getOrderItemCollection()) {
			ExistingSpecimenOrderItem spItem = (ExistingSpecimenOrderItem) item;
			itemDto.setComments(spItem.getDescription());
			itemDto.setDistQty(spItem.getDistribtedQuantity());
			itemDto.setOrderitemId(spItem.getId());
			itemDto.setRequestedQty(spItem.getRequestedQuantity());
			itemDto.setSpecimenId(spItem.getSpecimen().getId());
			itemDto.setSpecimenLabel(spItem.getSpecimen().getLabel());
			itemDto.setStatus(spDistri.getStatus());
			itemDtoList.add(itemDto);
			//				}
			submissionDTO.setOrderItemSubmissionDTOs(itemDtoList);
			OrderBizLogic bizLogic = new OrderBizLogic();
			OrderStatusDTO response = bizLogic.updateOrder(submissionDTO, requestedBy.getId(), (HibernateDAO) dao, true);
			if (response.getOrderErrorDTOs() != null && response.getOrderErrorDTOs().size() > 0) {
				LOGGER.error("Error while distributing specimen.");
				throw this.getBizLogicException(null, "error.specimenDistribution", "");
			}

		}
		catch (DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	private ExistingSpecimenOrderItem createItem(Specimen origSpecimen, SpecimenDistribution spDistri) {
		ExistingSpecimenOrderItem item = new ExistingSpecimenOrderItem();
		item.setSpecimen(origSpecimen);
		item.setStatus(spDistri.getStatus());
		item.setDescription(spDistri.getComments());
		item.setDistribtedQuantity(spDistri.getQuantity());
		item.setRequestedQuantity(spDistri.getQuantity());
		return item;
	}

	private void checkPrivileges(User user, Specimen specimen) throws BizLogicException {
		try {
			OrderBizLogic bizLogic = new OrderBizLogic();
			if (!bizLogic.isSuperAdmin(user)) {

				PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
				PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());

				List siteIdsList = bizLogic.getUserSitesWithDistributionPrev(user, privilegeCache);
				if (hasDistributionPrivOnSite(specimen, siteIdsList) || hasDistributionPrivOnCp(user, privilegeCache, specimen)) {

				}
			}
		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
	}

	private boolean hasDistributionPrivOnCp(User user, PrivilegeCache privilegeCache, Specimen specimen)
			throws BizLogicException {
		try {
			SpecimenCollectionGroup specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
			final Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId();
			String objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cpId;
			boolean isAuthorized = privilegeCache.hasPrivilege(objectId,
					Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
			final SessionDataBean sdb = new SessionDataBean();
			sdb.setUserId(user.getId());
			sdb.setUserName(user.getLoginName());
			if (!isAuthorized) {
				isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(Permissions.DISTRIBUTION, sdb, cpId.toString());
			}
		}
		catch (final SMException e) {
			LOGGER.error(e.getMessage(), e);
			throw AppUtility.handleSMException(e);
		}
		return false;
	}

	private boolean hasDistributionPrivOnSite(Specimen specimen, List siteIdsList) {
		SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
		if (specimenPosition != null) {
			if (siteIdsList.contains(specimenPosition.getStorageContainer().getSite().getId())) {
				return true;
			}
		}
		return false;
	}

	private Site getSite(Site site, HibernateDAO dao) throws BizLogicException {
		SiteDAO siteDAO = new SiteDAO();
		Long siteId;
		siteId = siteDAO.getIdBySiteName(dao, site.getName());
		site.setId(siteId);
		return site;
	}

	private User getUser(User user, HibernateDAO dao) throws BizLogicException {
		UserDAO userDAO = new UserDAO();
		try {
			user = userDAO.getUserFromLoginName(dao, user.getLoginName());
		}
		catch (DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return user;
	}

	private OrderDetails getOrderDetails(String orderName, HibernateDAO dao) throws BizLogicException {
		try {
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.STRING, orderName));
			List<OrderDetails> result = dao.executeNamedQuery("getOrder", params);
			if (result != null && result.size() > 0) {
				return result.get(0);
			}
		}
		catch (DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return null;
	}

	private DistributionProtocol getDistributionProtocol(String title, HibernateDAO dao) throws BizLogicException {
		try {
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.STRING, title));
			List<DistributionProtocol> dpList = dao.executeNamedQuery("getDpByTitle", params);
			if (dpList != null && dpList.size() > 0) {
				return dpList.get(0);
			}
			else {
				LOGGER.error("Error: Distribution Protocol object not found with the given title.");
				throw this.getBizLogicException(null, "errors.distributionprotocol.required", "");
			}
		}
		catch (DAOException daoExp) {
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	private Specimen getSpecimen(String label, String barcode, HibernateDAO dao) throws BizLogicException {
		SpecimenDAO specimenDAO = new SpecimenDAO();
		Specimen originalSpecimen = specimenDAO.getSpecimenByLabelOrBarcode(label, barcode, dao);
		if (originalSpecimen == null) {
			throw this.getBizLogicException(null, "errors.distribution.specimenNotFound", "");
		}
		return originalSpecimen;
	}

}
