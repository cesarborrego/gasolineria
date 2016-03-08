package com.neology.ecunfc;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Locale;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.util.Log;

@SuppressWarnings("unused")
public class NFC {
	// region Field Variables and Constants
	private final static String TAG = "ECU_NFC";

	// Resultados
	private final static int RESULT_ECU_00_Success = 0;
	private final static int RESULT_ECU_01_NoTagInTheField = 1;
	private final static int RESULT_ECU_02_MoreThan1Tag = 2;
	private final static int RESULT_ECU_03_CannotReadBlock = 3;
	private final static int RESULT_ECU_04_ReaderConnectionFailed = 4;
	private final static int RESULT_ECU_05_ReaderConfigurationError = 5;
	private final static int RESULT_ECU_10_WrongDataFormat = 10;
	private final static int RESULT_ECU_11_MemoryOverrun = 11;
	private final static int RESULT_ECU_12_BlockedMemory = 12;
	private final static int RESULT_ECU_13_OffsetOutOfRange = 13;
	private final static int RESULT_ECU_14_WrongTollingFormat = 14;
	private final static int RESULT_ECU_15_WriteError = 15;
	private final static int RESULT_ECU_16_LenghtOutOfRange = 16;
	private final static int RESULT_ECU_17_WrongBNJFolioFormat = 17;
	private final static int RESULT_ECU_18_TollingWriteError = 18;
	private final static int RESULT_ECU_19_WrongPrefix = 19;
	private final static int RESULT_ECU_20_NotPersonalizedTag = 20;
	private final static int RESULT_ECU_21_TagNotReadPreviously = 21;
	private final static int RESULT_ECU_60_Exception_leerFolioTLP = 60;
	private final static int RESULT_ECU_61_Exception_leerUID = 61;
	private final static int RESULT_ECU_62_Exception_leerFolioBNJ = 62;
	private final static int RESULT_ECU_63_Exception_leerChipBNJ = 63;
	private final static int RESULT_ECU_64_Exception_grabarChipBNJ = 64;
	private final static int RESULT_ECU_65_Exception_grabarChipBNJ = 65;

	private final static int RESULT_ECU__1_Unknown = -1;

	private final static String RESULT_ECU_00_Success_Text = "** Operacin exitosa";
	private final static String RESULT_ECU_01_NoTagInTheField_Text = "** No hay tag en el campo de lectura";
	private final static String RESULT_ECU_02_MoreThan1Tag_Text = "** M�s de 1 tag en el campo de lectura";
	private final static String RESULT_ECU_03_CannotReadBlock_Text = "** Error de lectura: No se pudo leer la localidad";
	private final static String RESULT_ECU_04_ReaderConnectionFailed_Text = "** Problemas de conexi�n con el lector. Verifique los par�metros de conexi�n";
	private final static String RESULT_ECU_05_ReaderConfigurationError_Text = "** Error en la configuraci�n del lector. Verifique los par�metros de conexi�n";
	private final static String RESULT_ECU_10_WrongDataFormat_Text = "** Formato de datos err�neo";
	private final static String RESULT_ECU_11_MemoryOverrun_Text = "** Capacidad de memoria excedida";
	private final static String RESULT_ECU_12_BlockedMemory_Text = "** Memoria bloqueada";
	private final static String RESULT_ECU_13_OffsetOutOfRange_Text = "** Offset fuera de rango [1-23]";
	private final static String RESULT_ECU_14_WrongTollingFormat_Text = "** Formato de telepeaje incorrecto";
	private final static String RESULT_ECU_15_WriteError_Text = "** Error de escritura: No se pudo escribir en la localidad";
	private final static String RESULT_ECU_16_LengthOutOfRange_Text = "** Longitud fuera de rango [1-23]";
	private final static String RESULT_ECU_17_WrongBNJFolioFormat_Text = "** Formato de folio BNJ incorrecto";
	private final static String RESULT_ECU_18_TollingWriteError_Text = "** Error al escribir telepeaje";
	private final static String RESULT_ECU_19_WrongPrefix_Text = "** Prefijo incorrecto";
	private final static String RESULT_ECU_20_NotPersonalizedTag_Text = "** El tag no se encuentra personalizado";
	private final static String RESULT_ECU_21_TagNotReadPreviously_Text = "** El tag no ha sido le�do previamente";
	private final static String RESULT_ECU_60_Exception_leerFolioTLP_Text = "** Excepci�n leerFolioTLP()";
	private final static String RESULT_ECU_61_Exception_leerUID_Text = "** Excepci�n leerUID()";
	private final static String RESULT_ECU_62_Exception_leerFolioBNJ_Text = "** Excepci�n leerFolioBNJ()";
	private final static String RESULT_ECU_63_Exception_leerChipBNJ_Text = "** Excepci�n leerChipBNJ(int offset, int length)";
	private final static String RESULT_ECU_64_Exception_grabarChipBNJ_Text = "** Excepci�n grabarChipBNJ(string Data, int offset, int length, Boolean bloqueo, string inprefijo)";
	private final static String RESULT_ECU_65_Exception_grabarChipBNJ_Text = "** Excepci�n grabarChipBNJ(string Data, int offset, int length, Boolean bloqueo)";
	private final static String RESULT_ECU__1_Unknown_Text = "** Error: Status Desconocido: ";

	public final static int MODO_INACTIVO = 0;
	public final static int MODO_LECTURA = 1;
	public final static int MODO_ESCRITURA = 2;

	private NfcAdapter _nfcAdapter;
	private PendingIntent _pendingIntent;
	private IntentFilter[] _intentFilters;
	private String[][] _techLists;

	private Activity _this;
	private String appMime = "com.neology.ecufuelrfid";
	private int counter = 0;

	// region Listeners
	protected Vector<NFC_Listener> _listeners;

	private boolean resultAvailable;

	String _folio = "";
	String _placa = "";
	String _subsidio = "";
	String _galonesMes = "";
	byte[] messagePayload = null;

	private boolean _sonDatosValidos = false;
	private boolean _yaProcesado = false;

	public boolean sonDatosValidos() {
		return this._sonDatosValidos;
	}

	private byte[] encodeData(int folio, String placa, int subsidio, int galonesEnteros, int galonesDecimales) {
		String dataFolio = String.format("%06X", folio & 0xFFFFFF);
		_folio = String.format("%08d", folio & 0xFFFFFF);
		String dataSubsidio = String.format("%02X", subsidio & 0xFF);
		String dataGalones = String.format("%04X", galonesEnteros & 0xFFFF)
				+ String.format("%02x", galonesDecimales & 0xFF);
		String dataPlaca = ASCIIAndHex.asciiToHex(placa);

		String data = dataFolio + dataSubsidio + dataGalones + dataPlaca;

		byte[] result = ASCIIAndHex.hexStringToByteArray(data);
		return result;
	}

	public boolean ingresarDatos(String folio, String placa, String subsidio, String galonesMes) {
		boolean validParameters = true;

		clearData();

		int iFolio = 0;
		int iSubsidio = 0;
		int iGalonesEnteros = 0;
		int iGalonesDecimales = 0;
		String sPlaca = "";

		try {
			iFolio = Integer.parseInt(folio);
		} catch (NumberFormatException ex) {
			validParameters = false;
			Log.e(TAG, "No es posible convertir el valor de folio a int - " + folio);
			sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
			sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
		}

		if (validParameters) {
			if (iFolio < 1 || iFolio > 0xFFFFFF) {
				validParameters = false;
				Log.e(TAG, "El valor de folio debe estar entre 1 y " + 0xFFFFFF);
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		if (validParameters) {
			if (placa.length() < 1 || placa.length() > 7) {
				validParameters = false;
				Log.e(TAG, "La longitud de la placa debe estar entre 1 y 7 caracteres ");
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		if (validParameters) {
			sPlaca = String.format("%1$-7s", placa);
		}

		try {
			iSubsidio = Integer.parseInt(subsidio);
		} catch (NumberFormatException ex) {
			validParameters = false;
			Log.e(TAG, "No es posible convertir el valor de subsidio a int - " + subsidio);
			sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
			sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
		}

		if (validParameters) {
			if (iSubsidio < 1 || iSubsidio > 0xFF) {
				validParameters = false;
				Log.e(TAG, "El valor de folio debe estar entre 1 y " + 0xFF);
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		String[] galones = galonesMes.replace('.', ' ').split(" ");
		if (galones.length == 0) {
			validParameters = false;
			Log.e(TAG, "El valor de galones debe tener el formato ####.## - " + galonesMes);
			sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
			sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
		} else if (galones.length == 1) {
			galones[1] = "0";
		} else if (galones.length == 2) {

		} else {
			validParameters = false;
			Log.e(TAG, "El valor de galones debe tener el formato ####.## - " + galonesMes);
			sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
			sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
		}

		if (validParameters) {
			try {
				iGalonesEnteros = Integer.parseInt(galones[0]);
			} catch (NumberFormatException ex) {
				validParameters = false;
				Log.e(TAG, "No es posible convertir el valor de galones (enteros) a int - " + iGalonesEnteros);
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		if (validParameters) {
			if (iGalonesEnteros < 1 || iGalonesEnteros > 0xFFFF) {
				validParameters = false;
				Log.e(TAG, "El valor de galones (enteros) debe estar entre 1 y " + 0xFFFF);
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		if (validParameters) {
			try {
				iGalonesDecimales = Integer.parseInt(galones[1]);
			} catch (NumberFormatException ex) {
				validParameters = false;
				Log.e(TAG, "No es posible convertir el valor de galones (decimales) a int - " + iGalonesDecimales);
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}

		if (validParameters) {
			if (iGalonesDecimales < 0 || iGalonesDecimales > 99) {
				validParameters = false;
				Log.e(TAG, "El valor de galones (decimales) debe estar entre 0 y 99");
				sendLogEvent(RESULT_ECU_10_WrongDataFormat_Text);
				sendNFCEvent(RESULT_ECU_10_WrongDataFormat);
			}
		}
		if (validParameters) {
			try {
				messagePayload = encodeData(iFolio, sPlaca, iSubsidio, iGalonesEnteros, iGalonesDecimales);
	
				if (messagePayload != null && messagePayload.length == 14)
					_sonDatosValidos = true;
				else
					_sonDatosValidos = false;
			} catch (Exception ex) {
				_sonDatosValidos = false;
			}
		}

		return _sonDatosValidos;
	}

	private int _modoOperacion = MODO_INACTIVO;

	public void modoEscritura() {
		if (_nfcAdapter != null) {
			_nfcAdapter = null;
		}

		_nfcAdapter = NfcAdapter.getDefaultAdapter(_this);
		if (_nfcAdapter != null) {
			_pendingIntent = PendingIntent.getActivity(_this, 0,
					new Intent(_this, _this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
			IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

			try {
				ndef.addDataType("*/*");
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("fail", e);
			}
			_intentFilters = new IntentFilter[] { discovery, ndef };
			_techLists = new String[][] { new String[] { NfcF.class.getName()// ,
					} };

			_modoOperacion = MODO_ESCRITURA;
			//sendNFCEvent("2|1");
			sendLogEvent("Modo escritura");
		} else {
			_modoOperacion = MODO_INACTIVO;
			//sendNFCEvent("2|-1");
			sendLogEvent("Adaptador NFC no inicializado");
		}
	}

	
	
	public void modoLectura() {
		if (_nfcAdapter != null) {
			_nfcAdapter = null;
		}

		_nfcAdapter = NfcAdapter.getDefaultAdapter(_this);
		if (_nfcAdapter != null) {
			_pendingIntent = PendingIntent.getActivity(_this, 0,
					new Intent(_this, _this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

			try {
				ndef.addDataType("app/" + appMime);
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("fail", e);
			}
			_intentFilters = new IntentFilter[] { ndef };
			_techLists = new String[][] { new String[] { NfcF.class.getName()
					} };

			_modoOperacion = MODO_LECTURA;
			//sendNFCEvent("2|0");
			sendLogEvent("Modo lectura");
		} else {
			_modoOperacion = MODO_INACTIVO;
			//sendNFCEvent("2|-1");
			sendLogEvent("Adaptador NFC no inicializado");
		}
	}
	
	public void modoInactivo() {
		if (_nfcAdapter != null) {
			_nfcAdapter = null;
		}

		_nfcAdapter = NfcAdapter.getDefaultAdapter(_this);
		if (_nfcAdapter != null) {
			_pendingIntent = PendingIntent.getActivity(_this, 0,
					new Intent(_this, _this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
			IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

			try {
				ndef.addDataType("*/*");
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("fail", e);
			}
			_intentFilters = new IntentFilter[] { discovery, ndef };
			_techLists = new String[][] { new String[] { NfcF.class.getName()// ,
					} };

			_modoOperacion = MODO_INACTIVO;
			//sendNFCEvent("2|0");
			sendLogEvent("Modo inactivo");
		} else {
			_modoOperacion = MODO_INACTIVO;
			//sendNFCEvent("2|-1");
			sendLogEvent("Adaptador NFC no inicializado");
		}
	}


	private void add_RFID_Listener(NFC_Listener listener) {
		if (_listeners == null)
			_listeners = new Vector<NFC_Listener>();

		_listeners.addElement(listener);
	}

	private void remove_RFID_Listeners() {
		if (_listeners != null)
			_listeners.clear();
	}
	// endregion Listeners

	public boolean Initialize(final WeakReference<Activity> ref, NFC_Listener nfc_list) {
		_this = ref.get();

		// Initializing listeners
		_listeners = new Vector<NFC_Listener>();
		add_RFID_Listener((NFC_Listener) nfc_list);

		sendLogEvent("Neology - AT911 NFC ECU v0.0.1 20151201");

		sendLogEvent("NFC inicializado");
		return true;
	}

	public void onPauseProcess() {
		Log.i(TAG, "onPauseProcess");
		if (_nfcAdapter != null) {
			_yaProcesado = true;
			_nfcAdapter.disableForegroundDispatch(_this);
		}
	}

	// region Send events methods
	private void sendNFCEvent(int data) {
		sendNFCEvent(data + "");
	}

	private void sendNFCEvent(String data) {
		Log.i(TAG, data);
		if (_listeners != null) {
			for (NFC_Listener l : _listeners)
				l.Event_NFC(data);
		}
	}

	private void sendLogEvent(String data) {
		Log.w(TAG, "LogEvent-" + data);
		if (_listeners != null) {
			for (NFC_Listener l : _listeners)
				l.Event_Log_NFC(data);
		}
	}
	// endregion Send events methods

	public void onResumeProcess() {
		Log.i(TAG, "onResumeProcess");
		if (_nfcAdapter != null) {
			_nfcAdapter.enableForegroundDispatch(_this, _pendingIntent, _intentFilters, _techLists);
			if (_modoOperacion == MODO_LECTURA) {
				if (_yaProcesado == false) {
					onNewIntentProcess(_this.getIntent());
				}
				_yaProcesado = false;
			}
		}
	}

	public void onNewIntentProcess(Intent intent) {
		Log.i(TAG, "New intent: " + intent);
		_this.setIntent(intent);

		if (_modoOperacion == MODO_LECTURA) {
			if (intent != null && intent.getAction() != null
					&& intent.getAction().equals("android.nfc.action.NDEF_DISCOVERED")) {
				try {
					readTag(intent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (_modoOperacion == MODO_ESCRITURA) {
			if (intent != null && intent.getAction() != null
					&& (intent.getAction().equals("android.nfc.action.TAG_DISCOVERED")
							|| intent.getAction().equals("android.nfc.action.NDEF_DISCOVERED"))) {
				if (_sonDatosValidos) {
					try {
						Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

						byte[] bUID = tag.getId();
						StringBuilder sbUID = new StringBuilder();
						for (byte b : bUID)
							sbUID.append(String.format("%02x", b & 0xFF));
						String sUID = sbUID.toString().toUpperCase(Locale.getDefault());

						writeTag(tag, messagePayload);

						sendLogEvent("Grabado con �xito");
						sendNFCEvent(RESULT_ECU_00_Success + "|5|" + sUID + "|" + _folio);
						clearData();
					} catch (IOException e) {
						sendLogEvent("Error al grabar, intente nuevamente");
						e.printStackTrace();
					} catch (FormatException e) {
						e.printStackTrace();
					}
				} else {
					sendLogEvent("ERROR - Valide los datos antes de grabar - Los datos no son v�lidos");
				}
			}
		}
		else if (_modoOperacion == MODO_INACTIVO) {
			sendLogEvent("Tag Detectado en modo inactivo.");
			return;
		}
		else {

		}
	}

	private void writeTag(Tag tag, byte[] payload) throws IOException, FormatException {
        byte[] key;
        byte[] keypbe;
        byte[] encryptedData = null;
        String encryptedString;
        String stringKey = secure.bin2hex(tag.getId()) + appMime;

        try {
			key = secure.getkey(stringKey);
			keypbe = secure.getkey(stringKey);
			encryptedData = secure.encrypt(key, payload);
			
			encryptedString = secure.bin2hex(encryptedData);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				new String("app/" + appMime).getBytes(Charset.forName("US-ASCII")), new byte[] {}, encryptedData);

		NdefRecord[] records = { relayRecord, NdefRecord.createApplicationRecord(appMime) };
		NdefMessage message = new NdefMessage(records);

		Ndef ndef = Ndef.get(tag);

		if (ndef != null) {
			Log.d(TAG, "NDEF!=null");
			ndef.connect();
			ndef.getTag();
			ndef.writeNdefMessage(message);
			ndef.close();
		} else {
			Log.d(TAG, "NDEF=null");
			NdefFormatable format = NdefFormatable.get(tag);
			if (format != null) {
				Log.d(TAG, "format=null");
				format.connect();
				format.getTag();
				format.format(message);
				format.close();
			}
		}
	}

	@SuppressLint("DefaultLocale")
	private void readTag(Intent intent) throws IOException {
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		byte[] bUID = tagFromIntent.getId();
		StringBuilder sbUID = new StringBuilder();
		for (byte b : bUID)
			sbUID.append(String.format("%02x", b & 0xFF));
		String sUID = sbUID.toString().toUpperCase(Locale.getDefault());

		NdefMessage[] ndefMessages = null;
		Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		if (rawMessages == null || rawMessages.length == 0) {
			Log.d(TAG, "Error while getting ndef messages");
			return;
		}

		ndefMessages = new NdefMessage[rawMessages.length];
		for (int x = 0; x < rawMessages.length; x++) {
			ndefMessages[x] = (NdefMessage) rawMessages[x];
			if (ndefMessages[x] == null) {
				Log.d(TAG, "ndefMessage == null, aborting read");
				return;
			}
		}

		byte[] bFolio = null;
		byte bSubsidy = 0x00;
		byte[] bGalons = null;
		byte[] bPlaca = null;

		byte[] payload = null;
		String mimeType = null;
		byte[] type = null;
		
		boolean result = false;
		for (NdefMessage x : ndefMessages) {
			for (int i = 0; i < x.getRecords().length; i++) {
				// Collecting data
				payload = x.getRecords()[i].getPayload();
				type = x.getRecords()[i].getType();
				mimeType = new String(type, "UTF-8");

				// Showing data in log
				Log.d(TAG, "MimeType: " + mimeType);
				Log.d(TAG, "Payload: " + ASCIIAndHex.byteArrayToHexString(payload).toUpperCase(Locale.getDefault()));

				if (mimeType != null && mimeType.equals("app/" + appMime) && payload.length == 16) {
					try {
						String datosLeidos = "";

						byte[] key2 = secure.getkey(secure.bin2hex(tagFromIntent.getId()) + appMime);
						byte[] decryptedData = secure.decrypt(key2, payload);
						
						bFolio = new byte[] { decryptedData[0], decryptedData[1], decryptedData[2] };
						bSubsidy = decryptedData[3];
						bGalons = new byte[] { decryptedData[4], decryptedData[5], decryptedData[6] };
						bPlaca = new byte[] { decryptedData[7], decryptedData[8], decryptedData[9], decryptedData[10], decryptedData[11], decryptedData[12],
								decryptedData[13] };
						
						result = true;
						break;
					} catch (Exception ex) {
						Log.d(TAG, "Error while parsing data");
						sendNFCEvent(RESULT_ECU_10_WrongDataFormat + "|6");
					}
				}
			}
			if (result)
				break;
		}

		if (result) {
			String iFolio = String.format("%08d", (bFolio[2] & 0xFF | (bFolio[1] & 0xFF) << 8 | (bFolio[0] & 0xFF) << 16));
			String iSubsidio = String.format("%d", bSubsidy & 0xFF);
			String iGalons = String.format("%d", (bGalons[1] & 0xFF | ((bGalons[0] & 0xFF) << 8))) + "."
					+ String.format("%02d", (bGalons[2] & 0xFF));
			String sPlaca = new String(bPlaca);
		
			sendLogEvent("Lectura exitosa");
			sendNFCEvent("0|6|" + sUID + "|" + iFolio + "|" + iSubsidio + "|" + iGalons + "|" + sPlaca);
		} else {
			sendLogEvent("Error al leer");
			sendNFCEvent(RESULT_ECU_10_WrongDataFormat + "|6|" + sUID);
		}
	}

	private void clearData() {
		_folio = "";
		_galonesMes = "";
		_placa = "";
		_subsidio = "";

		messagePayload = null;
		_sonDatosValidos = false;
	}
}
