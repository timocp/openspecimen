
package com.krishagni.catissueplus.core.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ErrorCodeEnum;

import edu.wustl.common.util.logger.Logger;

@Aspect
public class TransactionalInterceptor {

	private static Logger LOGGER = Logger.getCommonLogger(TransactionalInterceptor.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Pointcut("within(com.krishagni.catissueplus.service.impl.* || com.krishagni.catissueplus.core.*.services.impl.*)")
	public void anyPublicMethod() {
	}

	@Around("anyPublicMethod() && @annotation(plusTransactional)")
	public Object startTransaction(ProceedingJoinPoint pjp, PlusTransactional plusTransactional) {
		boolean isTransactionStarted = false;
		boolean isSessionStarted = false;

		Session session = null;
		try {
			session = SessionFactoryUtils.doGetSession(sessionFactory, false);
		}
		catch (IllegalStateException ex) {
			//			ex.printStackTrace();
			LOGGER.info("Session not found. Creating a new session");
			LOGGER.info(ex.getMessage(),ex);
		}

		if (session == null) {
			session = sessionFactory.getCurrentSession();
			LOGGER.info("New session created");
			isSessionStarted = true;
		}

		Transaction tx = session.getTransaction();
		if (tx != null && !tx.isActive()) {
			tx = session.beginTransaction();
			LOGGER.info("New transaction started");
			isTransactionStarted = true;
		}

		Object object = null;
		try {
			object = pjp.proceed();
		}
		catch (Throwable e) {
			LOGGER.error(e.getCause(),e);
			LOGGER.error(e.getMessage(),e);
			if (isTransactionStarted && tx != null) {
				tx.rollback();
				LOGGER.info("Error thrown, transaction rolled back.");
			}
			throw new CatissueException(ErrorCodeEnum.QUERY_EXECUTION_ERROR);
		}
		finally {
			if (isTransactionStarted && tx != null) {
				tx.commit();
			}
			if (session != null && session.isOpen() && isSessionStarted) {
				session.close();
				LOGGER.info("Session closed.");
			}

		}

		return object;
	}
}
