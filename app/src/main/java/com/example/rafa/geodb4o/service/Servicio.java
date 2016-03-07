package com.example.rafa.geodb4o.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.rafa.geodb4o.bd.Db4O;
import com.example.rafa.geodb4o.pojo.Posicion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class Servicio extends Service implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private LocationRequest peticionLocalizaciones;
    private GoogleApiClient cliente;
    private Db4O bd;


    public Servicio(){
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            cliente.connect();
            Toast.makeText(this, "Conectado", Toast.LENGTH_LONG).show();
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            } else {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }

        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setSmallestDisplacement(1);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return START_REDELIVER_INTENT;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        bd.close();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onConnected(Bundle bundle) {
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setFastestInterval(10000);
        peticionLocalizaciones.setSmallestDisplacement(1);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        float la = (float)location.getLatitude();
        float lo = (float)location.getLongitude();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Posicion po = new Posicion(la,lo,day);
        bd = new Db4O(this);
        bd.insertar(po);
        bd.close();
        Toast.makeText(this,"Latitud: " +la + " " + "Longitud: " + lo,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
