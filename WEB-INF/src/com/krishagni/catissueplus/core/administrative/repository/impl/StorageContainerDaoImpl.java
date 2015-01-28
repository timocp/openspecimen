
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {
	
	@Override
	public StorageContainer getStorageContainer(Long id) {
		return (StorageContainer) sessionFactory.getCurrentSession().get(StorageContainer.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorageContainerSummary> getStorageContainers(
			String name,
			int maxResults, 
			Long specimenId,
			boolean onlyFreeContainers) {
		
		Long cpId = null; 
		String specimenType = null;
		
		Object[] cpAndSpecimenType = getCpAndSpecimenType(specimenId);
		if (cpAndSpecimenType != null) {
			cpId = ((Number)cpAndSpecimenType[0]).longValue();
			specimenType = (String)cpAndSpecimenType[1];
		}
				
		Query query = getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(getContainersSql(cpId, specimenType, name, onlyFreeContainers));

		int idx = 0;
		if (specimenType != null) {
			query.setLong(idx++, cpId).setString(idx++, specimenType);
		}
		
		if (StringUtils.isNotBlank(name)) {
			query.setString(idx++, "%" + name.toUpperCase() + "%");
		}
		
		if (maxResults <= 0) {
			maxResults = 100;
		}
		
		query.setMaxResults(maxResults);
		return getContainersList(query.list());
	}
	
	@Override
	public Specimen getSpecimen(Long specimenId) {
		return (Specimen)getSessionFactory().getCurrentSession().get(Specimen.class, specimenId);
	}
	
	
	@SuppressWarnings("unchecked")
	private Object[] getCpAndSpecimenType(Long specimenId) {
		if (specimenId == null) {
			return null;
		}
				
		List<Object[]> rows = getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(GET_CP_AND_TYPE_BY_SPECIMEN_ID_SQL)
				.setLong(0, specimenId)
				.list();		
		
		if (CollectionUtils.isEmpty(rows)) {
			return null;
		}
		
		return rows.iterator().next();
	}
	
	private String getContainersSql(Long cpId, String specimenType, String name, boolean onlyFreeContainers) {
		StringBuilder restrictions = new StringBuilder();
		StringBuilder join = new StringBuilder();
		String having = "";
		
		if (specimenType != null) {
			restrictions.append(CP_AND_SPECIMEN_TYPE_RESTRICTION);
			join.append(CP_AND_SPECIMEN_TYPE_JOIN);
		}
		
		if (StringUtils.isNotBlank(name)) {
			restrictions.append(NAME_RESTRICTION);
		}
		
		if (onlyFreeContainers) {
			having = FREE_CONTAINERS_RESTRICTION;
		}
		
		return String.format(GET_CONTAINERS_SQL, join.toString(), restrictions.toString(), having);		
	}
	
	private List<StorageContainerSummary> getContainersList(List<Object[]> rows) {
		if (CollectionUtils.isEmpty(rows)) {
			return Collections.emptyList();
		}
		
		List<StorageContainerSummary> result = new ArrayList<StorageContainerSummary>();
		for (Object[] row : rows) {
			result.add(getContainer(row)); 
		}
		
		return result;		
	}
	
	private StorageContainerSummary getContainer(Object[] row) {
		StorageContainerSummary result = new StorageContainerSummary();
		result.setId(((Number)row[0]).longValue());
		result.setName((String)row[1]);
		result.setSiteName((String)row[4]);
		result.setActivityStatus("Active");
		return result;
	}
	
	private static final String GET_CONTAINERS_SQL =
			"select " +
			"  c.identifier, c.name, " +
			"  cap.one_dimension_capacity * cap.two_dimension_capacity as capacity, " +
			"  count(distinct sp.identifier) + count(distinct cp.identifier) as consumed, " +
			"  site.name " +
			"from " +
			"  catissue_storage_container sc " +
			"  inner join catissue_container c on c.identifier = sc.identifier " +
			"  inner join catissue_capacity cap on cap.identifier = c.capacity_id " +
			"  inner join catissue_site site on site.identifier = sc.site_id " +
			"  left join catissue_specimen_position sp on sp.container_id = c.identifier " +
			"  left join catissue_container_position cp on cp.parent_container_id = c.identifier " +
			"  %s " +
			"where " +
			"  c.activity_status = 'Active'" +
			"  %s " +
			"group by " +
			"  c.identifier, c.name, cap.one_dimension_capacity, cap.two_dimension_capacity " +
			"%s " +
			"order by " +
			"  c.name";
	
	private static final String CP_AND_SPECIMEN_TYPE_RESTRICTION = 
			"and (cpp.collection_protocol_id = ? or cpp.collection_protocol_id is null) " +
			"and (stype.specimen_type = ? or stype.specimen_type is null)";
	
	private static final String NAME_RESTRICTION = 
			"and upper(c.name) like ? ";
	
	private static final String CP_AND_SPECIMEN_TYPE_JOIN = 
			" left join catissue_st_cont_coll_prot_rel cpp on cpp.storage_container_id = c.identifier " +
			" left join catissue_stor_cont_spec_type stype on stype.storage_container_id = c.identifier";
	
	private static final String FREE_CONTAINERS_RESTRICTION = 
			"having (cap.one_dimension_capacity * cap.two_dimension_capacity) > (count(distinct sp.identifier) + count(distinct cp.identifier))";
	
	private static final String GET_CP_AND_TYPE_BY_SPECIMEN_ID_SQL =
			"select " +
			"  cpr.collection_protocol_id, s.specimen_type " +
			"from " +
			"  catissue_specimen s " +
			"  inner join catissue_specimen_coll_group visit on visit.identifier = s.specimen_collection_group_id " +
			"  inner join catissue_coll_prot_reg cpr on cpr.identifier = visit.collection_protocol_reg_id " +
			"where " +
			"  s.identifier = ? and s.activity_status = 'Active'";
}
