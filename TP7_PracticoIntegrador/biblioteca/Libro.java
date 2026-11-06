package biblioteca;


/**
 * Representa un Libro y su lista de sus Prestamos.
 * Permite consultar si el libro se encuentra prestado o no y quien lo tiene prestado.
 * 
 * @author Lombardi Lucas
 * @version 05/11/2026
 */
import java.util.*;
public class Libro implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Creacion de Atributos.
     */
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;
    
    /**
     * Clase construcora de Libro.
     * 
     * @param p_titulo Titulo del libro.
     * @param p_edicion Edicion del libro.
     * @param p_editorial Editorial del libro.
     * @param p_anio Año de publicacion del libro.
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
    }
    
    /**
     * Establece el titulo de libro.
     */
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }
    
    /**
     * Establece la edicion de la que es el libro.
     */
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }
    
    /**
     * Establece de que editorial viene el libro.
     */
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }
    
    /**
     * Establece el año de salida del libro.
     */
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }
    
    /**
     * Establece la lista de prestamos del libro.
     */
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }
    
    /**
     * Obtiene el titulo del libro.
     * 
     * @return Titulo del libro.
     */
    public String getTitulo(){
        return this.titulo;
    }
    
    /**
     * Obtiene la edicion del libro.
     * 
     * @return Edicion del libro.
     */
    public int getEdicion(){
        return this.edicion;
    }
    
    /**
     * Obtiene la editorial del libro.
     * 
     * @return Editorial del libro.
     */
    public String getEditorial(){
        return this.editorial;
    }
    
    /**
     * Obtiene el año de salida del libro.
     * 
     * @return año de salida.
     */
    public int getAnio(){
        return this.anio;
    }
    
    /**
     * Obtiene la lista de prestamos del libro.
     * 
     * @return lista de prestamos.
     */
    public ArrayList<Prestamo> getPrestamos(){
        return this.prestamos;
    }
    
    /**
     * Agrega prestamos a la lista de prestamos del libro.
     * 
     * @param p_prestamo Prestamo que se agregará.
     * @return true al agregar al Prestamo a la lista.
     */
    public boolean agregarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().add(p_prestamo);
    }
    
    /**
     * Quita prestamos del libro de la lista.
     * 
     * @param p_prestamo Prestamo que se quitará.
     * @return true si el Prestamo que se quiere quitar esta en al lista.
     */
    public boolean quitarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().remove(p_prestamo);
    }
    
    /**
     * Devuelve el ultimo prestamo del libro.
     * 
     * @return Ultimo Prestamo de la lista o null si no hay prestamos.
     */
    public Prestamo ultimoPrestamo(){
        if(this.getPrestamos().size() != 0){
            return this.getPrestamos().get(this.getPrestamos().size() -1);
        }else{
            return null;
        }
    }
    
    /**
     * Revisa si el ultimo libro se encuentra prestado actualmente.
     * 
     * @return true si el libro esta prestado y false si fue devuelto o nunca tuvo prestamo.
     */
    public boolean prestado(){
        if(this.ultimoPrestamo() != null){
            if(this.ultimoPrestamo().getFechaDevolucion() == null){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /**
     * Devuelve una cadena de texto con el titulo del Libro.
     */
    public String toString(){
        return "Titulo: " + this.getTitulo();
    }
}
