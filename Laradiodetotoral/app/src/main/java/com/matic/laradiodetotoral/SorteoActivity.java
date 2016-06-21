package com.matic.laradiodetotoral;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SorteoActivity extends AppCompatActivity {

    private String FIREBASE_URL = "https://boiling-inferno-5956.firebaseio.com/RadioTotoral/Chat";
    private String FIREBASE_CHILD_NOMBRE = "nombre";
    private String FIREBASE_CHILD_DOCUMENTO = "documento";
    private String FIREBASE_CHILD_DESCRIPCION = "descripcion";


    @Bind(R.id.txt_nombre)
    EditText txtNombre;
    @Bind(R.id.txt_doc)
    EditText txtDoc;
    @Bind(R.id.txt_descrip)
    EditText txtDesc;
    Firebase firebaseNombre,firebaseDocumento, firebaseDescripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);
        //activar boton back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        firebaseNombre = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD_NOMBRE);
        firebaseDocumento = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD_DOCUMENTO);
        firebaseDescripcion = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD_DESCRIPCION);

        firebaseNombre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(SorteoActivity.this,"Nombre: "+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @OnClick(R.id.button)
    public void writeToFirebase() {
        String name=txtNombre.getText().toString();
        String doc=txtDoc.getText().toString();
        String desc=txtDesc.getText().toString();

        firebaseNombre.setValue(name);
        firebaseDocumento.setValue(doc);
        firebaseDescripcion.setValue(desc);

        vaciarCampos();
    }


    public void vaciarCampos(){
        txtNombre.setText("");
        txtDoc.setText("");
        txtDesc.setText("");

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



}
