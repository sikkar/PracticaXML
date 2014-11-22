package com.izv.angel.practica1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorArrayList extends ArrayAdapter <Bicicleta> {

    private Context contexto;
    private ArrayList <Bicicleta> lista;
    private int recurso;
    private LayoutInflater i;

    public class ViewHolder {
        public TextView tv1, tv2, tv3;
        public ImageView iv;
        public ImageButton bt1,bt2;
        public int posicion;
    }

    public AdaptadorArrayList(Context context, int resource, ArrayList <Bicicleta> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv1 = (TextView) convertView.findViewById(R.id.tvMarca);
            vh.tv2 = (TextView) convertView.findViewById(R.id.tvModelo);
            vh.iv = (ImageView) convertView.findViewById(R.id.ivFoto);
            vh.bt1 = (ImageButton) convertView.findViewById(R.id.btEliminar);
            vh.bt2 = (ImageButton) convertView.findViewById(R.id.btEditar);
            vh.tv3 = (TextView) convertView.findViewById(R.id.tvTipo);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv1.setText(lista.get(position).getMarca());
        vh.tv2.setText(lista.get(position).getModelo());
        vh.tv3.setText(lista.get(position).getTipo());
        vh.iv.setTag(position);
        vh.iv.setImageURI(lista.get(position).getFoto());
        vh.bt1.setTag(position);
        vh.bt2.setTag(position);
        return convertView;
    }

}