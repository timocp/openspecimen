
package com.krishagni.catissueplus.core.biospecimen.domain;

public class Race {

	private Long id;

	private String raceName;

	private Participant participant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRaceName() {
		return raceName;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	
	@Override
	public int hashCode() {
		return 31 * 1 + ((raceName == null) ? 0 : raceName.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Race other = (Race) obj;
		if (!raceName.equals(other.raceName)) {
			return false;
		}
		return true;
	}

}
