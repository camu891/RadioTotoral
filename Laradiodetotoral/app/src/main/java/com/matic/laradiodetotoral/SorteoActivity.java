package com.matic.laradiodetotoral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SorteoActivity extends AppCompatActivity {

    private String FIREBASE_URL = "https://boiling-inferno-5956.firebaseio.com/";
    private String FIREBASE_CHILD = "Sorteo";


    @Bind(R.id.txt_nombre)
    EditText txtNombre;
    @Bind(R.id.txt_doc)
    EditText txtDoc;
    @Bind(R.id.txt_descrip)
    EditText txtDesc;
    Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);
        //activar boton back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL).child(FIREBASE_CHILD);


    }

    @OnClick(R.id.button)
    public void writeToFirebase() {
        String name=txtNombre.getText().toString();
        String doc=txtDoc.getText().toString();
        String desc=txtDesc.getText().toString();

        firebase.setValue(name);
        firebase.setValue(doc);
        firebase.setValue(desc);
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
