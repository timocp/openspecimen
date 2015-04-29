
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.text.DateFormatter;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.MapUpdater;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class Participant {

	/**
	 * System generated unique id.
	 * */
	protected Long id;

	/**
	 * Last name of the participant.
	 */
	protected String lastName;

	/**
	 * First name of the participant.
	 */
	protected String firstName;

	/**
	 * Middle name of the participant.
	 */
	protected String middleName;

	/**
	 * Birth date of participant.
	 */
	protected Date birthDate;

	/**
	 * The gender of the participant.
	 */
	protected String gender;

	/**
	 * The genetic constitution of the individual.
	 */
	protected String sexGenotype;

	/**
	 * Participant's race origination.
	 */
	protected Set<Race> raceColl = new HashSet<Race>();

	/**
	 * Participant's ethnicity status.
	 */
	protected String ethnicity;

	/**
	 * Social Security Number of participant.
	 */
	protected String socialSecurityNumber;

	/**
	 * Defines whether this participant record can be queried (Active) or not
	 * queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	/**
	 * Death date of participant.
	 */
	protected Date deathDate;

	/**
	 * Defines the vital status of the participant like 'Dead', 'Alive' or
	 * 'Unknown'.
	 */
	protected String vitalStatus;

	protected Map<String, ParticipantMedicalIdentifier> pmiCollection = new HashMap<String, ParticipantMedicalIdentifier>();

	/**
	 * A collection of registration of a Participant to a Collection Protocol.
	 */
	protected Map<String, CollectionProtocolRegistration> cprCollection = new HashMap<String, CollectionProtocolRegistration>();

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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
	}

	public Set<Race> getRaceColl() {
		return raceColl;
	}

	public void setRaceColl(Set<Race> raceCollection) {
		this.raceColl = raceCollection;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete(false);
		}
		this.activityStatus = activityStatus;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public Map<String, ParticipantMedicalIdentifier> getPmiCollection() {
		return pmiCollection;
	}

	public void setPmiCollection(Map<String, ParticipantMedicalIdentifier> participantMedicalIdentifierCollection) {
		this.pmiCollection = participantMedicalIdentifierCollection;
	}

	public Map<String, CollectionProtocolRegistration> getCprCollection() {
		return cprCollection;
	}

	public void setCprCollection(Map<String, CollectionProtocolRegistration> collectionProtocolRegistrationCollection) {
		this.cprCollection = collectionProtocolRegistrationCollection;
	}

	public void update(Participant participant) {
		this.setFirstName(participant.getFirstName());
		this.setLastName(participant.getLastName());
		this.setMiddleName(participant.getMiddleName());
		this.setSocialSecurityNumber(participant.getSocialSecurityNumber());
		this.setActivityStatus(participant.getActivityStatus());
		this.setSexGenotype(participant.getSexGenotype());
		this.setVitalStatus(participant.getVitalStatus());
		this.setGender(participant.getGender());
		this.setEthnicity(participant.getEthnicity());
		this.setBirthDate(participant.getBirthDate());
		this.setDeathDate(participant.getDeathDate());
		updateRace(participant.getRaceColl());
		updatePmi(participant);
		if(participant.getPmiCollection() != null){
			Iterator<Entry<String, ParticipantMedicalIdentifier>> entries = participant.getPmiCollection().entrySet().iterator();
			while(entries.hasNext()) {
				ParticipantMedicalIdentifier pmi = entries.next().getValue();
				pmi.setParticipant(this);
			}
		}
	}

	private void updateRace(Set<Race> raceColl) {
		SetUpdater.<Race> newInstance().update(this.raceColl, raceColl);
		for (Race race : this.raceColl) {
			race.setParticipant(this);
		}
	}

	private void updatePmi(Participant participant) {
		updateMrn(this.pmiCollection,participant.getPmiCollection());
	}
	
	private void updateMrn(Map<String, ParticipantMedicalIdentifier> oldColl,Map<String, ParticipantMedicalIdentifier> newColl)
	{
		oldColl = oldColl==null?new HashMap<String, ParticipantMedicalIdentifier>():oldColl;
		newColl = newColl==null?new HashMap<String, ParticipantMedicalIdentifier>():newColl;
		Iterator<Entry<String, ParticipantMedicalIdentifier>> entries = oldColl.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, ParticipantMedicalIdentifier> entry = entries.next();
			if (!newColl.containsKey(entry.getKey())) {
				entries.remove();
			}
		}
		Iterator<Entry<String, ParticipantMedicalIdentifier>> newEntries = newColl.entrySet().iterator();
		while (newEntries.hasNext()) { 
			Entry<String, ParticipantMedicalIdentifier> entry = newEntries.next();
			if (!oldColl.containsKey(entry.getKey())) {
				oldColl.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public void updateActivityStatus(String activityStatus) {
		setActivityStatus(activityStatus);
	}

	public void setActive() {
		setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.activityStatus);
	}

	public void delete(boolean isIncludeChildren) {
		if (isIncludeChildren) {
			Map<String, CollectionProtocolRegistration> registrationCollection = this.cprCollection;
			for (CollectionProtocolRegistration cpr : registrationCollection.values()) {
				cpr.delete(isIncludeChildren);
			}
		}
		else {
			checkActiveDependents();
		}
		updateMrn();
		this.socialSecurityNumber = Utility.getDisabledValue(this.socialSecurityNumber);
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	private void updateMrn() {
		Iterator<ParticipantMedicalIdentifier> entries = this.pmiCollection.values().iterator();
		while (entries.hasNext()) {
			ParticipantMedicalIdentifier pmi = entries.next();
			pmi.setMedicalRecordNumber(Utility.getDisabledValue(pmi.getMedicalRecordNumber()));
		}
	}

	private void checkActiveDependents() {
		for (CollectionProtocolRegistration cpr : this.cprCollection.values()) {
			if (cpr.isActive()) {
				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
			}
		}
	}

}
