package com.neo.gas_ec.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cesar on 6/11/15.
 */
public class Preferences {

    SharedPreferences settings;

    /**
     * Constructor que crea el objeto SharedPreferences
     * @param sharedPrefName
     * @param context
     */
    public Preferences(String sharedPrefName, Context context){
        //Referencia al shared preferences
        settings = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    /**
     * True/False dependiendo si existe el valor
     * @param key
     * @return
     */
    public boolean isExist(String key){
        String value = settings.getString(key, null);
        if(value==null){return false;}else{ return true; }
    }

    /**
     * Devuelve el valor correspondiente a la llave del parametro
     * @param key
     * @return
     */
    public String Get_stringfrom_shprf(String key){
        String valor = settings.getString(key, null);
        return valor;
    }

    public int Get_intfrom_shprf(String key){
        int valor = settings.getInt(key, 0);
        return valor;
    }

    /**
     * Escribe una cadena "valor" con la clave "clave" en SharedPreferences
     * @param clave
     * @param valor
     */
    public void Write_String(String clave, String valor){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(clave, valor);
        editor.commit();
    }

    public void Write_Int(String clave, int valor){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(clave, valor);
        editor.commit();
    }
    /**
     * Borra el valor contenido en la clave "clave" en SharedPreferences
     * @param clave
     */
    public void Remove_Value(String clave){
        SharedPreferences.Editor editor=settings.edit();
        editor.remove(clave);
        editor.commit();
    }
}
