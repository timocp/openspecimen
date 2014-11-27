package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.dao.SCGDAO;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.dto.ParticipantDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.security.global.Permissions;

public class ParticipantViewAction  extends CatissueBaseAction
{

	public ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		     SessionDataBean sessionData =  (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		     String pageOf = request.getParameter(Constants.PAGE_OF);
		     if(Validator.isEmpty(pageOf))
		     {
		    	 pageOf = (String)request.getAttribute(Constants.PAGE_OF);
		     }
		     String participantId = request.getParameter("id");
		     if(Validator.isEmpty(participantId))
		     {
		    	 participantId = (String)request.getAttribute("id");
		     }
		     String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		     if(Validator.isEmpty(cpId))
    		 {
		    	 cpId = (String)request.getAttribute(Constants.CP_SEARCH_CP_ID);
    		 }
		       HibernateDAO hibernateDao = null;
		      try{ 
		       hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionData);
		       CollectionProtocolRegistrationBizLogic cprbizlogic = new CollectionProtocolRegistrationBizLogic();
		       Long registrationId = cprbizlogic.getRegistrationId(hibernateDao,new Long(participantId),
		    		   new Long(cpId));
		       request.setAttribute("cprId", registrationId);
		       request.setAttribute("cpTitleList",cprbizlogic.getCpTitlelistForParticipant(new Long(participantId),hibernateDao));
//		       SpecimenCollectionGroupBizLogic scgBizlogic = new SpecimenCollectionGroupBizLogic();
		       
		       ColumnValueBean columnValueBean = new ColumnValueBean("id", Long.valueOf(cpId));
						List<CollectionProtocol> collectionProtocols = (List<CollectionProtocol>) hibernateDao
								.retrieve(CollectionProtocol.class.getName(), columnValueBean);
//						collectionProtocol = 
				       //CollectionProtocolRegistration reg = cprbizlogic.getCPRbyCollectionProtocolIDAndParticipantID(hibernateDao, Long.valueOf(cpId), Long.valueOf(participantId));
					
				       List<CollectionProtocol> arms = new ArrayList<CollectionProtocol>();
				       
				       final CollectionProtocol parentCPofArm = collectionProtocols.get(0);
						if (parentCPofArm != null && "Parent".equalsIgnoreCase(parentCPofArm.getType()))
						{
							final List<CollectionProtocol> childCPColl = cprbizlogic.getChildColl(parentCPofArm);
							final Iterator<CollectionProtocol> iteratorofchildCP = childCPColl.iterator();
							while (iteratorofchildCP.hasNext())
							{
								final CollectionProtocol protocol = iteratorofchildCP.next();
								if (protocol != null && "Arm".equalsIgnoreCase(protocol.getType()))
								{
									arms.add(protocol);
								}
								else 
								{
									final List<CollectionProtocol> childCPCollection = cprbizlogic.getChildColl(protocol);
									final Iterator<CollectionProtocol> iteratorofchildCollProt = childCPCollection.iterator();
									while (iteratorofchildCollProt.hasNext())
									{
										final CollectionProtocol childCP = iteratorofchildCollProt.next();
										if (childCP != null && "Arm".equalsIgnoreCase(childCP.getType()))
										{
											arms.add(childCP);
										}
									}
								}
							}
						}
		       SCGDAO scgdao= new SCGDAO();
		       
		       List<NameValueBean> scgLabels = new ArrayList<NameValueBean>();
		       		       List<NameValueBean> eventLabels = new ArrayList<NameValueBean>();
		       		       List<NameValueBean> specimenLabels = new ArrayList<NameValueBean>();
		       		       scgLabels.addAll(scgdao.getSCGNameList(hibernateDao,registrationId));
		       		       eventLabels.addAll(scgdao.getEventLabelsList(hibernateDao,new Long(registrationId)));
		       		       specimenLabels = scgdao.getspecimenLabelsList(hibernateDao,registrationId);
		       		       for(CollectionProtocol collectionProtocol : arms)
		       		       {
		       		    	   CollectionProtocolRegistration armReg = cprbizlogic.getCPRbyCollectionProtocolIDAndParticipantID(hibernateDao, collectionProtocol.getId(), Long.valueOf(participantId));
		       		    	   if(armReg != null)
		       		    	   {
		       		    		   scgLabels.addAll(scgdao.getSCGNameList(hibernateDao,armReg.getId()));
		       		    		   eventLabels.addAll(scgdao.getEventLabelsList(hibernateDao,armReg.getId()));
		       		    		   specimenLabels = scgdao.getspecimenLabelsList(hibernateDao,armReg.getId());
		       		    	   }
		       		       }
		       String scgLabelString = getJsonArrayFromList(scgLabels); 
		       String cpeLabelString = getJsonArrayFromList(eventLabels);
		       String specLabelString = getJsonArrayFromList(specimenLabels);
		       request.setAttribute("specLabelString", specLabelString);
		        request.setAttribute("eventPointLabels",cpeLabelString);
		        request.setAttribute("scgLabels",scgLabelString);
		       	ParticipantBizLogic bizLogic=new ParticipantBizLogic();
				ParticipantDTO participantDTO=bizLogic.getParticipantDTO(hibernateDao,new Long(participantId), 
						new Long(cpId));
				if(! AppUtility.hasPrivilegeToView(CollectionProtocol.class.getName(), new Long(cpId), sessionData, Permissions.REGISTRATION)){
				    participantDTO.setFirstName("");
				    participantDTO.setLastName("");
				    participantDTO.setDob(null);
				    participantDTO.getMrns().clear();
				}
				request.setAttribute("participantDto", participantDTO);
				request.setAttribute("datePattern", CommonServiceLocator.getInstance().getDatePattern());
			}
			  finally
			 {
			    if(hibernateDao!=null)
			    {
				  hibernateDao.closeSession();	
			    }
			 }
 				return mapping.findForward(pageOf);
 			}
	
	private String getJsonArrayFromList(List<NameValueBean> list) throws JSONException
	{
		  JSONArray innerDataArray = new JSONArray();
		  for(NameValueBean bean:list)
		  {
		    JSONObject innerJsonObject = new JSONObject();
			innerJsonObject.put("text",bean.getName());
			innerJsonObject.put("value",bean.getValue());
		    innerDataArray.put(innerJsonObject);
		    innerDataArray.toString();
		  }
		  return innerDataArray.toString();
		
	}

}


	
