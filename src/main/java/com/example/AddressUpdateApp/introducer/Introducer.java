package com.example.AddressUpdateApp.introducer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

enum Consent {
	GIVEN, NOT_GIVEN, AWAITING_RESPONSE
}

@Entity
@Table
public class Introducer {

	@Id
	private String uid;
	private String txnId;
	private String requesterUid;
	private String addressXml;
	private Consent consentProvided;

	public Introducer() {
	}

	public Introducer(String uid, String txnId, String requesterUid, String addressXml, Consent consentProvided) {
		this.uid = uid;
		this.txnId = txnId;
		this.requesterUid = requesterUid;
		this.addressXml = addressXml;
		this.consentProvided = consentProvided;
	}

	public Introducer(String uid, String requesterUid) {
		this(uid, null, requesterUid, null, Consent.AWAITING_RESPONSE);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRequesterUid() {
		return requesterUid;
	}

	public void setRequesterUid(String requesterUid) {
		this.requesterUid = requesterUid;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getAddressXml() {
		return addressXml;
	}

	public void setAddressXml(String addressXml) {
		this.addressXml = addressXml;
	}

	public Consent getConsentProvided() {
		return consentProvided;
	}

	public void setConsentProvided(Consent consentProvided) {
		this.consentProvided = consentProvided;
	}

	@Override
	public String toString() {
		return "Introducer{" +
				"uid='" + uid + '\'' +
				", txnId='" + txnId + '\'' +
				", requesterUid='" + requesterUid + '\'' +
				", addressXml='" + addressXml + '\'' +
				", consentProvided=" + consentProvided +
				'}';
	}
}
