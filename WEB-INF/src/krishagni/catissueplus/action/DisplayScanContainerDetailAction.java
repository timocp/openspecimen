
package krishagni.catissueplus.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.dao.StorageContainerDAO;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.HibernateDAO;

public class DisplayScanContainerDetailAction extends CatissueBaseAction {

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String scannerName = request.getParameter("scannerName");
			String contName = request.getParameter("containerName");
			HibernateDAO dao = (HibernateDAO) AppUtility.openDAOSession(null);
			StorageContainerDAO contDao = new StorageContainerDAO();
			Long contId = contDao.getContainerId(dao, contName);
			String scannerHql = "select ipAddress from " + BoxScanner.class.getName() + " scan where scan.name='"
					+ scannerName + "'";
			List result = dao.executeQuery(scannerHql);
			String ipAddress = "";
			if (result != null && !result.isEmpty()) {
				ipAddress = String.valueOf(result.get(0));
			}

			request.setAttribute("ipAddress", ipAddress);
			request.setAttribute("selCont", contName);
		}
		catch (BizLogicException e) {

			return mapping.findForward(Constants.FAILURE);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

}
