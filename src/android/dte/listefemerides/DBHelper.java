package android.dte.listefemerides;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
	//private static final String DB_NAME = "efemerides.sqlite";

	private static final String TBL_EFEMERIDE = "efemerides";
	//campos de la tabla
	public static final String EFEMERIDE_TITULO = "_id";
	public static final String EFEMERIDE_CONTENIDO = "COMENTARIO";
	public static final String EFEMERIDE_IMAGEN = "jpg";
	public static final String EFEMERIDE_ANNO = "A";
	public static final String EFEMERIDE_MES = "M";
	public static final String EFEMERIDE_DIA = "D";
	public static final String EFEMERIDE_REL = "destacada";

	// Ruta por defecto de las bases de datos en el sistema Android
	private static String DB_PATH = "/data/data/android.dte.listefemerides/databases/";
	private static String DB_NAME = "efemerides.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DBHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;

	}

	/**
	 * Crea una base de datos vacía en el sistema y la reescribe con nuestro
	 * fichero de base de datos.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// la base de datos existe y no hacemos nada.
		} else {
			// Llamando a este método se crea la base de datos vacía en la ruta
			// por defecto del sistema
			// de nuestra aplicación por lo que podremos sobreescribirla con
			// nuestra base de datos.
			this.getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copiando Base de Datos");
			}
		}

	}

	/**
	 * Comprueba si la base de datos existe para evitar copiar siempre el
	 * fichero cada vez que se abra la aplicación.
	 * 
	 * @return true si existe, false si no existe
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// si llegamos aqui es porque la base de datos no existe todavía.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copia nuestra base de datos desde la carpeta assets a la recién creada
	 * base de datos en la carpeta de sistema, desde dónde podremos acceder a
	 * ella. Esto se hace con bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Abrimos el fichero de base de datos como entrada
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Ruta a la base de datos vacía recién creada
		String outFileName = DB_PATH + DB_NAME;

		// Abrimos la base de datos vacía como salida
		OutputStream myOutput = new FileOutputStream(outFileName);

		// Transferimos los bytes desde el fichero de entrada al de salida
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Liberamos los streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void open() throws SQLException {

		// Abre la base de datos
		try {
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}

		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public Efemeride getEfemerides(int num) {
		Calendar calendario = new GregorianCalendar();
		int mes = calendario.get(Calendar.MONTH) + 1;
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int total = 0;
		String elTitulo = "bla bla " + c.moveToFirst();
		Efemeride e = new Efemeride();
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				total++;
				if (total == num){
					e.setAnno(c.getInt(0));
					e.setDia(c.getInt(1));
					e.setDestacada(c.getInt(2));
					e.setMes(c.getInt(3));
					e.setImagen(c.getString(4));
					e.setHecho(c.getString(5));
					e.setComentario(c.getString(6));
				}
			} while (c.moveToNext());
		}
		return e;
	}

	public int getTotal() {
		Calendar calendario = new GregorianCalendar();
		int mes = calendario.get(Calendar.MONTH) + 1;
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int total = 0;
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				total++;
			} while (c.moveToNext());
		}

		return total;
	}
	
	public int getTotal(String dia, String mes) {

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int total = 0;
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				total++;
				// String email = c.getString(1);
			} while (c.moveToNext());
		}
		return total;
	}
	
	public EfemerideItem[] getEfemerides() {

		Calendar calendario = new GregorianCalendar();
		int mes = calendario.get(Calendar.MONTH) + 1;
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int ind = 0;
		EfemerideItem[] efItem = new EfemerideItem[getTotal()];
		EfemerideItem e;
		String fec = "";
		String imag = "";
		String tit = "";
		String sub = "";
		String elTitulo = "bla bla " + c.moveToFirst();
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {

				fec += c.getInt(1);// Dia
				// e.setDestacada(c.getInt(2));
				fec += "-" + c.getInt(3);// MEs
				fec += "-" + c.getInt(0);// Anno
				imag = c.getString(4);// Imagen
				// e.setImagen(c.getString(4));
				tit = c.getString(5);// titulo
				sub = c.getString(6);
				e = new EfemerideItem(tit, sub, fec, imag);
				efItem[ind++] = e;
				fec = "";
			} while (c.moveToNext());
		}

		return efItem;
	}

	public EfemerideItem[] getEfemerides(String dia, String mes) {

		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int ind = 0;
		EfemerideItem[] efItem = new EfemerideItem[getTotal(dia, mes)];
		EfemerideItem e;
		String fec = "";
		String imag = "";
		String tit = "";
		String sub = "";
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				fec += c.getInt(1);// Dia
				// e.setDestacada(c.getInt(2));
				fec += "-" + c.getInt(3);// MEs
				fec += "-" + c.getInt(0);// Anno
				imag = c.getString(4);// Imagen
				// e.setImagen(c.getString(4));
				tit = c.getString(5);// titulo
				sub = c.getString(6);
				e = new EfemerideItem(tit, sub, fec, imag);
				efItem[ind++] = e;
				fec = "";
			} while (c.moveToNext());
		}

		return efItem;
	}

	public int getRelevantes(int dia, int mes) {
		Cursor c = myDataBase.rawQuery("SELECT * FROM " + TBL_EFEMERIDE
				+ " WHERE " + EFEMERIDE_MES + " = " + mes + " AND "
				+ EFEMERIDE_DIA + " = " + dia, null);

		// Nos aseguramos de que existe al menos un registro
		int r = 0;
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				if (c.getInt(2) == 1) {
					r = 1;
					break;
				}
			} while (c.moveToNext());
		}
		return r;
	}
}
