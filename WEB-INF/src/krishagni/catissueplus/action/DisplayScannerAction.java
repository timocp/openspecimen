package krishagni.catissueplus.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.krishagni.catissueplus.core.administrative.events.BoxScannerDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;


public class DisplayScannerAction extends CatissueBaseAction{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<BoxScanner> scannerList = new ArrayList<BoxScanner>();
		String param = request.getParameter("container");
		if(!StringUtils.isEmpty(param)){
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item","Invalid Container Name.");
			actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, actionErrors);
		}
		String hql = "from "+BoxScanner.class.getName();
		List result = AppUtility.executeQuery(hql);
		List<NameValueBean> list = new ArrayList<NameValueBean>();
		for (Object object : result) {
			BoxScanner scanner = (BoxScanner)object;
			NameValueBean bean = new NameValueBean(scanner.getName(),scanner.getId());
			list.add(bean);
//			BoxScannerDetail.fromDomain(scanner);
		}
		request.setAttribute("scannerList", list);
		return mapping.findForward(Constants.SUCCESS);
	}
}
