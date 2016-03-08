package com.neology.ecurfid;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import com.atid.lib.dev.ATRfidManager;
import com.atid.lib.dev.ATRfidReader;
import com.atid.lib.dev.event.RfidReaderEventListener;
import com.atid.lib.dev.rfid.param.SimpleSelectionMask;
import com.atid.lib.dev.rfid.type.BankType;
import com.atid.lib.dev.rfid.type.MaskActionType;
import com.atid.lib.dev.rfid.type.ReaderState;
import com.atid.lib.dev.rfid.type.ResultCode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;


public class RFID implements RfidReaderEventListener {
	// region Field Variables and Constants
	private final static String TAG = "ECU_UHF";
	
	private final static int STATUS_NOTINITIALIZED = -1;
	private final static int STATUS_IDLE = 0;

	// prePersoTag(
	private final static int PP_READ_EPC = 1;
	private final static int PP_READ_TID = 2;
	private final static int PP_WRITE_EPC = 3;

	// persoTag(
	private final static int P_READ_EPC = 6;
	private final static int P_READ_TID = 7;
	private final static int P_WRITE_CONSUMO = 8;
	private final static int P_WRITE_USER = 9;

	// leerTag(
	private final static int L_EPC = 11;
	private final static int L_TID = 12;
	private final static int L_USER = 13;

	// grabarConsumo(
	private final static int G_READ_EPC = 16;
	private final static int G_READ_TID = 17;
	private final static int G_READ_USER = 18;
	private final static int G_WRITE_USER = 19;

	// test_leeEPC(
	private final static int T_LEPC_READ_EPC = 21;

	// test_leeTID(
	private final static int T_LTID_READ_EPC = 26;
	private final static int T_LTID_READ_TID = 27;

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
	private final static int RESULT_ECU_17_WrongECUFolioFormat = 17;
	private final static int RESULT_ECU_18_TollingWriteError = 18;
	private final static int RESULT_ECU_19_WrongPrefix = 19;
	private final static int RESULT_ECU_20_NotPersonalizedTag = 20;
	private final static int RESULT_ECU_21_TagNotReadPreviously = 21;
	private final static int RESULT_ECU_61_Exception_prePersoTag = 61;
	private final static int RESULT_ECU_62_Exception_persoTag = 62;
	private final static int RESULT_ECU_63_Exception_leerTag = 63;
	private final static int RESULT_ECU_64_Exception_grabarConsumo = 64;
	
	private final static int RESULT_ECU_101_PrePerso = 1;
	private final static int RESULT_ECU_102_Perso = 2;
	private final static int RESULT_ECU_103_LeerTag = 3;
	private final static int RESULT_ECU_104_GrabarConsumo = 4;
	
	private static int CURRENT_FUNTION = 0;
	
	
	
	

	private final static int RESULT_ECU__1_Unknown = -1;

	private final static String RESULT_ECU_00_Success_Text = "** Operación exitosa";
	private final static String RESULT_ECU_01_NoTagInTheField_Text = "** No hay tag en el campo de lectura";
	private final static String RESULT_ECU_02_MoreThan1Tag_Text = "** Más de 1 tag en el campo de lectura";
	private final static String RESULT_ECU_03_CannotReadBlock_Text = "** Error de lectura: No se pudo leer la localidad";
	private final static String RESULT_ECU_04_ReaderConnectionFailed_Text = "** Problemas de conexión con el lector. Verifique los parámetros de conexión";
	private final static String RESULT_ECU_05_ReaderConfigurationError_Text = "** Error en la configuración del lector. Verifique los parámetros de conexión";
	private final static String RESULT_ECU_10_WrongDataFormat_Text = "** Formato de datos erróneo";
	private final static String RESULT_ECU_11_MemoryOverrun_Text = "** Capacidad de memoria excedida";
	private final static String RESULT_ECU_12_BlockedMemory_Text = "** Memoria bloqueada";
	private final static String RESULT_ECU_13_OffsetOutOfRange_Text = "** Offset fuera de rango [1-23]";
	private final static String RESULT_ECU_14_WrongTollingFormat_Text = "** Formato de telepeaje incorrecto";
	private final static String RESULT_ECU_15_WriteError_Text = "** Error de escritura: No se pudo escribir en la localidad";
	private final static String RESULT_ECU_16_LengthOutOfRange_Text = "** Longitud fuera de rango [1-23]";
	private final static String RESULT_ECU_17_WrongECUFolioFormat_Text = "** Formato de folio ECU incorrecto";
	private final static String RESULT_ECU_18_TollingWriteError_Text = "** Error al escribir telepeaje";
	private final static String RESULT_ECU_19_WrongPrefix_Text = "** Prefijo incorrecto";
	private final static String RESULT_ECU_20_NotPersonalizedTag_Text = "** El tag no se encuentra personalizado";
	private final static String RESULT_ECU_21_TagNotReadPreviously_Text = "** El tag no ha sido leído previamente";
	private final static String RESULT_ECU_61_Exception_prePersoTag_Text = "** Excepción prePersoTag(";
	private final static String RESULT_ECU_62_Exception_persoTag_Text = "** Excepción persoTag(";
	private final static String RESULT_ECU_63_Exception_leerTag_Text = "** Excepción leerTag(";
	private final static String RESULT_ECU_64_Exception_grabarConsumo_Text = "** Excepción grabarConsumo(";
	private final static String RESULT_ECU__1_Unknown_Text = "** Error: Status Desconocido: ";

	// prePersoTag(
	private String _PP_EPC = "";
	private String _PP_TID = "";
	private String _PP_Folio = "";

	// persoTag(
	private String _P_EPC = "";
	private String _P_TID = "";
	private String _P_Folio = "";
	private String _P_Dato = "";
	private String _P_Dato2 = "";

	// leerTag(
	private String _L_EPC = "";
	private String _L_TID = "";
	private String _L_USER = "";
	private String _L_Folio = "";
	private int _L_USER_Count = 0;

	// grabarConsumo(
	private String _G_EPC = "";
	private String _G_TID = "";
	private String _G_USER = "";
	private String _G_Folio = "";
	private String _G_Consumo = "";
	private String _G_Dato = "";

	private static String _last_EPC = "";

	private Timer _timer_Timeout = null;
	private TimerTask _timerTask_Timeout = null;
	private Timer _timer_StartTask = null;
	private TimerTask _timerTask_StartTask = null;

	private int _timeoutCurrent = 0;
	private int _timeout = 2500;
	private int _power = 15;

	private ATRfidReader _reader = null;
	private boolean _readerConfigured = false;
	private boolean _readerOccupied = false;
	private boolean _restartingReader = false;
	private static boolean _initializedBefore = false;

	private boolean _writting = false;
	private int _retryNumber = 0;
	private boolean _waitingForStop = false;

	private int _status = STATUS_NOTINITIALIZED;

	private boolean _tagNotFound = false;
	private SimpleSelectionMask _mask;

	// endregion	
	
	// region Getters and Setters
	public int getTimeout() {
		return this._timeout;
	}

	public void setTimeout(int timeout) {
		if (timeout < 1300 || timeout > 10000) {
			sendLogEvent("ECU-El valor de timeout debe estar entre 1300 y 10000");
			sendRFIDEvent(RESULT_ECU_05_ReaderConfigurationError);
			sendLogEvent(RESULT_ECU_05_ReaderConfigurationError_Text);
			this._timeout = 2500;
		} else {
			this._timeout = timeout;
		}
	}

	public int getPower() {
		return this._power;
	}

	public void setPower(int power) {
		if (power < 0 || power > 20) {
			sendLogEvent("ECU-El valor de power debe estar entre 0 y 20");
			sendRFIDEvent(RESULT_ECU_05_ReaderConfigurationError);
			sendLogEvent(RESULT_ECU_05_ReaderConfigurationError_Text);
			this._power = 0;
		} else {
			this._power = power;
		}
	}
	// endregion Getters and Setters

	// region Listeners
	protected Vector<RFID_Listener> _listeners;

	private boolean resultAvailable;

	private void add_RFID_Listener(RFID_Listener listener) {
		if (_listeners == null)
			_listeners = new Vector<RFID_Listener>();
		_listeners.addElement(listener);
	}

	private void remove_RFID_Listeners() {
		if (_listeners != null)
			_listeners.clear();
	}
	// endregion Listeners

	// region Constructor and Destructor

	public boolean Initialize(/*Activity toAdd, */RFID_Listener rfListener) {
		KillTimer_ReadTimeout();

		if (_readerOccupied || _readerConfigured || _reader != null) {
			try {
				remove_RFID_Listeners();
				_reader.destroy();
				ATRfidManager.onDestroy();

				_readerConfigured = false;
				_readerOccupied = false;
				_reader = null;

				Thread.sleep(100);
			} catch (InterruptedException e) {

			} catch (Exception e) {

			} finally {
				_reader = null;
				_readerOccupied = false;
				_readerConfigured = false;
			}
		}

		if ((this._reader = ATRfidManager.getInstance()) == null) {
			_status = STATUS_NOTINITIALIZED;
			_readerConfigured = false;

			Log.e(TAG, "Error al obtener instancia del lector RFID. Reinicie el equipo.");
			sendLogEvent(RESULT_ECU_04_ReaderConnectionFailed_Text);
			sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed);
		} else {
			// Initializing listeners
			_listeners = new Vector<RFID_Listener>();
			add_RFID_Listener((RFID_Listener) rfListener);
			_reader.setEventListener(this);

			// Minimum power setting
			// 20 -> Minimum
			// 0 -> Maximum
			_reader.setPower(this._power);

			// Sending events
			if (!_initializedBefore) {
				sendLogEvent("Neology - AT911 RFID ECU v0.0.1 20151125");
				_initializedBefore = true;

				String firmwareVersion = _reader.getFirmewareVersion();
				sendLogEvent("FW RFID v" + firmwareVersion);
				if (!firmwareVersion.equalsIgnoreCase("R15033100")) {
					_status = STATUS_NOTINITIALIZED;
					_readerConfigured = false;

					Log.e(TAG, "Versi�n de FW RFID incorrecta. Por favor actualice el FW RFID.");
					sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed);
					sendLogEvent(RESULT_ECU_04_ReaderConnectionFailed_Text);
					return false;
				}
			}

			// Initializing mask
			_mask = new SimpleSelectionMask();
			_readerConfigured = true;
			_tagNotFound = false;
			_writting = false;

			_status = STATUS_IDLE;
			sendLogEvent("RFID disponible");
		}

		return _readerConfigured;
	}

	public void Destroy() {
		KillTimer_ReadTimeout();
		remove_RFID_Listeners();
		setStatus(STATUS_NOTINITIALIZED);
		if (_reader != null) {
			try {
				_reader.disconnect();
			} catch (Exception ex) {
				Log.e(TAG, "Destroy: " + ex.getMessage());
			}
		}
		_reader = null;
		ATRfidManager.onDestroy();
	}
	// endregion Constructor and Destructor

	private void setStatus(int status) {
		this._status = status;

		switch (_status) {
		case STATUS_NOTINITIALIZED:
			sendLogEvent("RFID-No Inicializado");
			break;
		case STATUS_IDLE:
			break;

		// prePerso(
		case PP_READ_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("prePerso-Leyendo EPC...");
			break;
		case PP_READ_TID:
			_timeoutCurrent = _timeout;
			sendLogEvent("prePerso-Leyendo TID...");
			break;
		case PP_WRITE_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("prePerso-Escribiendo EPC...");
			break;

		// persoTag(
		case P_READ_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("perso-Leyendo EPC...");
			break;

		case P_READ_TID:
			_timeoutCurrent = _timeout;
			sendLogEvent("perso-Leyendo TID...");
			break;

		case P_WRITE_CONSUMO:
			_timeoutCurrent = _timeout;
			sendLogEvent("P_WRITE_CONSUMO: Escribiendo CONSUMO EN USER...");
			break;

		case P_WRITE_USER:
			_timeoutCurrent = _timeout;
			sendLogEvent("perso-Escribiendo USER...");
			break;

		// leer_Tag
		case L_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("leerTag-Leyendo EPC...");
			break;
		case L_TID:
			_timeoutCurrent = _timeout;
			sendLogEvent("leerTag-Leyendo TID...");
			break;
		case L_USER:
			_timeoutCurrent = _timeout;
			sendLogEvent("leerTag-Leyendo USER...");
			break;

		// grabarConsumo(
		case G_READ_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("grabarConsumo-Leyendo EPC...");
			break;
		case G_READ_TID:
			_timeoutCurrent = _timeout;
			sendLogEvent("grabarConsumo-Leyendo TID...");
			break;
		case G_READ_USER:
			_timeoutCurrent = _timeout;
			sendLogEvent("grabarConsumo-Leyendo USER...");
			break;
		case G_WRITE_USER:
			_timeoutCurrent = _timeout;
			sendLogEvent("grabarConsumo-Escribiendo USER...");
			break;

		// test_leeEPC(
		case T_LEPC_READ_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("test_leerEPC-Leyendo EPC...");
			break;

		// test_leeTID
		case T_LTID_READ_EPC:
			_timeoutCurrent = _timeout;
			sendLogEvent("test_LeerTID-Leyendo EPC...");
			break;
		case T_LTID_READ_TID:
			_timeoutCurrent = _timeout;
			sendLogEvent("test_LeerTID-Leyendo TID...");
			break;

		default:
			sendLogEvent("setStatus-N/A");
			break;
		}

		// Reset previous timer if it exists
		KillTimer_ReadTimeout();
		if (!(_status == STATUS_IDLE || _status == STATUS_NOTINITIALIZED)) {
			// Start a new timer when timeout is reached
			_timerTask_Timeout = new TimerTask() {
				@Override
				public void run() {
					onOperationTimeout();
				}
			};
			_timer_Timeout = new Timer();
			_timer_Timeout.schedule(_timerTask_Timeout, _timeoutCurrent);
		} else if (_status == STATUS_IDLE) {
			// Put the reader in IDLE
			if (_reader != null) {
				_reader.stop();
			}

			if (!_restartingReader && !WaitForStop()) {
				_status = STATUS_NOTINITIALIZED;
				_readerConfigured = false;
			}

			if (_tagNotFound) {
				_tagNotFound = false;
				RFID._last_EPC = "";
				sendRFIDEvent(RESULT_ECU_01_NoTagInTheField  +"|" + CURRENT_FUNTION);
				sendLogEvent(RESULT_ECU_01_NoTagInTheField_Text +"|" + CURRENT_FUNTION);
			}

			if (_readerConfigured)
				sendLogEvent("RFID disponible");
		}
	}

	private void KillTimer_ReadTimeout() {
		if (_timerTask_Timeout != null) {
			_timerTask_Timeout.cancel();
		}
		if (_timer_Timeout != null) {
			_timer_Timeout.purge();
			_timer_Timeout.cancel();
		}
		_timerTask_Timeout = null;
		_timer_Timeout = null;
	}

	private void onOperationTimeout() {
		Log.d(TAG, "Operation timeout");
		if (_writting) {
			Log.d(TAG, "W_Operation timeout " + _retryNumber);
			if (_retryNumber >= 3) {
				_tagNotFound = true;

				setStatus(STATUS_IDLE);
			} else {
				_retryNumber++;
				_waitingForStop = true;
				_reader.stop();
				WaitForStop();
				this._status = STATUS_IDLE;
				_waitingForStop = false;
				
				_timer_StartTask = new Timer();
				_timer_StartTask.schedule(_timerTask_StartTask, 100);

				Log.w(TAG, "R_Operation timeout-TimerStartTask created");
			}
		} else {
			if (this._status == L_EPC) {

				Log.d(TAG, "R_Operation timeout " + _retryNumber);
				if (_retryNumber >= 3) {
					_tagNotFound = true;

					setStatus(STATUS_IDLE);
				} else {
					_retryNumber++;

					_waitingForStop = true;
					if (_reader.getState().equals(ReaderState.Stop)) {
						onReaderStateChanged(ReaderState.Stop);
					} else {
						_reader.stop();
					}
					WaitForStop();
					this._status = STATUS_IDLE;
					_waitingForStop = false;

					
					_timer_StartTask = new Timer();
					_timer_StartTask.schedule(_timerTask_StartTask, 50);

					Log.w(TAG, "R_Operation timeout-TimerStartTask created");
				}
			} else {
				_tagNotFound = true;
				setStatus(STATUS_IDLE);
			}
		}
	}

	private boolean WaitForStop() {
		// Waits for the reader to stop completely
		int i = 0;
		ReaderState rs;
		boolean result = true;
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (_reader != null && (rs = _reader.getState()) != ReaderState.Stop) {
			Log.w(TAG, "Waiting... " + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			if (i >= 3) {
				sendLogEvent("Reiniciando lector... " + rs);
				_restartingReader = true;
				result = false;
				break;
			}
		}
		_readerOccupied = !result;
		return result;
	}
	// endregion setStatus, timers management, stop and retry

	// region Send events methods
	private void sendRFIDEvent(int data) {
		sendRFIDEvent(data + "");
	}

	private void sendRFIDEvent(String data) {
		Log.i(TAG, data);
		if (_listeners != null) {
			for (RFID_Listener l : _listeners)
				l.Event_RFID(data);
		}
	}

	private void sendLogEvent(String data) {
		Log.w(TAG, _status + "-LogEvent-" + data);
		if (_listeners != null) {
			for (RFID_Listener l : _listeners)
				l.Event_Log(data);
		}
	}
	// endregion Send events methods

	/***** Public methods ***************************************************/
	public boolean prePersoTag(String folio) {
		CURRENT_FUNTION = RESULT_ECU_101_PrePerso;
		if (!_readerConfigured) {
			sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed + "|" + CURRENT_FUNTION); 
			return false;
		}

		if (folio.equals("")) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION); 
			return false;
		}

		
		int iFolio;
		try {
			iFolio = Integer.parseInt(folio);
			if (iFolio < 1 || iFolio > 17777215) {
				sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION); 
				return false;
			}
		} catch (Exception e) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION); 
			return false;
		}

		boolean result = false;
		int iTimeout = this._timeout;

		if (_readerOccupied == false) {
			_readerOccupied = true;
			sendLogEvent("RFID ocupado");

			if (iTimeout < 1300 || iTimeout > 10000) {
				sendRFIDEvent(RESULT_ECU_61_Exception_prePersoTag+ "|" + CURRENT_FUNTION); 
			} else {
				_tagNotFound = true;
				_writting = false;
				_retryNumber = 0;
				_timeout = iTimeout;

				_PP_EPC = "";
				_PP_TID = "";
				_PP_Folio = folio;

				if (_readerConfigured && _status == STATUS_IDLE && _reader != null) {
					result = _reader.readEpc6cTag();
				}

				if (result) {
					setStatus(PP_READ_EPC);
				} else {
					sendRFIDEvent(RESULT_ECU_61_Exception_prePersoTag + "|" + CURRENT_FUNTION); 
					_tagNotFound = false;
					setStatus(STATUS_IDLE);
				}
			}
		}
		return result;
	}

	@SuppressLint("DefaultLocale")
	public boolean persoTag(String folio, String Placa, String sVin, String TipoV, String TypeCombus, String Subsidio,
			String Galones, String FechaRec, String GalonesConsu) {
		CURRENT_FUNTION = RESULT_ECU_102_Perso;
		if (!_readerConfigured) {
			sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed  + "|" + CURRENT_FUNTION);
			return false;
		}

		if (folio.equals("")) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat  + "|" + CURRENT_FUNTION);
			return false;
		}
		
		try {
			int iFolio = Integer.parseInt(folio);

			String[] SpGalones = Galones.split("\\.");
			int uGalones = Integer.parseInt(SpGalones[0]);

			String DecimGalones = SpGalones[1];
			if (DecimGalones.length() > 2)
				DecimGalones = DecimGalones.substring(0, 2);
			else if (DecimGalones.length() < 2)
				DecimGalones = "0" + DecimGalones;
			int dGalones = Integer.parseInt(DecimGalones);

			int iDia = Integer.parseInt(FechaRec.substring(0, 2));
			int iMes = Integer.parseInt(FechaRec.substring(2, 4));
			int iYear = Integer.parseInt(FechaRec.substring(4, 8));

			String[] SpGalonesConsu = GalonesConsu.split("\\.");
			int uGalonesCon = Integer.parseInt(SpGalonesConsu[0]);

			String DecimGalonesCon = SpGalonesConsu[1];
			if (DecimGalonesCon.length() > 2)
				DecimGalonesCon = DecimGalonesCon.substring(0, 2);
			else if (DecimGalonesCon.length() < 2)
				DecimGalonesCon = "0" + DecimGalonesCon;
			int dGalonesCon = Integer.parseInt(DecimGalonesCon);

			if (iFolio < 1 || iFolio > 16777215 || Placa.length() > 7 || TypeCombus.length() > 1
					|| Subsidio.length() > 1 || TipoV.length() > 1 || sVin.length() > 17 || uGalones > 65535
					|| FechaRec.length() < 8 || iDia > 31 || iMes > 12 || iYear > 65535 || uGalonesCon > 65535) {
				
				sendRFIDEvent(RESULT_ECU_10_WrongDataFormat  + "|" + CURRENT_FUNTION);
				return false;
			}

			_P_Dato = String.format("%14s", StringToHex(Placa))
					.replace(' ',
							'0')
					+ String.format("%2s", StringToHex(TypeCombus)).replace(' ', '0')
					+ String.format("%4s", Integer.toHexString(uGalones)).replace(' ', '0')
					+ String.format("%2s", Integer.toHexString(dGalones)).replace(' ', '0')
					+ String.format("%2s", StringToHex(Subsidio)).replace(' ', '0')
					+ String.format("%2s", StringToHex(TipoV)).replace(' ', '0')
					+ String.format("%34s", StringToHex(sVin)).replace(' ', '0')
					+ String.format("%4s", "").replace(' ',
							'0')
					+ String.format("%2s", Integer.toHexString(iDia))
							.replace(' ',
									'0')
					+ String.format("%2s", Integer.toHexString(iMes)).replace(' ', '0')
					+ String.format("%4s", Integer.toHexString(iYear)).replace(' ', '0')
					+ String.format("%4s", Integer.toHexString(uGalonesCon)).replace(' ', '0')
					+ String.format("%2s", Integer.toHexString(dGalonesCon)).replace(' ', '0') + "000000";
			
			_P_Dato = _P_Dato.toUpperCase();
			Log.i(TAG, "_P_Dato: " + _P_Dato);

		} catch (Exception e) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat  + "|" + CURRENT_FUNTION);
			return false;
		}

		boolean result = false;
		int iTimeout = this._timeout;

		if (_readerOccupied == false) {
			_readerOccupied = true;
			sendLogEvent("RFID ocupado");

			if (iTimeout < 1300 || iTimeout > 10000) {
				sendRFIDEvent(RESULT_ECU_62_Exception_persoTag  + "|" + CURRENT_FUNTION);
			} else {
				_tagNotFound = true;
				_writting = false;
				_retryNumber = 0;
				_timeout = iTimeout;

				_P_EPC = "";
				_P_TID = "";
				_P_Folio = folio;

				if (_readerConfigured && _status == STATUS_IDLE && _reader != null) {
					result = _reader.readEpc6cTag();
				}
				if (result) {
					setStatus(P_READ_EPC);
				} else {
					sendRFIDEvent(RESULT_ECU_62_Exception_persoTag  + "|" + CURRENT_FUNTION);
					_tagNotFound = false;
					setStatus(STATUS_IDLE);
				}
			}
		}
		return result;
	}

	@SuppressLint("DefaultLocale")
	public boolean grabarConsumo(String folio, String FechaRec, String Galones) {
		CURRENT_FUNTION = RESULT_ECU_104_GrabarConsumo;
		if (!_readerConfigured) {
			sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed  + "|" + CURRENT_FUNTION);
			return false;
		}

		if (folio.equals("")) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat  + "|" + CURRENT_FUNTION);
			return false;
		}
		
		int iFolio;
		try {

			int iDia = Integer.parseInt(FechaRec.substring(0, 2));
			int iMes = Integer.parseInt(FechaRec.substring(2, 4));
			int iYear = Integer.parseInt(FechaRec.substring(4, 8));

			String[] SpGalones = Galones.split("\\.");
			int uGalones = Integer.parseInt(SpGalones[0]);

			String DecimGalones = SpGalones[1];
			if (DecimGalones.length() > 2)
				DecimGalones = DecimGalones.substring(0, 2);
			else if (DecimGalones.length() < 2)
				DecimGalones = "0" + DecimGalones;
			int dGalones = Integer.parseInt(DecimGalones);

			iFolio = Integer.parseInt(folio);
			if (iFolio < 1 || iFolio > 17777215 || uGalones > 65535 || FechaRec.length() < 8 || iDia > 31 || iMes > 12
					|| iYear > 65535) {
				sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION);
				return false;
			}

			_G_Dato = String.format("%2s", Integer.toHexString(iDia)).replace(' ', '0')
					+ String.format("%2s", Integer.toHexString(iMes)).replace(' ', '0')
					+ String.format("%4s", Integer.toHexString(iYear)).replace(' ', '0')
					+ String.format("%4s", Integer.toHexString(uGalones)).replace(' ', '0')
					+ String.format("%2s", Integer.toHexString(dGalones)).replace(' ', '0') + "00";
			_G_Dato = _G_Dato.toUpperCase();
		} catch (Exception e) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat  + "|" + CURRENT_FUNTION);
			return false;
		}

		boolean result = false;
		int iTimeout = this._timeout;

		if (_readerOccupied == false) {
			_readerOccupied = true;
			sendLogEvent("RFID ocupado");

			if (iTimeout < 1300 || iTimeout > 10000) {
				sendRFIDEvent(RESULT_ECU_64_Exception_grabarConsumo  + "|" + CURRENT_FUNTION);
			} else {
				_tagNotFound = true;
				_writting = false;
				_retryNumber = 0;
				_timeout = iTimeout;

				_G_EPC = "";
				_G_TID = "";
				_G_Folio = folio;

				if (_readerConfigured && _status == STATUS_IDLE && _reader != null) {
					result = _reader.readEpc6cTag();
				}
				if (result) {
					setStatus(G_READ_EPC);
				} else {
					sendRFIDEvent(RESULT_ECU_64_Exception_grabarConsumo  + "|" + CURRENT_FUNTION);
					_tagNotFound = false;
					setStatus(STATUS_IDLE);
				}
			}
		}
		return result;
	}

	public boolean leerTag(String folio) {
		CURRENT_FUNTION = RESULT_ECU_103_LeerTag;
		if (!_readerConfigured) {
			sendRFIDEvent(RESULT_ECU_04_ReaderConnectionFailed + "|" + CURRENT_FUNTION);
			return false;
		}
	
		int iFolio;
		try {
			iFolio = Integer.parseInt(folio);
			if (iFolio < 1 || iFolio > 17777215) {
				sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION);
				return false;
			}
		} catch (Exception e) {
			sendRFIDEvent(RESULT_ECU_10_WrongDataFormat + "|" + CURRENT_FUNTION);
			return false;
		}
		boolean result = false;
		int iTimeout = this._timeout;

		if (_readerOccupied == false) {
			_readerOccupied = true;
			sendLogEvent("RFID ocupado");

			if (iTimeout < 1300 || iTimeout > 10000) {
				sendRFIDEvent(RESULT_ECU_63_Exception_leerTag  + "|" + CURRENT_FUNTION);
			} else {
				_tagNotFound = true;
				_writting = false;
				_retryNumber = 0;
				_timeout = iTimeout;

				_L_EPC = "";
				_L_TID = "";
				_L_USER = "";
				_L_Folio = folio;

				if (_readerConfigured && _status == STATUS_IDLE && _reader != null) {
					result = _reader.readEpc6cTag();
				}
				if (result) {
					setStatus(L_EPC);
				} else {
					sendRFIDEvent(RESULT_ECU_63_Exception_leerTag + "|" + CURRENT_FUNTION);
					_tagNotFound = false;
					setStatus(STATUS_IDLE);
				}
			}
		}
		return result;
	}

	// region M�todos privados

	////////////////////////////////////////////////
	// Reader Interface Methods //
	////////////////////////////////////////////////
	@Override
	public void onReaderDetectTag(String arg0) {
		// Log.i(TAG, _status + "-onReaderDetectTag(" + arg0 + ")");

		switch (_status) {
		////////////////////////////////////////////////
		// prePersoTag
		////////////////////////////////////////////////

		case PP_READ_EPC:
			_PP_EPC = arg0;
			_mask = new SimpleSelectionMask();
			_mask.setBank(BankType.EPC);
			_mask.setOffset(32);
			_mask.setMask(_PP_EPC.substring(4, _PP_EPC.length()));
			_mask.setAction(MaskActionType.Match);
			Log.i(TAG, "PP_READ_EPC-" + _PP_EPC);
			break;

		case PP_READ_TID:
			Log.i(TAG, "PP_READ_EPC-" + _PP_EPC);
			_PP_TID = arg0;
			break;

		case PP_WRITE_EPC:
			break;

		// persoTag( states
		case P_READ_EPC:
			_P_EPC = arg0;
			_mask = new SimpleSelectionMask();
			_mask.setBank(BankType.EPC);
			_mask.setOffset(32);
			_mask.setMask(_P_EPC.substring(4, _P_EPC.length()));
			_mask.setAction(MaskActionType.Match);
			Log.i(TAG, "P_READ_EPC-" + _P_EPC);
			break;

		case P_READ_TID:
			_P_TID = arg0;
			Log.i(TAG, "P_READ_TID-" + _P_TID);
			break;

		case P_WRITE_USER:
			Log.i(TAG, "P_WRITE_USER- Regresa.....");
			break;

		case P_WRITE_CONSUMO:
			Log.i(TAG, "P_WRITE_CONSUMO- Regresa.....");
			break;

		// leerTag( states
		case L_EPC:
			_L_EPC = arg0;
			_mask.setBank(BankType.EPC);
			_mask.setOffset(32);
			_mask.setMask(_L_EPC.substring(4, _L_EPC.length()));
			_mask.setAction(MaskActionType.Match);
			Log.i(TAG, "_L_EPC-" + _L_EPC);
			break;

		case L_TID:
			_L_TID = arg0;
			Log.i(TAG, "_L_TID-" + _L_TID);
			break;

		case L_USER:
			_L_USER = arg0;
			Log.i(TAG, "_L_USER-" + _L_USER);
			break;

		// grabarConsumo( states
		case G_READ_EPC:
			_G_EPC = arg0;
			_mask = new SimpleSelectionMask();
			_mask.setBank(BankType.EPC);
			_mask.setOffset(32);
			_mask.setMask(_G_EPC.substring(4, _G_EPC.length()));
			_mask.setAction(MaskActionType.Match);
			Log.i(TAG, "G_READ_EPC-" + _G_EPC);
			break;

		case G_READ_TID:
			_G_TID = arg0;
			Log.i(TAG, "G_READ_TID-" + _G_TID);
			break;

		case G_WRITE_USER:
			break;

		case STATUS_NOTINITIALIZED:
			Log.e(TAG, "onReaderDetectTag-STATUS_NOTINITIALIZED-Not supposed to be here");
			break;
		case STATUS_IDLE:
			Log.e(TAG, "onReaderDetectTag-STATUS_IDLE-Not supposed to be here");
			break;
		default:

			break;
		// endregion Not valid status
		}
	}

	int blockECU = 0;

	private boolean writeECUBlock() {
		try {
			Log.i(TAG, "writeECUBlock: Metodo escritura");
			boolean result = false;
			resultAvailable = false;
			if (blockECU < 3) {
				String DataToWrite = _P_Dato.substring(0 + 28 * blockECU, 28 + 28 * blockECU);
				result = _reader.writeMemory(BankType.User, 0 + 7 * blockECU, DataToWrite, (SimpleSelectionMask) _mask);
				Thread.sleep(250);
				if (result) {
					setStatus(P_WRITE_USER);
					blockECU++;
				} else {
					setStatus(STATUS_IDLE);
				}
			} else {
				setStatus(STATUS_IDLE);
				result = false;
			}
			return result;
		} catch (Exception ex) {
			return false;
		}
	}

	////////////////////////////////////////////////
	// Reader State Changed //
	////////////////////////////////////////////////
	@Override
	public void onReaderStateChanged(ReaderState arg0) {
		Log.i(TAG, _status + "-onReaderStateChanged(" + arg0.toString() + ")");

		if (arg0 == ReaderState.Stop) {
			switch (_status) {

			//////////////////////////////////////////////
			// prePersoTag
			//////////////////////////////////////////////
			case PP_READ_EPC:
				if (_reader.readMemory6c(BankType.TID, 0, 6, (SimpleSelectionMask) _mask)) {
					setStatus(PP_READ_TID);
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case PP_READ_TID:
				if (!_PP_TID.equals("")) {
					String sEPC = CalculateFolio(_PP_TID, _PP_Folio);
					if (_reader.writeMemory(BankType.EPC, 2, sEPC, (SimpleSelectionMask) _mask)) {
						setStatus(PP_WRITE_EPC);
					} else {
						setStatus(STATUS_IDLE);
					}
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case PP_WRITE_EPC:
				if (_waitingForStop == false) {
					if (resultAvailable == false) {
						Log.d(TAG, "Waiting for result...");
					} else {
						sendRFIDEvent(RESULT_ECU_00_Success + "|" +  CURRENT_FUNTION +"|" +  _PP_EPC.substring(4, _PP_EPC.length()) +"|" + _PP_TID);
						_tagNotFound = false;
						_writting = true;
						setStatus(STATUS_IDLE);
					}
				} else {
					Log.w(TAG, "READ TAG stopped");
				}
				break;

			//////////////////////////////////////////////
			// persoTag
			//////////////////////////////////////////////
			case P_READ_EPC:
				if (_reader.readMemory6c(BankType.TID, 0, 6, (SimpleSelectionMask) _mask)) {
					setStatus(P_READ_TID);
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case P_READ_TID:
				try {
					Log.i(TAG, "P_READ_TID");
					if (!_P_TID.equals("")) {
						String sEPC = CalculateFolio(_P_TID, _P_Folio);
						Log.i(TAG, "Dato a Grabar:" + _P_Dato);
						Log.i(TAG, "EPC calculado: " + sEPC + " EPC Normal: " + _P_EPC.substring(4, _P_EPC.length()));
						if (_P_EPC.substring(4, _P_EPC.length()).equals(sEPC)) {
							writeECUBlock();
						} else {
							setStatus(STATUS_IDLE);
						}
					} else {
						setStatus(STATUS_IDLE);
					}
				} catch (Exception e) {
					Log.e(TAG, "_P_Dato: " + e.getMessage());
				}
				break;

			case P_WRITE_USER:
				try {
					Log.d(TAG, "P_WRITE_USER: onReaderStateChanged");
					if (_waitingForStop == false) {
						if (resultAvailable == false) {
							Log.d(TAG, "Waiting for result...");
						} else {
							if (blockECU < 3) {
								writeECUBlock();
							} else if (blockECU >= 3) {
								sendRFIDEvent(RESULT_ECU_00_Success + "|" + CURRENT_FUNTION +"|" +  _P_EPC.substring(4, _P_EPC.length()) +"|" + _P_TID);
								_tagNotFound = false;
								_writting = true;
								setStatus(STATUS_IDLE);
							}
						}
					} else {
						Log.w(TAG, "Reader stopped");
					}
				} catch (Exception e) {
					Log.i(TAG, "P_WRITE_USER: " + e.getMessage());
				}

				break;

			case P_WRITE_CONSUMO:
				Log.d(TAG, "P_WRITE_USER: onReaderStateChanged");
				if (_waitingForStop == false) {
					if (resultAvailable == false) {
						Log.d(TAG, "Waiting for result...");
					} else {
						sendRFIDEvent(RESULT_ECU_00_Success + "|" + CURRENT_FUNTION +"|" + _P_EPC.substring(4, _P_EPC.length()) +"|" + _P_TID);
						_tagNotFound = false;
						_writting = true;
						setStatus(STATUS_IDLE);
					}
				} else {
					Log.w(TAG, "Reader stopped");
				}
				break;

			//////////////////////////////////////////////
			// leerTag
			//////////////////////////////////////////////
			case L_EPC:
				if (_reader.readMemory6c(BankType.TID, 0, 6, (SimpleSelectionMask) _mask)) {
					setStatus(L_TID);
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case L_TID:
				if (!_L_TID.equals("")) {
					String sEPC = CalculateFolio(_L_TID, _L_Folio);
					Log.i(TAG, "EPC calculado: " + sEPC + " EPC Normal: " + _L_EPC.substring(4, _L_EPC.length()));
					if (_L_EPC.substring(4, _L_EPC.length()).equals(sEPC)) {
						Log.i(TAG, "Lee Usuario ");
						if (_reader.readMemory6c(BankType.User, 0, 20, (SimpleSelectionMask) _mask)) {
							setStatus(L_USER);
						} else {
							setStatus(STATUS_IDLE);
						}
					}
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case L_USER:
				Log.i(TAG, "L_USER:" + _L_USER);
				String sTag = ReParse(_L_USER);
				if (!sTag.equals("")) {
					sendRFIDEvent(RESULT_ECU_00_Success + "|" + CURRENT_FUNTION +"|" +   _L_EPC.substring(4, _L_EPC.length())+"|" + _L_TID + "|" + sTag + "|");
				} else {
					sendRFIDEvent(RESULT_ECU_14_WrongTollingFormat + "|" + CURRENT_FUNTION);
				}
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
				break;

			// grabarConsumo( states
			case G_READ_EPC:
				if (_reader.readMemory6c(BankType.TID, 0, 6, (SimpleSelectionMask) _mask)) {
					setStatus(G_READ_TID);
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case G_READ_TID:
				Log.i(TAG, "G_READ_TID");
				if (!_G_TID.equals("")) {
					String sEPC = CalculateFolio(_G_TID, _G_Folio);
					Log.i(TAG, "EPC calculado: " + sEPC + " EPC Normal: " + _G_EPC.substring(4, _G_EPC.length()));
					if (_G_EPC.substring(4, _G_EPC.length()).equals(sEPC)) {
						// _P_Dato
						// _mask.setOffset(32);
						// _mask.setMask(_P_EPC.substring(4,_P_EPC.length()));
						Log.i(TAG, "Escribe");
						if (_reader.writeMemory(BankType.User, 16, _G_Dato, (SimpleSelectionMask) _mask)) {
							setStatus(G_WRITE_USER);
						} else {
							setStatus(STATUS_IDLE);
						}
					} else {
						setStatus(STATUS_IDLE);
					}
				} else {
					setStatus(STATUS_IDLE);
				}
				break;

			case G_WRITE_USER:
				if (_waitingForStop == false) {
					if (resultAvailable == false) {
						Log.d(TAG, "Waiting for result...");
					} else {
						sendLogEvent("El grabado fue completado con �xito");
						sendRFIDEvent(RESULT_ECU_00_Success + "|" + CURRENT_FUNTION);
						_tagNotFound = false;
						_writting = true;
						setStatus(STATUS_IDLE);
					}
				} else {
					Log.w(TAG, "Reader stopped");
				}
				break;	

			// region Not valid status
			case STATUS_IDLE:
				break;
			case STATUS_NOTINITIALIZED:
				Log.e(TAG, "onReaderStateChanged-STATUS_NOTINITIALIZED-Not supposed to be here.");
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
				break;
			default:
				break;
			// endregion not valid status
			}
		}
	}

	@Override
	public void onReaderResult(ResultCode arg0) {
		Log.i(TAG, "onReaderResult(" + arg0.toString() + ")");

		switch (_status) {
		case PP_WRITE_EPC:
			resultAvailable = true;
			if (arg0.equals(ResultCode.Success)) {
				sendLogEvent("Bloque escrito con �xito");
				_tagNotFound = false;
			} else if (arg0.equals(ResultCode.MemoryOverrun)) {
				sendLogEvent("Error- Memoria Overrun.");
				sendRFIDEvent(RESULT_ECU_12_BlockedMemory + "|" + CURRENT_FUNTION);
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
			}
			break;

		case P_WRITE_USER:
			Log.i(TAG, "P_WRITE_USER....");
			resultAvailable = true;
			if (arg0.equals(ResultCode.Success)) {
				sendLogEvent("Bloque escrito con �xito");
				_tagNotFound = false;
			} else if (arg0.equals(ResultCode.MemoryOverrun)) {
				sendLogEvent("Error- Memoria Overrun.");
				sendRFIDEvent(RESULT_ECU_12_BlockedMemory + "|" + CURRENT_FUNTION);
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
			}
			break;

		case P_WRITE_CONSUMO:
			Log.i(TAG, "P_WRITE_USER....");
			resultAvailable = true;
			if (arg0.equals(ResultCode.Success)) {
				sendLogEvent("Bloque escrito con �xito");
				_tagNotFound = false;
			} else if (arg0.equals(ResultCode.MemoryOverrun)) {
				sendLogEvent("Error- Memoria Overrun.");
				sendRFIDEvent(RESULT_ECU_12_BlockedMemory + "|" + CURRENT_FUNTION);
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
			}
			break;

		case G_WRITE_USER:
			resultAvailable = true;
			if (arg0.equals(ResultCode.Success)) {
				sendLogEvent("Bloque escrito con �xito");
				_tagNotFound = false;
			} else if (arg0.equals(ResultCode.MemoryOverrun)) {
				sendLogEvent("Error- Memoria Overrun.");
				sendRFIDEvent(RESULT_ECU_12_BlockedMemory + "|" + CURRENT_FUNTION);
				_tagNotFound = false;
				setStatus(STATUS_IDLE);
			}
			break;

		// region Not valid status
		case STATUS_NOTINITIALIZED:
			Log.e(TAG, "onReaderResult-STATUS_NOTINITIALIZED-Not supposed to be here");
			break;
		case STATUS_IDLE:
			Log.e(TAG, "onReaderResult-STATUS_IDLE-Not supposed to be here");
			break;
		default:
			break;
		// endregion Not valid status
		}
	}

	@Override
	public void onReaderRssiChanged(int arg0) {
		Log.i(TAG, "onReaderRssiChanged(" + arg0 + ")");
	}

	// region Utilities
	private String CalculateFolio(String TID, String Folio) {
		// Log.i(TAG, " CalculateFolio()");
		int foo = Integer.parseInt(Folio);
		String hFolio = Integer.toHexString(foo);
		hFolio = String.format("%6s", hFolio).replace(' ', '0');
		String FF = "4E45435500" + hFolio + TID.substring(TID.length() - 8, TID.length());
		// Log.i(TAG, " Folio Final: " + FF.toUpperCase());
		return FF.toUpperCase();
	}

	@SuppressLint("DefaultLocale")
	public String convertHexToString(String hex) {
		StringBuilder sb = new StringBuilder(hex.length() / 2);
		for (int i = 0; i < hex.length(); i += 2) {
			String hexRes = "" + hex.charAt(i) + hex.charAt(i + 1);
			int ival = Integer.parseInt(hexRes, 16);
			sb.append((char) ival);
		}
		return sb.toString();
	}

	private String StringToHex(String str) {
		char[] chars = str.toCharArray();
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}
		return hex.toString();
	}

	private String ReParse(String str) {
		try {
			// Log.i(TAG, " ReParse()");
			String Placa = convertHexToString(str.substring(0, 14));
			String Tipo = convertHexToString(str.substring(14, 16));

			Long Galones = Long.parseLong(str.substring(16, 20), 16);

			Long dGalones = (Long.parseLong(str.substring(20, 22), 16));
			String sdGalones = dGalones.toString();
			if (sdGalones.length() < 2)
				sdGalones = "0" + sdGalones;

			String Subsidio = convertHexToString(str.substring(22, 24));

			String sType = convertHexToString(str.substring(24, 26));
			String sVin = convertHexToString(str.substring(26, 60));

			Long Dia = Long.parseLong(str.substring(64, 66), 16);
			String sDia = Dia.toString();
			if (sDia.length() < 2)
				sDia = "0" + sDia;

			Long Mes = Long.parseLong(str.substring(66, 68), 16);
			String sMes = Mes.toString();
			if (sMes.length() < 2)
				sMes = "0" + sMes;

			long Year = Long.parseLong(str.substring(68, 72), 16);

			Long GalonesC = Long.parseLong(str.substring(72, 76), 16);
			Long dGalonesC = Long.parseLong(str.substring(76, 78), 16);
			String sdGalonesC = dGalonesC.toString();
			if (sdGalonesC.length() < 2)
				sdGalonesC = "0" + sdGalonesC;

			String Result = Placa + "|" + sVin + "|" + sType + "|" + Tipo + "|" + Subsidio + "|" + Galones + "."
					+ sdGalones + "|" + sDia + sMes + Year + "|" + GalonesC + "." + sdGalonesC;
			return Result;
		} catch (Exception e) {
			Log.e(TAG, " ReParse " + e.toString());
			return str;
		}
	}
}
