package mx.cutg.blogger.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ManejadorDB
{
	private DBB DB;
	private SQLiteDatabase mine;
	private Cursor resultset;

	public ManejadorDB(Context contexto, String nb_DataBase)
	{
		DB = new DBB(contexto, nb_DataBase, null, 1);
	}


	//METODO PARA ABRIR LA BD.. FALSE LECTURA, TRUE ESCRITURA
    @SuppressWarnings("unused")
	public void AbrirDB(boolean modo)
	{
		if(modo){
            mine = DB.getWritableDatabase();
        }
		else{
            mine = DB.getReadableDatabase();
        }
	}


	//METODO QUE EJECUTA UNA CONSULTA
    @SuppressWarnings("unused")
	public void consulta(StringBuilder consulta)
	{
		resultset = mine.rawQuery(consulta.toString(), null);
	}


	//METODO QUE EJECUTA UNA TRANSACCION
    @SuppressWarnings("unused")
	public void transaccion(StringBuilder accion)
	{
		mine.execSQL(accion.toString());
	}


	//METODO QUE REGRESA EL CURSOR
    @SuppressWarnings("unused")
	public Cursor ObtenerCursor()
	{
		return resultset;
	}


    //METODO QUE VERIFICA SI PUEDE MOVER EL CURSOR A LA PRIMERA POSICION
    @SuppressWarnings("unused")
    public boolean moveToFirst()
    {
        return resultset != null && resultset.getCount() != 0 && resultset.moveToFirst();
    }

	
	//METODO QUE RETORNA EL OBJETO DATABASE
    @SuppressWarnings("unused")
	public SQLiteDatabase ObtenerTransaccion()
	{
		return mine;
	}
	
	
	//METODO QUE REGRESA EL OBJETO BDD
    @SuppressWarnings("unused")
	public DBB Obtener_DBB()
	{
		return DB;
	}


    //METODO QUE INICIA UNA TRANSACCION
    @SuppressWarnings("unused")
    public void transaccionStart()
    {
        if(mine != null){
            mine.beginTransaction();
        }
    }


    //METODO QUE HACE COMIT A UNA TRANSACCION
    @SuppressWarnings("unused")
    public void transaccionCommit()
    {
        if(mine != null && mine.inTransaction()){
            mine.setTransactionSuccessful();
        }
    }


    //METODO QUE CIERRA UNA TRANSACCION
    @SuppressWarnings("unused")
    public void transaccionClose()
    {
        if(mine != null && mine.inTransaction()){
            mine.endTransaction();
        }
    }


	//METODO QUE CIERRA EL CURSOR
	public void CerrarCursor()
	{
		if(resultset != null && !resultset.isClosed())
			resultset.close();
	}
	
	
	//METODO QUE CIERRA LA BD
	public void CerrarBD()
	{
		if(mine!=null && mine.isOpen())
			mine.close();
	}

	
	//METODO QUE OBTIENE EL NEXT ID DE UNA TABLA Y CAMPO EN ESPECIFICO
    @SuppressWarnings("unused")
	public int NextID(String columna, String tabla, String where, boolean closeDB)
	{
		int nextID;
		
		try
		{
			StringBuilder str = new StringBuilder();
			
			if(where == null){
				str.append("SELECT MAX(").append(columna).append(") FROM ").append(tabla);
			}
			else{
				str.append("SELECT MAX(").append(columna).append(") FROM ").append(tabla).append(" WHERE ").append(where);
			}
			
			//ABRIMOS DB Y CONSULTAMOS
			if(mine == null || !mine.isOpen()){
				mine = DB.getReadableDatabase();
			}
				
			CerrarCursor();
			resultset = mine.rawQuery(str.toString(), null);
			
			if(resultset.moveToFirst()){
				nextID = resultset.getInt(0) + 1;
			}
			else{
				nextID = 0;
			}
		}
		catch(SQLiteException ex)
		{
			nextID = 0;
		}
		finally
		{
			CerrarCursor();
			
			if(closeDB){
                CerrarBD();
            }
		}
		
		return nextID;
	}
	
	
}//CIERRA CLASE
