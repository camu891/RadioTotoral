package com.matic.laradiodetotoral.rss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.speech.tts.Voice;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.matic.laradiodetotoral.R;
import com.matic.laradiodetotoral.utils.Constants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by matic on 14/06/16.
 */
public class ReadRSS  extends AsyncTask<Void,Void,Void>{

    Context context;
    ProgressDialog pd;
    URL url;
    ArrayList<FeedItem> feedItems;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lManager;

    public ReadRSS(Context context, RecyclerView recyclerView){

        this.context=context;
        this.recyclerView=recyclerView;
        pd=new ProgressDialog(context);
        pd.setMessage("Cargando...");
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //pd.show();
    }


    @Override
    protected Void doInBackground(Void... params) {

       ProcessXml(getData());

        return null;

    }

    private void ProcessXml(Document data) {

        if (data!=null) {
            feedItems=new ArrayList<>();

            Log.d("RootRSS", data.getDocumentElement().getNodeName());
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node currentChild = items.item(i);
                if (currentChild.getNodeName().equalsIgnoreCase("item")) {

                    FeedItem item = new FeedItem();
                    NodeList itemChilds = currentChild.getChildNodes();
                    for (int j = 0; j < itemChilds.getLength(); j++) {
                        Node current = itemChilds.item(j);
                        Log.d("TextContent", current.getTextContent());

                        if (current.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("description")) {
                            item.setDescripcion(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(current.getTextContent());
                        } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(current.getTextContent());
                        }else if(current.getNodeName().equalsIgnoreCase("category")) {
                            item.setCategory(current.getTextContent());}

                    }

                    feedItems.add(item);

                    Log.d("rssItems","items: "+item);

                }
            }
        }

    }


    public Document getData(){
        try {
            url=new URL(Constants.URL_RSS);

            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream=connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=builderFactory.newDocumentBuilder();
            Document xmlDoc=builder.parse(inputStream);

            return xmlDoc;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }





    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

       // if (pd.isShowing()){pd.dismiss();}

        RSSAdapter adapter=new RSSAdapter(context,feedItems);



        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        recyclerView.setAdapter(adapter);



    }
}
