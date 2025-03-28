/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PantallaInicio;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Hashtag {
    private String hashtag;
    private String categoria;
    private int vecesUtilizado;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Hashtag(String hashtag, String categoria, int vecesUtilizado) {
        this.hashtag = hashtag;
        this.categoria = categoria;
        this.vecesUtilizado = vecesUtilizado;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        String oldHashtag = this.hashtag;
        this.hashtag = hashtag;
        pcs.firePropertyChange("hashtag", oldHashtag, hashtag);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        String oldCategoria = this.categoria;
        this.categoria = categoria;
        pcs.firePropertyChange("categoria", oldCategoria, categoria);
    }

    public int getVecesUtilizado() {
        return vecesUtilizado;
    }

    public void setVecesUtilizado(int vecesUtilizado) {
        int oldVecesUtilizado = this.vecesUtilizado;
        this.vecesUtilizado = vecesUtilizado;
        pcs.firePropertyChange("vecesUtilizado", oldVecesUtilizado, vecesUtilizado);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
