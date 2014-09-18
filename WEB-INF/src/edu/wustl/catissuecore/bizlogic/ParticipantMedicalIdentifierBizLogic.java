/**
 * <p>Title: ParticipantMedicalIdentifierBizLogic Class>
 * <p>Description:	ParticipantMedicalIdentifierBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana Thakur
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.common.util.Operation;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public class ParticipantMedicalIdentifierBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * Logger object.
	 */
	private static final Logger logger = Logger.getCommonLogger(ParticipantBizLogic.class);
	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		//throw new DAOException(ApplicationProperties.getValue("participant.medical.identifier.creation.error"));
		final ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) obj;
		final Site site = participantMedicalIdentifier.getSite();
		final Participant participant = participantMedicalIdentifier.getParticipant();
		final String medicalRecordNumber = participantMedicalIdentifier.getMedicalRecordNumber();
		if (site == null || site.getName() == null)
		{
			throw this.getBizLogicException(null, "errors.participant.extiden.missing", "");
		}
		if (participant == null)
		{
			throw this.getBizLogicException(null, "participant.medical.identifier.creation.error",
					"");
		}
		if (Validator.isEmpty(medicalRecordNumber))
		{
			throw this.getBizLogicException(null, "errors.participant.extiden.missing", "");
		}
		return true;
	}

	protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) obj;
			Participant participant = updatePmi(pmi,dao);
			ParticipantBizLogic bizlogic = new ParticipantBizLogic();
			if(!bizlogic.isAuthorized(dao, participant, sessionDataBean)){
				throw AppUtility.getUserNotAuthorizedException("Registration", "CollectionProtocol",
						Participant.class.getSimpleName());
			}
			
		}
		catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}

	private Participant updatePmi(ParticipantMedicalIdentifier pmi, DAO dao) throws DAOException, BizLogicException {
		String siteName = pmi.getSite().getName();
		CollectionProtocolRegistration cpr = pmi.getParticipant().getCollectionProtocolRegistrationCollection().iterator().next();
		String ppid = cpr.getProtocolParticipantIdentifier();
		String cpTitle = cpr.getCollectionProtocol().getTitle();
		String siteHql = "from "+Site.class.getName()+" s where s.name = '"+siteName+"' and s.activityStatus != 'Disabled'";
		List result = dao.executeQuery(siteHql);
		if(result.isEmpty()){
			throw this.getBizLogicException(null, "participant.medical.identifier.creation.error",
					"");
		}
		Site site = (Site)result.get(0);
		String participantHql = "select cpr.participant.id from "+CollectionProtocolRegistration.class.getName()+ " cpr where "
				+ " cpr.protocolParticipantIdentifier = '"+ ppid +"' and cpr.collectionProtocol.title = '"+cpTitle
						+ "' and cpr.activityStatus != 'Disabled'";
		List partList = dao.executeQuery(participantHql);
		if(partList.isEmpty()){
			throw this.getBizLogicException(null, "participant.medical.identifier.creation.error",
					"");
		}
		Long pId = Long.valueOf(partList.get(0).toString());
		Participant participant = new Participant();
		participant.setId(pId);
		pmi.setSite(site);
		pmi.setParticipant(participant);
		dao.insert(pmi);
		return participant;
	}

	private Participant populateParticipant(ParticipantMedicalIdentifier pmi) {
		return null;
	}
}
