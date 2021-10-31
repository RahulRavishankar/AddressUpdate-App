package com.example.AddressUpdateApp.introducer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.AddressUpdateApp.utils.Consent;

@Entity
@Table
public class Introducer {

	@Id
	private String uid;
	private String txnId;
	private String requesterUid;
	private String address;
	private Consent consentProvided;

	public Introducer() {
	}

	public Introducer(String uid, String txnId, String requesterUid, String address, Consent consentProvided) {
		this.uid = uid;
		this.txnId = txnId;
		this.requesterUid = requesterUid;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
				", address='" + address + '\'' +
				", consentProvided=" + consentProvided +
				'}';
	}
}
