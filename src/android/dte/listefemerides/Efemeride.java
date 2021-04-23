package android.dte.listefemerides;

//clase para guardar en un objeo los campos de la base de datos
public class Efemeride {
    private int anno;
    private int mes;
    private int dia;
    private int destacada;
    private String imagen;
    private String hecho;
    private String comentario;
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getDestacada() {
		return destacada;
	}
	public void setDestacada(int destacada) {
		this.destacada = destacada;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getHecho() {
		return hecho;
	}
	public void setHecho(String hecho) {
		this.hecho = hecho;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
