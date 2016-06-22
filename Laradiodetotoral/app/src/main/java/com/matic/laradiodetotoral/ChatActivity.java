package com.matic.laradiodetotoral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.matic.laradiodetotoral.adapters.ChatAdapter;
import com.matic.laradiodetotoral.models.Chat;
import com.matic.laradiodetotoral.rss.RSSAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager lManager;
    private RecyclerView recyclerView;


    private String FIREBASE_URL = "https://estacionfm.firebaseio.com/";

    @Bind(R.id.txtBox_comment)
    EditText txtComments;


    private ArrayList<Chat> list;

    Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //activar boton back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);

        firebase=new Firebase(FIREBASE_URL).child("Comentarios");


        // Obtener el Recycler
        recyclerView = (RecyclerView) findViewById(R.id.reciclador_chat);
        recyclerView.setHasFixedSize(true);

        cargarListado();
    }

    @OnClick(R.id.btn_send)
    public void writeToFirebase() {

        String comment=txtComments.getText().toString();
        Date d=new Date();
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date= formatter.format(d.getTime());


        final HashMap<String, String> map1=new HashMap<String, String>();
        map1.put("comment",comment);
        map1.put("date",date);
        firebase.push().setValue(map1);

        vaciarCampos();
    }


    public void vaciarCampos(){
        txtComments.setText("");


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

                list=new ArrayList<Chat>();

                Map<String, Object> objectMap = (HashMap<String, Object>)
                        dataSnapshot.getValue();

                for (Object obj : objectMap.values()) {


                    if (obj instanceof Map) {

                        Map<String, Object> mapObj = (Map<String, Object>) obj;

                        String com = (String) mapObj.get("comment");
                        String fecha = (String) mapObj.get("date");

                        list.add(new Chat(com,fecha));


                    }
                }


                if(!list.isEmpty() || list!=null) {
                    ChatAdapter adapter = new ChatAdapter(ChatActivity.this, list);

                    // Usar un administrador para LinearLayout
                    lManager = new LinearLayoutManager(ChatActivity.this);
                    recyclerView.setLayoutManager(lManager);

                    // Crear un nuevo adaptador
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(ChatActivity.this,"NO HAY COMENTARIOS",Toast.LENGTH_SHORT).show();
                }

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
