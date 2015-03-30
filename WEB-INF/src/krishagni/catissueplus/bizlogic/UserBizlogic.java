package krishagni.catissueplus.bizlogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import krishagni.catissueplus.dto.UserDetails;
import krishagni.catissueplus.dto.UserRolePrivBean;
import krishagni.catissueplus.util.CommonUtil;
import krishagni.catissueplus.util.DAOUtil;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.dao.SiteDAO;
import edu.wustl.catissuecore.dao.UserDAO;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;


public class UserBizlogic {

	private static final Logger LOGGER = Logger.getCommonLogger(UserBizlogic.class);
	public UserDetails insert(UserDetails details, HibernateDAO hibernateDao, SessionDataBean sessionDataBean) throws BizLogicException {
		UserDTO userDto = new UserDTO();
			User user = new User();
			userDto = populateUser(details,user,hibernateDao);
		
		UserBizLogic bizLogic = new UserBizLogic();
		bizLogic.insert(userDto,sessionDataBean);
		return UserDetails.fromDomain(userDto);
	}
	
	public UserDetails update(UserDetails userDetails, SessionDataBean sessionDataBean)throws BizLogicException {
		UserDAO dao = new UserDAO();
		UserDTO userDto = new UserDTO();
		HibernateDAO hDao = null;
		User oldUser = null;
		try {
			hDao = DAOUtil.openDAOSession(sessionDataBean);
			oldUser = dao.getUserById(hDao, userDetails.getId());
			userDto = populateUser(userDetails, oldUser, hDao);
			
		}finally{
				try {
					AppUtility.closeDAOSession(hDao);
				}
				catch (ApplicationException e) {
					LOGGER.error(e);
					String msg = CommonUtil.getErrorMessage(e, new User(), "Updating");
					throw new BizLogicException(e.getErrorKey(), e, msg);
				}
		}
		UserBizLogic bizLogic = new UserBizLogic();
		bizLogic.update(userDto, oldUser, sessionDataBean);
		
		return UserDetails.fromDomain(userDto);
	}

	private UserDTO populateUser(UserDetails details,User user, HibernateDAO hibernateDao) throws BizLogicException {
		UserDTO dto = new UserDTO();
//		User user = new User();
		user.setId(details.getId());
		user.setActivityStatus(details.getActivityStatus());
		Address address = null;
		if(user.getId() != null){
			address = user.getAddress();
		}
		else{
			address = new Address();
		}
		
		address.setCity(details.getCity());
		address.setCountry(details.getCountry());
		address.setFaxNumber(details.getFaxNumber());
		address.setPhoneNumber(details.getPhoneNumber());
		address.setState(details.getState());
		address.setStreet(details.getStreet());
		address.setZipCode(details.getZipCode());
		user.setAddress(address);

		CancerResearchGroup crg = getCrg(details.getCrgName(), hibernateDao);
		Department dept = getDept(details.getDeptName(), hibernateDao);
		Institution inst = getInst(details.getInstName(), hibernateDao);
		user.setCancerResearchGroup(crg);
		user.setDepartment(dept);
		user.setInstitution(inst);
		user.setComments(details.getComments());
		user.setEmailAddress(details.getEmailAddress());
		user.setFirstName(details.getFirstName());
		user.setLastName(details.getLastName());
		user.setStartDate(new Date());
		user.setLoginName(details.getLoginName());
		dto.setUser(user);
		
		Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		for (Entry<Long, UserRolePrivBean> entry : details.getUserRowIdMap().entrySet()) {
			UserRolePrivBean userBean = entry.getValue();
			SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();
			bean.setAllCPChecked(true);
			SiteDAO dao = new SiteDAO();
			Site site = dao.getSite(hibernateDao, entry.getKey());
			List<Site> list = new ArrayList<Site>();
			list.add(site);
			bean.setSiteList(list);
			user.setSiteCollection(new HashSet<Site>(list));
			List<NameValueBean> privList = new ArrayList<NameValueBean>();
			for (String priv : userBean.getPrivileges()) {
			for (NameValueBean nvb : AppUtility.getAllPrivileges()) {
				if(nvb.getName().equals(priv)){
					privList.add(nvb);
//					break;
				}
			}
			}
			bean.setPrivileges(privList); 
			bean.setRole(new NameValueBean("Supervisor", 2));
			bean.setUser(user);
			userRowIdBeanMap.put(String.valueOf(site.getId()), bean);
		}
		dto.setUserRowIdBeanMap(userRowIdBeanMap);
		return dto;
	}

	private Institution getInst(String name, HibernateDAO hibernateDao) throws BizLogicException{
		String hql = "from "+Institution.class.getName()+" where name = '"+name+"'";
		List<Institution> result;
		try {
			result = hibernateDao.executeQuery(hql);
		}
		catch (DAOException e) {
			LOGGER.error(e);
			throw new BizLogicException(null, null,
					"errors.executeQuery.genericmessage", "");
		}
		return result.get(0);
	}

	private Department getDept(String name, HibernateDAO hibernateDao) throws BizLogicException {
		String hql = "from "+Department.class.getName()+" where name = '"+name+"'";
		List<Department> result;
		try {
			result = hibernateDao.executeQuery(hql);
		}
		catch (DAOException e) {
			LOGGER.error(e);
			throw new BizLogicException(null, null,
					"errors.executeQuery.genericmessage", "");
		}
		return result.get(0);
	}


	private CancerResearchGroup getCrg(String name,HibernateDAO hibernateDao) throws BizLogicException{
		String hql = "from "+CancerResearchGroup.class.getName()+" where name = '"+name+"'";
		List<CancerResearchGroup> result;
		try {
			result = hibernateDao.executeQuery(hql);
		}
		catch (DAOException e) {
			LOGGER.error(e);
			throw new BizLogicException(null, null,
					"errors.executeQuery.genericmessage", "");
		}
		return result.get(0);
	}


}
