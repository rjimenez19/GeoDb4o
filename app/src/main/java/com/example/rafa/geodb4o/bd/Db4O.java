package com.example.rafa.geodb4o.bd;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.example.rafa.geodb4o.pojo.Posicion;

import java.util.ArrayList;


public class Db4O {

    private ObjectContainer bd;

    public Db4O(Context ctx) {
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ctx.getExternalFilesDir(null) + "/bd.db4o");
    }


    public void insertar(Posicion ser){
        bd.store(ser);
        bd.commit();
    }

    public void update(Posicion ser){
        bd.store(ser);
        bd.commit();
    }

    public void delete(Posicion ser){
        bd.delete(ser);
        bd.commit();
    }

    public void close(){
        bd.close();
    }

    public ArrayList<Posicion> getConsulta(){
        Query consulta = bd.query();
        consulta.constrain(Posicion.class);
        ObjectSet<Posicion> ser = consulta.execute();
        ArrayList<Posicion> lista = new ArrayList<Posicion>();
        for(Posicion serie: ser){
            lista.add(serie);
        }
        return lista;
    }

    public ArrayList<String> getConsultaDias(){
        Query consulta = bd.query();
        consulta.constrain(Posicion.class);
        ObjectSet<Posicion> ser = consulta.execute();
        ArrayList<String> lista = new ArrayList<String>();
        for(Posicion serie: ser){
            String seri = serie.getDia()+"";
            lista.add(seri);
        }
        return lista;
    }

    public ArrayList<Posicion> getRuta(int dia){
        Query q = bd.query();
        q.constrain(Posicion.class);
        q.descend("dia").constrain(dia).like();
        ObjectSet<Posicion> p= q.execute();
        ArrayList<Posicion> ruta = new ArrayList<Posicion>();
        for(Posicion a: p){
            ruta.add(a);
        }
        return ruta;
    }
}
