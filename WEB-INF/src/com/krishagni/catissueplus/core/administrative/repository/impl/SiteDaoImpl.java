
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SiteDaoImpl extends AbstractDao<Site> implements SiteDao {

	private static final String FQN = Site.class.getName();

	private static final String GET_SITE_BY_NAME = FQN + ".getSiteByName";

	@Override
	@SuppressWarnings("unchecked")
	public Site getSite(String siteName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SITE_BY_NAME);
		query.setString("siteName", siteName);
		List<Site> siteList = query.list();
		return siteList.isEmpty() ? null : siteList.get(0);
	}

	@Override
	public Site getSite(Long id) {
		return (Site) sessionFactory.getCurrentSession().get(Site.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Boolean isUniqueSiteName(String siteName) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(GET_SITE_BY_NAME);
		query.setString("siteName", siteName);
		List<Site> siteList = query.list();
		return siteList.isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Site> getAllSites(String name, int maxResults) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Site.class);
		
		if (name != null && !name.trim().isEmpty()) {
			query.add(Restrictions.ilike("name", name.trim(), MatchMode.ANYWHERE));
		}
		query.add(Restrictions.ne("activityStatus", "Disabled"));
		query.addOrder(Order.asc("name"));
		
		if (maxResults <= 0) {
			maxResults = 100;
		}
		query.setMaxResults(maxResults);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> getSites(List<Long> siteIds) {
		return sessionFactory.getCurrentSession().createCriteria(Site.class)
				.add(Restrictions.in("id", siteIds))
				.list();
	}
}
