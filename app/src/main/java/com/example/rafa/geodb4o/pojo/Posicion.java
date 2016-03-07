package com.example.rafa.geodb4o.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Posicion implements Parcelable{

    private float latitud;
    private float longitud;
    private int dia;

    public Posicion() {
    }

    public Posicion(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.dia = 0;
    }

    public Posicion(float latitud, float longitud, int dia) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.dia = dia;
    }

    protected Posicion(Parcel in) {
        latitud = in.readFloat();
        longitud = in.readFloat();
        dia = in.readInt();
    }

    public static final Parcelable.Creator<Posicion> CREATOR = new Parcelable.Creator<Posicion>() {
        @Override
        public Posicion createFromParcel(Parcel in) {
            return new Posicion(in);
        }

        @Override
        public Posicion[] newArray(int size) {
            return new Posicion[size];
        }
    };

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "Posicion{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", dia=" + dia +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(latitud);
        dest.writeFloat(longitud);
        dest.writeInt(dia);
    }
}

