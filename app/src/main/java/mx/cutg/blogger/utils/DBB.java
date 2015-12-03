package mx.cutg.blogger.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBB extends SQLiteOpenHelper
{
	private static StringBuilder Entradas = new StringBuilder();

	public DBB(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}


	@Override
	public void onCreate(SQLiteDatabase sqld)
	{
        Entradas.append("CREATE TABLE Entradas(id_Entrada INTEGER NOT NULL, fh_Entrada DATETIME NOT NULL, nb_Autor TEXT NOT NULL, ")
            .append("nb_Titulo TEXT NOT NULL, de_Contenido TEXT NOT NULL, PRIMARY KEY(id_Entrada))");

        sqld.execSQL(Entradas.toString());
	}


	@Override
	public void onUpgrade(SQLiteDatabase sqld, int oldVersion, int newVersion)
	{
        sqld.execSQL("DROP TABLE IF EXISTS Entradas");
        sqld.execSQL(Entradas.toString());
	}

}