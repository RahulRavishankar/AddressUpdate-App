package com.example.AddressUpdateApp.requester;

import com.example.AddressUpdateApp.utils.Consent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Requester {

	@Id
	private String uid;
	private String txnId;
	private String introducerId;
	private String address;
	private Consent consentProvided;
	private String newAddress;

	public Requester() {
	}

	private Requester(String uid, String txnId) {
		this.uid = uid;
		this.txnId = txnId;
	}

	public Requester(String uid) {
		this(uid,null);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getIntroducerId() {
		return introducerId;
	}

	public void setIntroducerId(String introducerId) {
		this.introducerId = introducerId;
	}

	@Override
	public String toString() {
		return "Requester{" +
				"uid='" + uid + '\'' +
				", txnId='" + txnId + '\'' +
				", address='" + address + '\'' +
				", consentProvided=" + consentProvided +
				", newAddress='" + newAddress + '\'' +
				'}';
	}
}
