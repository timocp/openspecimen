
var com = com || {};
com.krishagni = com.krishagni || {};
com.krishagni.openspecimen = com.krishagni.openspecimen || {};
com.krishagni.openspecimen.query = com.krishagni.openspecimen.query || {};

com.krishagni.openspecimen.query.defaults = {
  resultViewFields: [
    "Participant.id", "Participant.vitalStatus", "Participant.ppid", 
    "Participant.medicalRecord.medicalRecordNumber", "Participant.medicalRecord.mrnSiteName"
  ]
};
