package krishagni.catissueplus.handler;

import krishagni.catissueplus.bizlogic.UserBizlogic;
import krishagni.catissueplus.dto.UserDetails;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;


public class UserHandler {
	
	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenHandler.class);

	public UserDetails createUser(UserDetails details, SessionDataBean sessionDataBean)  throws BizLogicException {
		HibernateDAO hibernateDao = null;
		try {
			hibernateDao = DAOUtil.openDAOSession(sessionDataBean);
			UserBizlogic bizlogic = new UserBizlogic();
			details = bizlogic.insert(details, hibernateDao, sessionDataBean);
			hibernateDao.commit();
		}
		catch (ApplicationException exception) {
			String errMssg = CommonUtil.getErrorMessage(exception, new Specimen(), "Inserting");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(), exception, exception.getMsgValues(), errMssg);

		}
		finally {
			DAOUtil.closeDAOSession(hibernateDao);
		}

		return details;
	}

	public UserDetails update(UserDetails userDetails, SessionDataBean sessionDataBean) throws BizLogicException {
		HibernateDAO hibernateDao = null;
		try {
			hibernateDao = DAOUtil.openDAOSession(sessionDataBean);
			UserBizlogic bizlogic = new UserBizlogic();

			userDetails = bizlogic.update(hibernateDao, userDetails, sessionDataBean);
			hibernateDao.commit();
		}
		catch (ApplicationException exception) {
			String errMssg = CommonUtil.getErrorMessage(exception, new Specimen(), "Updating");
			LOGGER.error(errMssg, exception);
			throw new BizLogicException(exception.getErrorKey(), exception, exception.getMsgValues(), errMssg);

		}
		finally {
			DAOUtil.closeDAOSession(hibernateDao);
		}

		return userDetails;
	}

}
