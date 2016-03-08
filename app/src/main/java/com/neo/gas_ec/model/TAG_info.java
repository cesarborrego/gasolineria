package com.neo.gas_ec.model;

/**
 * Created by cesar on 23/11/15.
 */
public class TAG_info {
    private String folioTag;
    private String placa;
    private String UID;
    private int cantidadGalones;
    private boolean isSUbsidio;

    public TAG_info() {

    }

    public TAG_info(
            String UID,
            String folioTag,
            String placa,
            int cantidadGalones,
            boolean isSUbsidio) {
        this.UID = UID;
        this.folioTag =  folioTag;
        this.placa = placa;
        this.cantidadGalones = cantidadGalones;
        this.isSUbsidio = isSUbsidio;
    }


    public String getFolioTag() {
        return folioTag;
    }

    public void setFolioTag(String folioTag) {
        this.folioTag = folioTag;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getCantidadGalones() {
        return cantidadGalones;
    }

    public void setCantidadGalones(int cantidadGalones) {
        this.cantidadGalones = cantidadGalones;
    }

    public boolean isSUbsidio() {
        return isSUbsidio;
    }

    public void setIsSUbsidio(boolean isSUbsidio) {
        this.isSUbsidio = isSUbsidio;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
