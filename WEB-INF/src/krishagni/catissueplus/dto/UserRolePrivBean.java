/**
 *
 */

package krishagni.catissueplus.dto;

import java.util.ArrayList;
import java.util.List;

public class UserRolePrivBean {

	private boolean isAllCPChecked = false;

	private List<String> privileges = new ArrayList<String>();

	public boolean isAllCPChecked() {
		return isAllCPChecked;
	}

	public void setAllCPChecked(boolean isAllCPChecked) {
		this.isAllCPChecked = isAllCPChecked;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}

}
