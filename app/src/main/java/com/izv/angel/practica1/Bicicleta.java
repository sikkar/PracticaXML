package com.izv.angel.practica1;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by AngelakaMogu on 17/10/2014.
 */
public class Bicicleta implements Comparable <Bicicleta>, Serializable, Parcelable {

    private String marca;
    private String modelo;
    private String tipo;
    private Uri foto;

    public Bicicleta(String marca, String modelo, Uri foto, String tipo) {
        this.marca = marca;
        this.modelo = modelo;
        this.foto = foto;
        this.tipo = tipo;
    }

    public Bicicleta() {
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Bicicleta.class != o.getClass()) return false;

        Bicicleta bicicleta = (Bicicleta) o;

        if (marca != null ? !marca.equals(bicicleta.marca) : bicicleta.marca != null) return false;
        if (modelo != null ? !modelo.equals(bicicleta.modelo) : bicicleta.modelo != null) return false;
        if (tipo != null ? !tipo.equals(bicicleta.tipo) : bicicleta.tipo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = marca != null ? marca.hashCode() : 0;
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Bicicleta bicicleta) {
        String a = getMarca() +","+getModelo()+ "," + getTipo() ;
        String b = bicicleta.getMarca() +","+bicicleta.getModelo()+ "," + bicicleta.getTipo();
        return a.compareTo(b);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.marca);
        parcel.writeString(this.modelo);
        parcel.writeValue(this.foto);
        parcel.writeString(this.tipo);
    }

    public static final Creator <Bicicleta> CREATOR = new Creator<Bicicleta>() {

        @Override
        public Bicicleta createFromParcel(Parcel p) {
            String marca = p.readString();
            String modelo = p.readString();
            Uri foto = (Uri)p.readValue(Uri.class.getClassLoader());
            String tipo = p.readString();
            return new Bicicleta(marca,modelo,foto,tipo);
        }

        @Override
        public Bicicleta[] newArray(int i) {
            return new Bicicleta[i];
        }
    };

    @Override
    public String toString() {
        return "Bicicleta{" +
                "marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", foto=" + foto +
                '}';
    }
}
