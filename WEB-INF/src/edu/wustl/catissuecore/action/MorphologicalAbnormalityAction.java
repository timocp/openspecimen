
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.MorphologicalAbnormalityBizlogic;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;

public class MorphologicalAbnormalityAction extends BaseAction {

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String action = (String) request.getParameter("action");
		String query = request.getParameter("mask") != null ? request.getParameter("mask") : "";
		String pvId = (String) request.getParameter("pvId");

		MorphologicalAbnormalityBizlogic bizLogic = new MorphologicalAbnormalityBizlogic();
		List results = null;
		if (pvId != null) {
			results = bizLogic.getChildsFromParentId(pvId);
			JSONObject obj = new JSONObject();
			Iterator iterator = results.iterator();
			JSONArray outputArray = new JSONArray();
			while (iterator.hasNext()) {
				List row = (List) iterator.next();
				outputArray.put(row);
			}
			obj.put("data", outputArray);
			response.flushBuffer();
			PrintWriter out = response.getWriter();
			out.write(obj.toString());
			return null;

		}
		else if (action != null && action.equals("getRootAbnormalities")) {
			results = bizLogic.getRootNodes();
			String xml = createTreeXml(results).toString();
			response.flushBuffer();
			response.setContentType("text/xml");
			final PrintWriter out = response.getWriter();
			out.write(xml);
			return null;
		}
		else {
			List<NameValueBean> morphoList = new ArrayList<NameValueBean>();
			results = bizLogic.getAllPvs(query);
			Iterator iterator = results.iterator();
			while (iterator.hasNext()) {
				List row = (List) iterator.next();
				morphoList.add(new NameValueBean(row.get(1), row.get(1)));
			}
			response.flushBuffer();
			response.setContentType("text/xml");
			final PrintWriter out = response.getWriter();
			out.write(createXml(morphoList).toString());
			return null;
		}
	}

	private StringBuffer createTreeXml(List results) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><tree id=\"0\">");
		Iterator iterator = results.iterator();
		while (iterator.hasNext()) {
			List row = (List) iterator.next();
			buffer.append("<item text=\"" + StringEscapeUtils.escapeXml((String) row.get(0)) + "\" id=\""
					+ StringEscapeUtils.escapeXml((String) row.get(1)) + "\">");
			if (row.get(2) != null && !row.get(2).toString().isEmpty()) {
				buffer.append("<item></item>");
			}
			buffer.append("</item>");
		}
		buffer.append("</tree>");
		return buffer;
	}

	private StringBuffer createXml(Collection<NameValueBean> clinicalDiagnosisBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\"?>");
		buffer.append("<complete>");
		if (clinicalDiagnosisBean != null) {
			final ListIterator iterator = ((List) clinicalDiagnosisBean).listIterator();
			int count = 0;
			while (iterator.hasNext() && count < 100) {
				final NameValueBean nameValueBean = (NameValueBean) iterator.next();
				if (count == 0) {
					buffer.append("<option value='Not Specified'>Not Specified</option>");
				}
				buffer.append("<option value=\"" + StringEscapeUtils.escapeXml(nameValueBean.getName()) + "\"><![CDATA["
						+ nameValueBean.getValue() + "]]></option>");
				count++;
			}
		}
		buffer.append("</complete>");
		return buffer;
	}
}
