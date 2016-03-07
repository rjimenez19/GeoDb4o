package com.example.rafa.geodb4o;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rafa.geodb4o.bd.Db4O;
import com.example.rafa.geodb4o.pojo.Posicion;
import com.example.rafa.geodb4o.service.Servicio;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap map;
    private static final int CTEPLAY = 1;
    private LocationRequest peticionLocalizaciones;
    private GoogleApiClient cliente;
    private Db4O bd;
    private Location ultimaLocalizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapa();
        Intent intent = new Intent(this, Servicio.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lunes) {
            ruta(2);
            return true;
        }
        if (id == R.id.martes) {
            ruta(3);
            return true;
        }

        if (id == R.id.miercoles) {
            ruta(4);
            return true;
        }
        if (id == R.id.jueves) {
            ruta(5);
            return true;
        }
        if (id == R.id.viernes) {
            ruta(6);
            return true;
        }
        if (id == R.id.sabado) {
            ruta(7);
            return true;
        }
        if (id == R.id.domingo) {
            ruta(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setSmallestDisplacement(1);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
        ultimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(cliente);
        if (ultimaLocalizacion != null) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double la = location.getLatitude();
        double lo = location.getLongitude();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    public void ruta(int dia) {
        bd = new Db4O(this);
        PolylineOptions rectOptions = new PolylineOptions();
        ArrayList<Posicion> pos = bd.getRuta(dia);
        for (Posicion p : pos) {
            rectOptions.add(new LatLng(p.getLatitud(), p.getLongitud()));
        }

        map.clear();
        map.addPolyline(rectOptions);
        bd.close();
    }

    public void mapa() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            cliente.connect();
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this, CTEPLAY).show();
            } else {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setInterval(10000);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
