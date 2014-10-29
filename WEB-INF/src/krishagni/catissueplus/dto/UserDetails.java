
package krishagni.catissueplus.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;

public class UserDetails {

	//	{"id":"107","lastName":"lname","firstName":"fname","emailAddress":"aka@a.com","loginName":"aka@a.com","userSiteIds":[],
	//		"userRowIdMap":{"15":{"privileges":["Distribution","Specimen Processing","Registration"],"isAllCPChecked":true,
	//		"isRowDeleted":false,"isRowEdited":true,"isCustChecked":false}},"activityStatus":"Active","deptName":"DEP",
	//		"instName":"AKU","crgName":"CRG","comments":"comments","city":"city","state":"state","country":"India"}

	private Long id;

	private String lastName;

	private String firstName;

	private String emailAddress;

	private String loginName;

	private Map<Long, UserRolePrivBean> userRowIdMap = new HashMap<Long, UserRolePrivBean>();

	private Date createDate;

	private String activityStatus;

	private String deptName;

	private String instName;

	private String crgName;

	private String comments;

	private String street;

	private String city;

	private String state;

	private String country;

	private String zipCode;

	private String faxNumber;

	private String phoneNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Map<Long, UserRolePrivBean> getUserRowIdMap() {
		return userRowIdMap;
	}

	public void setUserRowIdMap(Map<Long, UserRolePrivBean> userRowIdMap) {
		this.userRowIdMap = userRowIdMap;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getCrgName() {
		return crgName;
	}

	public void setCrgName(String crgName) {
		this.crgName = crgName;
	}

	public static UserDetails fromDomain(UserDTO userDto) {
		UserDetails details = new UserDetails();
		User user = userDto.getUser();
		details.setActivityStatus(user.getActivityStatus());
		details.setCity(user.getAddress().getCity());
		details.setComments(user.getComments());
		details.setCountry(user.getAddress().getCountry());
		details.setCreateDate(user.getStartDate());
		details.setCrgName(user.getCancerResearchGroup().getName());
		details.setDeptName(user.getDepartment().getName());
		details.setEmailAddress(user.getEmailAddress());
		details.setFaxNumber(user.getAddress().getFaxNumber());
		details.setFirstName(user.getFirstName());
		details.setId(user.getId());
		details.setInstName(user.getInstitution().getName());
		details.setLastName(user.getLastName());
		details.setLoginName(user.getLoginName());
		details.setPhoneNumber(user.getAddress().getPhoneNumber());
		details.setState(user.getAddress().getState());
		details.setStreet(user.getAddress().getStreet());
		details.setZipCode(user.getAddress().getZipCode());
		
		return details;
	}

}
