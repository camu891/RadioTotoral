package com.matic.laradiodetotoral.models;

import java.util.Date;

/**
 * Created by matic on 22/06/16.
 */
public class Chat {

    private String comentario;
    private String fecha;

    public Chat(String comentario, String fecha) {
        this.comentario = comentario;
        this.fecha = fecha;
    }



    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    @Override
    public String toString() {
        return "Chat{" +
                "comentario='" + comentario + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
