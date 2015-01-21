package krishagni.catissueplus.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.util.global.Constants;


public class DisplayScanContainerDetailAction extends CatissueBaseAction{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ipAddress = request.getParameter("ipAddress");
		String contName = request.getParameter("selCont");
		request.setAttribute("ipAddress", ipAddress);
		request.setAttribute("selCont", contName);
		return mapping.findForward(Constants.SUCCESS);
	}
	

}
	