package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenKitDetail {
    private Long id;

    private Long cpId;

    private String cpShortTitle;

    private String cpTitle;

    private String sendingSite;

    private String receivingSite;

    private Date sendingDate;

    private UserSummary sender;

    private List<SpecimenInfo> specimens;

    private String comments;

    private String activityStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public String getCpShortTitle() {
        return cpShortTitle;
    }

    public void setCpShortTitle(String cpShortTitle) {
        this.cpShortTitle = cpShortTitle;
    }

    public String getCpTitle() {
        return cpTitle;
    }

    public void setCpTitle(String cpTitle) {
        this.cpTitle = cpTitle;
    }

    public String getSendingSite() {
        return sendingSite;
    }

    public void setSendingSite(String sendingSite) {
        this.sendingSite = sendingSite;
    }

    public String getReceivingSite() {
        return receivingSite;
    }

    public void setReceivingSite(String receivingSite) {
        this.receivingSite = receivingSite;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    public UserSummary getSender() {
        return sender;
    }

    public void setSender(UserSummary sender) {
        this.sender = sender;
    }

    public List<SpecimenInfo> getSpecimens() {
        return specimens;
    }

    public void setSpecimens(List<SpecimenInfo> specimens) {
        this.specimens = specimens;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public static SpecimenKitDetail from(SpecimenKit kit) {
        SpecimenKitDetail detail = new SpecimenKitDetail();
        detail.setId(kit.getId());
        detail.setCpId(kit.getCollectionProtocol().getId());
        detail.setCpTitle(kit.getCollectionProtocol().getTitle());
        detail.setCpShortTitle(kit.getCollectionProtocol().getShortTitle());
        detail.setSendingSite(kit.getSendingSite().getName());
        detail.setReceivingSite(kit.getReceivingSite().getName());
        detail.setSendingDate(kit.getSendingDate());
        detail.setSender(UserSummary.from(kit.getSender()));
        detail.setSpecimens(SpecimenInfo.from(new ArrayList<Specimen>(kit.getSpecimens())));
        detail.setComments(kit.getComments());
        detail.setActivityStatus(kit.getActivityStatus());
        return detail;
    }
}
