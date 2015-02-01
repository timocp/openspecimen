package krishagni.catissueplus.rest;

import edu.wustl.common.beans.SessionDataBean;

public class AuthenticatedSessionCtx {
	private static final ThreadLocal<SessionDataBean> sessionCtx = new ThreadLocal<SessionDataBean>();

	public static void setCtx(SessionDataBean sdb) {
		sessionCtx.set(sdb);
	}
	
	public static SessionDataBean getSession() {
		return sessionCtx.get();
	}
	
	public static void clear() {
		sessionCtx.remove();
	}
}
