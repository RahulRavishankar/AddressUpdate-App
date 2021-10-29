package com.example.AddressUpdateApp.requester;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Requester {

	@Id
	private String uid;
	private String txnId;

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

	@Override
	public String toString() {
		return "Requester{" +
				"uid='" + uid + '\'' +
				", txnId='" + txnId + '\'' +
				'}';
	}
}
