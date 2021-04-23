package android.dte.listefemerides;


import java.io.InputStream;
import android.app.Activity;
import android.dte.listefemerides.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Adaptador personalizado para cada efemeride
public class AdaptadorEfemerides extends ArrayAdapter {
	Activity context;
	EfemerideItem[] datos;

	AdaptadorEfemerides(Activity context, EfemerideItem[] datos) {
		super(context, R.layout.layout_item, datos);
		this.datos = datos;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//---inflar la vista con el layout del item
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.layout_item, null);
		
		//---Accedo al campo fecha y lo lleno
		TextView lblFecha = (TextView) item.findViewById(R.id.LblFecha);
		lblFecha.setText(datos[position].getFecha());
		
		//---Accedo al campo imagen y lo lleno
		ImageView img = (ImageView) item.findViewById(R.id.imgImag);
		InputStream in;
		try {
			String imagen = datos[position].getImagen(); //obtencion del nombre de la imagen
			imagen = imagen.toLowerCase();
			in = context.getAssets().open("images/"+imagen+".jpg"); //obtencion de fichero input de la imagen a partir de su nombre
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		img.setImageBitmap(bitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//---Accedo al campo titulo y lo lleno
		TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
		lblTitulo.setText(datos[position].getTitulo());
		
		//---Accedo al campo contenido y lo lleno
		TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);
		String contenido = datos[position].getSubtitulo();
		contenido = contenido.replace("<br />", "\n"); //cambio el cambio de linea html por cambio de linea
		lblSubtitulo.setText(contenido);
		
		return (item);
	}
}