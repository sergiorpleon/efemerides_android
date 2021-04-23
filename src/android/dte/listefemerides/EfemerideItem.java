package android.dte.listefemerides;

//clase para guardar los campos de la base de datos
public class EfemerideItem {
	private String fecha;
	private String imagen;
	private String titulo;
	private String subtitulo;

	public EfemerideItem(String tit, String sub, String fec, String imag) {
		titulo = tit;
		subtitulo = sub;
		fecha = fec;
		imagen = imag;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public String getFecha() {
		return fecha;
	}
	public String getImagen() {
		return imagen;
	}
}
