package com.matic.laradiodetotoral;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.matic.laradiodetotoral.adapters.ChatAdapter;
import com.matic.laradiodetotoral.models.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        firebase.orderByChild("date");

        // Obtener el Recycler
        recyclerView = (RecyclerView) findViewById(R.id.reciclador_chat);
        recyclerView.setHasFixedSize(true);


        //cargarListado();
        LoadChat loadChat=new LoadChat();
        loadChat.execute();

    }

    @OnClick(R.id.btn_send)
    public void writeToFirebase() {


        String comment=txtComments.getText().toString();

        if (!comment.isEmpty())
        {
        Date d=new Date();
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date= formatter.format(d.getTime());
        final HashMap<String, String> map1=new HashMap<String, String>();
        map1.put("comment",comment);
        map1.put("date",date);
        firebase.push().setValue(map1);
        vaciarCampos();
        }else{
            Toast.makeText(this,"Primero debes escribir algo...",Toast.LENGTH_SHORT).show();
        }

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

                //ordeno la lista por fecha
                Collections.sort(list, new Comparator<Chat>() {
                    @Override
                    public int compare(Chat c2, Chat c1)
                    {
                        return  c1.getFecha().compareTo(c2.getFecha());
                    }
                });

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



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private class LoadChat  extends AsyncTask<Void,Void,Void> {

        ProgressDialog pd;

        @Override
        protected Void doInBackground(Void... voids) {
            cargarListado();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ChatActivity.this);
            pd.setMessage(ChatActivity.this.getString(R.string.str_loadingComments));
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd.isShowing()){pd.dismiss();}
        }
    }




}
