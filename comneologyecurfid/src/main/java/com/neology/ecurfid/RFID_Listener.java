package com.neology.ecurfid;

public interface RFID_Listener {
	public void Event_RFID(final String data);
	public void Event_Log(final String data);
}
