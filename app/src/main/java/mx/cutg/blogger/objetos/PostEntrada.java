package mx.cutg.blogger.objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class PostEntrada implements Parcelable
{
    private int id_entrada;
    private String nb_titulo;
    private String nb_autor;
    private String de_contenido;
    private String fh_publicacion;

    @SuppressWarnings("unused")
    public PostEntrada() {
        super();
    }


    @SuppressWarnings("unused")
    public PostEntrada(String autor, String titulo, String contenido, String fecha)
    {
        this.nb_titulo = titulo;
        this.nb_autor = autor;
        this.de_contenido = contenido;
        this.fh_publicacion = fecha;
    }


    public PostEntrada(Parcel source)
    {
        this.nb_titulo = source.readString();
        this.nb_autor = source.readString();
        this.de_contenido = source.readString();
        this.fh_publicacion = source.readString();
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(nb_titulo);
        parcel.writeString(nb_autor);
        parcel.writeString(de_contenido);
        parcel.writeString(fh_publicacion);
    }


    public static final Creator CREATOR = new Creator()
    {
        public PostEntrada createFromParcel(Parcel in) {
            return new PostEntrada(in);
        }
        public PostEntrada[] newArray(int size) {
            return new PostEntrada[size];
        }
    };


    public String getNb_titulo() {
        return nb_titulo;
    }


    public String getNb_autor() {
        return nb_autor;
    }


    public String getDe_contenido() {
        return de_contenido;
    }


    public String getFh_publicacion() {
        return fh_publicacion;
    }


    public void setNb_titulo(String nb_titulo) {
        this.nb_titulo = nb_titulo;
    }


    public void setNb_autor(String nb_autor) {
        this.nb_autor = nb_autor;
    }


    public void setDe_contenido(String de_contenido) {
        this.de_contenido = de_contenido;
    }


    public void setFh_publicacion(String fh_publicacion) {
        this.fh_publicacion = fh_publicacion;
    }

}