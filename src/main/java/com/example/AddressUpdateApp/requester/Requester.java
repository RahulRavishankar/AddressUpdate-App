package com.example.AddressUpdateApp.requester;

public class Requester {

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

	@Override
	public String toString() {
		return "Requester{" +
				"uid='" + uid + '\'' +
				", txnId='" + txnId + '\'' +
				'}';
	}
}
