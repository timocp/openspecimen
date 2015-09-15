package com.krishagni.catissueplus.core.audit.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.Hibernate;
import org.hibernate.envers.entities.mapper.relation.lazy.proxy.SetProxy;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.proxy.HibernateProxy;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.impl.DaoFactoryImpl;
import com.krishagni.rbac.domain.SubjectRole;


public class AuditUtil {
	
	private DaoFactory daoFactory;

	private String baseClass;

	private static Map<String, List<String>> entityAggregateMap = getEntityAggrMap();

	private HashMap<Class, Long> aggregateClassIdMap = new HashMap<Class, Long>();

	private final Set<String> excludedProperties = getExcludedProperties();
	
	private static Map<String, List<String>> getEntityAggrMap() {
		
		List<String> userAggregate = new ArrayList<String>();
		userAggregate.add("AuthDomain");
		userAggregate.add("Department");
		
		List<String> instAggregate = new ArrayList<String>();
		instAggregate.add("Department");
		
		List<String> siteAggregate = new ArrayList<String>();
		siteAggregate.add("User");
		siteAggregate.add("Institute");
		
		List<String> cpAggregate = new ArrayList<String>();
		cpAggregate.add("User");
		cpAggregate.add("Site");
		
		List<String> partAggregate = new ArrayList<String>();
		partAggregate.add("ParticipantMedicalIdentifier");
		partAggregate.add("CollectionProtocolRegistration");
		
		List<String> cprAggregate = new ArrayList<String>();
		cprAggregate.add("User");
		cprAggregate.add("ConsentTierResponse");
		
		List<String> visitAggregate = new ArrayList<String>();
		visitAggregate.add("CollectionProtocolRegistration");
		
		List<String> specimenAggregate = new ArrayList<String>();
		specimenAggregate.add("StorageContainerPosition");
		specimenAggregate.add("ExternalIdentifier");
		
		Map<String, List<String>> entityAggregateMap = new HashMap<String, List<String>>();
		entityAggregateMap.put("User", userAggregate);
		entityAggregateMap.put("Institute", instAggregate);
		entityAggregateMap.put("Site", siteAggregate);
		entityAggregateMap.put("CollectionProtocol", cpAggregate);
		entityAggregateMap.put("Participant", partAggregate);
		entityAggregateMap.put("CollectionProtocolRegistration", cprAggregate);
		entityAggregateMap.put("Visit", visitAggregate);
		entityAggregateMap.put("Specimen", specimenAggregate);
		
		return entityAggregateMap;
	}
	
	private void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}

	private String getBaseClass() {
		return baseClass;
	}
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public HashMap<Class, Long> getAggregateClassIdMap() {
		return aggregateClassIdMap;
	}

	public void setAggregateClassIdMap(HashMap<Class, Long> aggregateClassIdMap) {
		this.aggregateClassIdMap = aggregateClassIdMap;
	}

	public Object getObject(Class klass, Long objId) {
		return ((DaoFactoryImpl)daoFactory).getSessionFactory().getCurrentSession().load(klass, objId);
	}

	
	public AuditQuery getRevisionInstance(Class klass, Long current, Long entityId) {
		return daoFactory.getAuditDao().getAuditReader().createQuery()
				.forEntitiesAtRevision(klass, current)
				.add(AuditEntity.id().eq(entityId));

	}

	private Set<String> getExcludedProperties() {
		final Set<String> prop = new HashSet<String>();
		prop.add("class");
		prop.add("daoFactory");
		return prop;
	}
	
	private static boolean isSimpleObject(Object val) {
		if(val != null && (val instanceof Boolean || val instanceof Character || val instanceof Number 
				|| val instanceof String || val instanceof StringBuffer || val instanceof Date || val instanceof Enum)) {
			return true;
		}
		return false;
	}
	
	private static boolean isCollectionObject(Object val) {
		if(val != null && (val instanceof Collection || val instanceof SetProxy)) {
			return true;
		}
		return false;
	}
	
	private <T> T initalizeAndUnproxy(T entity) {
		if(entity != null) {
			Hibernate.initialize(entity);
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		
		return entity;
	}
	
	public List<Map<String, Object>> compareObjects(Object oldObject, Object newObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BeanMap map = new BeanMap(oldObject);
		List<Map<String, Object>> changeSet = new ArrayList<Map<String, Object>>();
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		aggregateClassIdMap.clear();
		String className = oldObject.getClass().getSimpleName();
		setBaseClass(className);
		
 		for (Object propNameObject : map.keySet()) {
			String propertyName = (String) propNameObject;
			try {
				if(excludedProperties.contains(propertyName)) {
					continue;
				}
				
				Object oldVal = propUtils.getProperty(oldObject, propertyName);
				Object newVal = propUtils.getProperty(newObject, propertyName);
				
				
				if (oldVal == null && newVal == null) {
					continue;
				}

				// ChangeSet of Simple Objects
				if (isSimpleObject(newVal)) {
					Object simpleChangeSet = getSimpleChangeSet(oldVal, newVal, className, propertyName);
					addToChangeSet(changeSet, simpleChangeSet);
				}

				// ChangeSet of One to Many or Collections
				else if (isCollectionObject(newVal)) {
					Object collectionsChangeSet = getCollectionsChangeSet(oldVal, newVal, className.concat(".").concat(propertyName));
					addToChangeSet(changeSet, collectionsChangeSet);
				}

				// ChangeSet of one to one Relations
				else {
					Object oneToOneChangeSet = getOneToOneChangeSet(oldVal, newVal, className.concat(".").concat(propertyName));
					addToChangeSet(changeSet, oneToOneChangeSet);
				}
			} catch (Exception e) {
				System.err.println("Error while fetching diff for property " + propertyName);
			}
		}
		return changeSet;
	}


	private Object getSimpleChangeSet(Object oldVal, Object newVal, String className, String propertyName) {
		try {
		if (oldVal != null && oldVal.equals(newVal)) {
			return null;
		} else {
			String propName = new StringBuilder(className).append(".").append(propertyName).toString();
			System.out.println("> " + propertyName + " is different (oldValue=\"" + oldVal + "\", newValue=\"" + newVal + "\")");
			return (getValueMap(propName, oldVal, newVal));
		}
		} catch (Exception e) {
			System.err.println("Error while getSimpleChangeSet");
		}
		return null;
	}

	private Object getCollectionsChangeSet(Object oldObj, Object newObj, String qualifiedName) {
		try {
				Object oldVal;
				Object newVal;
				System.out.println("qualifiedName : "+ qualifiedName);
				if (isAggregateCollection(oldObj) || isAggregateCollection(newObj)) {
					oldVal = getAggregareCollection(oldObj);
					newVal = getAggregareCollection(newObj);
				} else {
					oldVal = getValueCollection(oldObj);
					newVal = getValueCollection(newObj);
				}
				return (getValueMap(qualifiedName, oldVal, newVal));

		} catch (Exception e) {
			System.err.println("Error while getCollectionsChangeSet");
		}
		return null;
	}

	private boolean isAggregateCollection(Object obj) {
		if (obj != null) {
			Collection objects = (Collection) obj;
			for(Object aggregateObj : objects) {
				List<String> aggregates = entityAggregateMap.get(getBaseClass());
				if (aggregates != null && aggregates.contains(aggregateObj.getClass().getSimpleName())) {
					return true;
				}
			}
		}
		return false;
	}

	private Object getAggregareCollection(Object obj) throws Exception{
		Collection aggregateObjs = (Collection) obj;
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		Set<Object> aggreObjValues = new HashSet<Object>();
		for (Object aggregateObj : aggregateObjs) {
			aggreObjValues.add(getObjName(propUtils, aggregateObj, null));
		}
		return aggreObjValues;
	}

	private Object getValueCollection(Object valueObj) throws Exception {
		Collection<Map> objList = new HashSet<Map>();
		if ((!(valueObj instanceof Set)) || valueObj == null || ((Set) valueObj).size() == 0) {
			return null;
		}

		if (isSimpleObject(valueObj)) {
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMap.put("key", valueObj);
			objList.add(objMap);
			return objList;
		}

		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		Collection objects = (Collection) valueObj;
		for (Object obj : objects) {
			if (getBaseClass().equals(obj.getClass().getSimpleName())) {
				return null;
			}
			
			Map<String, Object> objMap = new HashMap<String, Object>();
			
			if(!isSimpleObject(obj)){
				
			
			BeanMap map = new BeanMap(obj);
			for (Object propNameObject : map.keySet()) {
				String propertyName = (String) propNameObject;
				// All Objects have class as one property which we dont need to have in audit
				if (excludedProperties.contains(propertyName)) {
					continue;
				}
				Object val = propUtils.getProperty(obj, propertyName);
				if (isSimpleObject(val)) {
					objMap.put(propertyName, val);
				} else if (isCollectionObject(val)) {
					objMap.put(propertyName, getValueCollection(val));
				} else {
					Object relatedObj = getObject(propUtils, val, "id");
					Object relatedObjName = getObjName(propUtils, relatedObj, propertyName);
					objMap.put(propertyName, relatedObjName);
				}
			}
			}
			objMap.put("name", obj);
			objList.add(objMap);
		}
		return objList;
	}


	private Object getOneToOneChangeSet(Object oldVal, Object newVal, String qualifiedName) 
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		try {
			PropertyUtilsBean propUtils = new PropertyUtilsBean();
			Object oldObj = getObject(propUtils, oldVal, "id");
			Object newObj = getObject(propUtils, newVal, "id");
			Object oldObjName = getObjName(propUtils, oldObj, null);
			Object newObjName = getObjName(propUtils, newObj, null);

			return (getValueMap(qualifiedName, oldObjName, newObjName));
		} catch (Exception e) {
			System.err.println("Error while getCollectionsChangeSet");
		}
		return null;
	}

	private Object getObjName(PropertyUtilsBean propUtils, Object obj, String objectName) 
			throws Exception{
		if (obj == null) {
			return null;
		}

		List<String> names = new ArrayList<String>();
		names.add("id");

		if (obj instanceof Specimen) {
			names.clear();
			names.add("label");
		} else if (obj instanceof Site) {
			names.clear();
			names.add("name");
		} else if (obj instanceof User) {
			names.clear();
			names.add("firstName");
			names.add("lastName");
			names.add("loginName");
		}  else {
			System.out.println("Object name not found " + obj.getClass().getSimpleName());
		}

		String objName = "";
		for (String name : names) {
			Object objVal = propUtils.getProperty(obj, name);
			if (isSimpleObject(objVal) ) {
				objVal = new StringBuilder(name).append(" : ").append(objVal);
			} else {
				objVal = (String)getObjName(propUtils, objVal, name);
			}
			objName = new StringBuilder(objectName != null ? objectName : objName).append(objName.isEmpty() ? "" : " ")
						.append(objVal)
						.toString();
		}
		return objName;
	}

	private Object getObject(PropertyUtilsBean propUtils, Object obj, String propertyName) {
		if (obj == null) {
			return null;
		}
		
		try {
			if(obj instanceof HibernateProxy) {
				Object handler = propUtils.getProperty(obj, "handler");
				AuditUtil audit = new AuditUtil();
				Long id = (Long) propUtils.getProperty(handler, propertyName);
				Object persistedObj = audit.getObject(handler.getClass(), id);
				return persistedObj;
				
			} else {
				return obj;
			}
		} catch (Exception e) {
			try {
				return initalizeAndUnproxy(obj);
			} catch(Exception exception) {
				System.err.println("Error on getObject propertyName : "+ propertyName);
			}
		}
		return null;
	}
	
	public void addToChangeSet(List<Map<String, Object>> changeSet, Object newValues) {
		if (newValues == null) {
			return;
		}

		if (newValues instanceof List) {
			for (Map<String, Object> newValue : (List<Map<String, Object>>)newValues) {
				changeSet.add(newValue);
			}
		} else if (newValues instanceof Map) {
			changeSet.add((Map<String, Object>) newValues);
		}
	}

	private Object getValueMap(String attr, Object oldVal, Object newVal) {
		if (oldVal == null && newVal == null) {
			return null;
		} else if (isSimpleObject(oldVal) && oldVal.equals(newVal)) {
			return null;
		}
		Map<String, Object> values = new HashMap<String, Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		try {
			if (oldVal instanceof Map) {
				Set<Map<String, Object>> valList = new HashSet<Map<String, Object>>();
				for (Map.Entry entry :  ((Map<String, Object>) oldVal).entrySet()) {
					Map<String, Object> val = new HashMap<String, Object>();
					String key = (String) entry.getKey();
					val.put("attr", attr + "." + key);
					val.put("oldVal", ow.writeValueAsString(entry.getValue()));
					val.put("newVal", ((Map<String, Object>) newVal).get(entry.getKey()));
					valList.add(val);
				}
				return valList;
			}
			values.put("attr", attr);
			values.put("oldVal", ow.writeValueAsString(oldVal));
			values.put("newVal", ow.writeValueAsString(newVal));
			return values;
		} catch (Exception e) {
			System.err.println("Error on getValueMap attr : "+ attr);
		}
		return null;
	}
	
	
}
