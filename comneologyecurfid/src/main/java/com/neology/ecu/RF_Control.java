package com.neology.ecu;

import java.lang.ref.WeakReference;
import java.util.Vector;

import com.atid.lib.dev.ATRfidManager;
import com.atid.lib.dev.rfid.param.SimpleSelectionMask;
import com.neology.ecunfc.NFC;
import com.neology.ecunfc.NFC_Listener;
import com.neology.ecurfid.RFID;
import com.neology.ecurfid.RFID_Listener;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class RF_Control implements RFID_Listener, NFC_Listener {
	private final static String TAG = "RF Control";
	
	private RFID rfid = null;
	private NFC _nfc = null;
	private String PersoUHF = null;
	
	
	String sFolio = null;
	String sPlaca = null;
	String sSubsidio = null;
	String sGalones = null;
	
	protected Vector<RF_Listener> _listeners;

	private void add_RF_Listener(RF_Listener listener) {
		if (_listeners == null)
			_listeners = new Vector<RF_Listener>();
		_listeners.addElement(listener);
	}

	private void remove_RF_Listeners() {
		if (_listeners != null)
			_listeners.clear();
	}

	
	public boolean Initialize(Activity ref) 
	{
		_nfc = new NFC();
		_nfc.Initialize(new WeakReference<Activity>(ref), this);
		_nfc.modoEscritura();
		
		rfid = new RFID();
		rfid.Initialize(/*ref, */this);
		
		_listeners = new Vector<RF_Listener>();
		add_RF_Listener((RF_Listener) ref);
		
		sendRF_LogEvent("RF Control v20151204 inicializado");
		
		return true;	
	}
	
	
	public void onPauseProcessNFC() {
		_nfc.onPauseProcess();
	}
	
	public void onResumeProcessNFC() {
		_nfc.onResumeProcess();
	}
	
	public void onNewIntentProcessNFC(Intent intent) {
	   _nfc.onNewIntentProcess(intent); 
	}
		
	
	////////////////////////////////////////////
	//             		UHF
	////////////////////////////////////////////
	
	public boolean prePersoTagUHF(String folio) {
		return rfid.prePersoTag(folio);
	}
	
	public boolean persoTagUHF(String folio, String Placa, String sVin, String TipoV, String TypeCombus, String Subsidio,
			String Galones, String FechaRec, String GalonesConsu){
		
		sFolio = folio;
		sPlaca = Placa;
		sSubsidio = Subsidio;
		sGalones = Galones;
		
		boolean result =  rfid.persoTag(folio,Placa,sVin,TipoV,TypeCombus,Subsidio,Galones,FechaRec,GalonesConsu);
		if (result)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean grabarConsumoUHF(String folio, String FechaRec, String Galones) {
		return rfid.grabarConsumo(folio,FechaRec,Galones);
	}

	public boolean leerTagUHF(String folio) {
		return rfid.leerTag(folio);
	}

	////////////////////////////////////////////
	//             		NFC
	///////////////////////////////////////////
	public boolean persoTagNFC(String folio, String Placa, String Subsidio,	String Galones ){
		return _nfc.ingresarDatos(folio, Placa, Subsidio,Galones);
	}
	
	public boolean readTagNFC(){
		_nfc.modoLectura();
		return true;
	}

	public void Destroy() {
		remove_RF_Listeners();
		if (rfid != null) {
			try {
				rfid.Destroy();
				_nfc.modoInactivo();
			} catch (Exception ex) {
				Log.e(TAG, "Destroy: " + ex.getMessage());
			}
		}
		rfid = null;
	}
	
	@Override
	public void Event_RFID(final String data) {
		Log.d(TAG, "Event_RFID RF Control: "+data);
		String[] SplitStr = data.split("|");
		if (SplitStr[1].equals("0") && SplitStr[3].equals("2"))
		{
			PersoUHF = data;
			Log.d(TAG, "Event_RFID RF Control: Inicia Perso NFC");
			sendRF_RFIDEvent("0|7|Presente Tag NFC");
			persoTagNFC(sFolio, sPlaca, sSubsidio, sGalones);
		}
		else
			sendRF_RFIDEvent(data);	 
	}
	
	@Override
	public void Event_NFC(String data) {
		Log.d(TAG, "Event_NFC RF_Control: "+ data);
		String[] SplitStr = data.split("|");
		if (SplitStr[1].equals("0") && SplitStr[3].equals("5"))
		{
			PersoUHF = PersoUHF +"|"+ data.substring(4,18);
			sendRF_RFIDEvent(PersoUHF);
			_nfc.modoInactivo();
		}
		else
		{
			sendRF_RFIDEvent(data);
			//_nfc.modoInactivo();
		}
	}
	
	
	@Override
	public void Event_Log(String data) {
		Log.d(TAG, "Event_Log RF Control: "+data);
		//sendRF_LogEvent("UHF "+ data);
	}
	
	// region Send events methods
	private void sendRF_RFIDEvent(String data) {
		if (_listeners != null) {
			for (RF_Listener l : _listeners)
				l.Event_RF_Control(data);
		}
	}
	
	// region Send events methods
	private void sendRF_LogEvent(String data) {
		if (_listeners != null) {
			for (RF_Listener l : _listeners)
				l.Event_RF_Log(data);
		}
	}

	@Override
	public void Event_Log_NFC(String data) {
	
	}
}
