package com.matic.laradiodetotoral;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.matic.laradiodetotoral.models.Chat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SorteoActivity extends AppCompatActivity {

    private String FIREBASE_URL = "https://estacionfm.firebaseio.com/";


    @Bind(R.id.txt_nombre)
    EditText txtNombre;
    @Bind(R.id.txt_doc)
    EditText txtDoc;
    @Bind(R.id.txt_coment)
    EditText txtComent;
    @Bind(R.id.txt_telefono)
    EditText txtTel;

    @Bind(R.id.listView)
    ListView listview;


    Firebase firebase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);
        //activar boton back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);

        firebase=new Firebase(FIREBASE_URL).child("Sorteos");



        cargarListado();

    }


    @OnClick(R.id.button)
    public void writeToFirebase() {

       attemptConfirm();
    }

    public void guardarDatos(){
        String name=txtNombre.getText().toString();
        String doc=txtDoc.getText().toString();
        String coment= txtComent.getText().toString();
        String tel=txtTel.getText().toString();

        final HashMap<String, String> map1=new HashMap<String, String>();
        map1.put("nombre",name);
        map1.put("documento",doc);
        map1.put("telefono",tel);
        map1.put("comentario",coment);
        firebase.push().setValue(map1);

        //ver como comprobar si se guardó o no
        Toast.makeText(SorteoActivity.this, getString(R.string.str_congrat), Toast.LENGTH_SHORT).show();
        vaciarCampos();
    }

    public void vaciarCampos(){
        txtNombre.setText("");
        txtDoc.setText("");
        txtComent.setText("");
        txtTel.setText("");

    }

    public void attemptConfirm() {

        // Reset errors.
        txtNombre.setError(null);
        txtDoc.setError(null);
        txtTel.setError(null);

        String name=txtNombre.getText().toString();
        String doc=txtDoc.getText().toString();
        String tel=txtTel.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(name)) {
            txtNombre.setError(getString(R.string.error_field_required));
            focusView = txtNombre;
            cancel = true;
        }

        if (TextUtils.isEmpty(doc)) {
            txtDoc.setError(getString(R.string.error_field_required));
            focusView = txtDoc;
            cancel = true;
        }

        if (TextUtils.isEmpty(tel)) {
            txtTel.setError(getString(R.string.error_field_required));
            focusView = txtTel;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            guardarDatos();
        }




    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void cargarListado(){

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> lista = new ArrayList<String>();

                Map<String, Object> objectMap = (HashMap<String, Object>)
                        dataSnapshot.getValue();

                for (Object obj : objectMap.values()) {


                    if (obj instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) obj;

                        String name = (String) mapObj.get("nombre");
                        String doc = (String) mapObj.get("documento");
                        String coment=(String) mapObj.get("comentario");
                        String tel=(String) mapObj.get("telefono");

                        Log.i("DATOSMAP", " nombre: "+name+"\n documento: "+doc+"\n comentario: "+coment+"\n tel: "+tel);


                        lista.add("Nombre: "+name+"\nDocumento: "+doc+"\nTeléfono: "+tel+"\nComentario: "+coment);


                    }
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        SorteoActivity.this,
                        android.R.layout.simple_list_item_1,
                        lista);

                listview.setAdapter(adapter);

                       /*String name = (String) dataSnapshot.child("nombre").getValue();
                        String doc = (String)  dataSnapshot.child("documento").getValue();
                        String desc = (String)  dataSnapshot.child("descripcion").getValue();

                        listDatos.add(new Chat(name,doc,desc));*/


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }





}
