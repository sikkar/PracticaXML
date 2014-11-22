package com.izv.angel.practica1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class Principal extends Activity {

    private ArrayList <Bicicleta> bicicletas;
    private AdaptadorArrayList ad;
    private static final int SELECT_PHOTO = 100;
    private int posicion;
    private AlertDialog alert=null;


    /*****************************************************/
    /*                 metodos on                        */
    /*****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        bicicletas = new ArrayList <Bicicleta> ();
        leerXML();
        initComponents();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_aniadir) {
            return aniadir();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("bicicletas", bicicletas);
        ad.notifyDataSetChanged();
        initComponents();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bicicletas=savedInstanceState.getParcelableArrayList("bicicletas");
        ad.notifyDataSetChanged();
        initComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alert!=null){
            alert.dismiss();
        }
    }


    /*****************************************************/
    /*                     auxiliares                    */
    /*****************************************************/

    public void initComponents(){
        //Bicicleta para probar la aplicacion
        /*
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/bike");
        Bicicleta bici = new Bicicleta("Orbea","Alma",uri ,"Montaña");
        bicicletas.add(bici);*/

        ad = new AdaptadorArrayList(this, R.layout.detalle_lista, bicicletas);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }

    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /*****************************************************/
    /*                 metodos manejo de la lista        */
    /*****************************************************/


    public boolean aniadir(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(R.string.alta);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        dialog.setView(vista);
        dialog.setPositiveButton(R.string.alta,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText et1, et2;
                        String tipo;
                        RadioGroup rg = (RadioGroup) vista.findViewById(R.id.radioGroup);
                        RadioButton rb = (RadioButton) vista.findViewById(rg.getCheckedRadioButtonId());
                        tipo = rb.getText().toString();
                        et1 = (EditText) vista.findViewById(R.id.etMarca);
                        et2 = (EditText) vista.findViewById(R.id.etModelo);
                        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/bike");
                        Bicicleta bc = new Bicicleta(et1.getText().toString(), et2.getText().toString(), uri, tipo);
                        if (!bicicletas.contains(bc)) {
                            bicicletas.add(bc);
                            tostada(getString(R.string.elAniadido));
                        } else {
                            tostada(getString(R.string.existe));
                            aniadir();
                        }
                        Collections.sort(bicicletas);
                        ad.notifyDataSetChanged();
                        crearXML();
                    }
                });
        dialog.setNegativeButton(R.string.cancelar,null);
        alert = dialog.create();
        alert.show();
        return true;
    }


    public void borrar(View view){
        final int elemento;
        elemento = (Integer)view.getTag();
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.borrar) +" "+ bicicletas.get(elemento).getMarca() +" "+bicicletas.get(elemento).getModelo()+" "+getString(R.string.exclamacion) );
        LayoutInflater inflater = LayoutInflater.from(this);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                bicicletas.remove(elemento);
                Collections.sort(bicicletas);
                ad.notifyDataSetChanged();
                crearXML();
            }
        });
        dialog.setNegativeButton(android.R.string.no,null);
        alert = dialog.create();
        alert.show();
    }

    public void editar(View view){
        final int elemento;
        elemento = (Integer)view.getTag();
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(R.string.editar);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_alta, null);
        dialog.setView(vista);
        final EditText et1, et2;
        et1 = (EditText) vista.findViewById(R.id.etMarca);
        et1.setText(bicicletas.get(elemento).getMarca());
        et2 = (EditText) vista.findViewById(R.id.etModelo);
        et2.setText(bicicletas.get(elemento).getModelo());
        dialog.setPositiveButton(R.string.editar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText et1, et2;
                        String tipo;
                        RadioGroup rg = (RadioGroup) vista.findViewById(R.id.radioGroup);
                        RadioButton rb = (RadioButton) vista.findViewById(rg.getCheckedRadioButtonId());
                        tipo = rb.getText().toString();
                        et1 = (EditText) vista.findViewById(R.id.etMarca);
                        et2 = (EditText) vista.findViewById(R.id.etModelo);
                        bicicletas.get(elemento).setMarca(et1.getText().toString());
                        bicicletas.get(elemento).setModelo(et2.getText().toString());
                        bicicletas.get(elemento).setTipo(tipo);
                        Collections.sort(bicicletas);
                        ad.notifyDataSetChanged();
                        crearXML();
                    }
                });
        dialog.setNegativeButton(R.string.cancelar,null);
        alert = dialog.create();
        alert.show();
    }



    /*****************************************************/
    /*                 metodos cambiar foto              */
    /*****************************************************/


    public void cambiarFoto(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        posicion = (Integer)view.getTag();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    boolean guardar =true;
                    for (int i=0;i<bicicletas.size();i++){
                        if(bicicletas.get(i).getFoto().compareTo(selectedImage)==0){
                            tostada(getString(R.string.utilizada));
                            guardar=false;
                        }
                    }
                    if (guardar){
                        bicicletas.get(posicion).setFoto(selectedImage);
                    }
                    ad.notifyDataSetChanged();
                    crearXML();
                }
        }
    }


    /*****************************************************/
    /*                 metodos gestion XML               */
    /*****************************************************/

    public void crearXML() {
        try {
            FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"bicicletas.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            for (int i=0;i<bicicletas.size();i++){
                docxml.startTag(null, "root");
                docxml.startTag(null,"bicicleta");
                    docxml.startTag(null,"marca");
                        docxml.text(bicicletas.get(i).getMarca());
                    docxml.endTag(null, "marca");
                    docxml.startTag(null,"modelo");
                        docxml.text(bicicletas.get(i).getModelo());
                    docxml.endTag(null, "modelo");
                    docxml.startTag(null,"foto");
                        docxml.text(bicicletas.get(i).getFoto().toString());
                    docxml.endTag(null, "foto");
                    docxml.startTag(null,"tipo");
                        docxml.text(bicicletas.get(i).getTipo());
                    docxml.endTag(null, "tipo");
                docxml.endTag(null, "bicicleta");
            }
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    public void leerXML (){
        XmlPullParser lectorxml = Xml.newPullParser();
        File archivo = new File(getExternalFilesDir(null),"bicicletas.xml");
        try {
            if(archivo.exists()){
                lectorxml.setInput(new FileInputStream(archivo),"utf-8");
            }
            else{
                FileOutputStream crear = new FileOutputStream(archivo);
                return;
            }
            int evento = lectorxml.getEventType();
            String marca="",tipo="",modelo="",foto="";
            Uri fotoUri=null;
            while (evento != XmlPullParser.END_DOCUMENT){
                String etiqueta="";
                if(evento == XmlPullParser.START_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("marca")==0){
                        marca = lectorxml.nextText();
                    }
                    if(etiqueta.compareTo("modelo")==0){
                        modelo = lectorxml.nextText();
                    }
                    if(etiqueta.compareTo("foto")==0){
                        foto = lectorxml.nextText();
                        fotoUri=Uri.parse(foto);
                    }
                    if(etiqueta.compareTo("tipo")==0){
                        tipo = lectorxml.nextText();
                    }
                }
                if(evento== XmlPullParser.END_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("bicicleta")==0){
                        Bicicleta bici = new Bicicleta(marca,modelo,fotoUri,tipo);
                        bicicletas.add(bici);
                    }
                }
                evento = lectorxml.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
