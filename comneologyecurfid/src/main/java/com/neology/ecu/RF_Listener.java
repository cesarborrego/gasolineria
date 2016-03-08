package com.neology.ecu;

public interface RF_Listener {
	public void Event_RF_Control(final String data);
	public void Event_RF_Log(final String data);
}
