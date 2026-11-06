package biblioteca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Clase que representa a un socio en un sistema de préstamos de libros.
 * Un socio tiene un DNI, un nombre, un plazo de días para cada préstamo,
 * y una lista de préstamos activos.
 * @author (Gonzalez Francisco)
 * @version
 */

public abstract class Socio implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

   /** DNI del socio */
   private int dniSocio;

   /** Nombre del socio */
   private String nombre;

   /** Número de días permitidos para cada préstamo */
   private int diasPrestamo;

   /** Lista de préstamos activos del socio */
   private ArrayList<Prestamo> prestamos;

   /**
   * Constructor de la clase Socio. Inicializa los atributos del socio
   * y crea una lista vacía de préstamos.
   *
   * @param p_dniSocio DNI del socio.
   * @param p_nombre Nombre del socio.
   * @param p_diasPrestamo Días permitidos para cada préstamo.
   */
   public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo) {
      this.setDniSocio(p_dniSocio);
      this.setNombre(p_nombre);
      this.setDiasPrestamo(p_diasPrestamo);
      this.setPrestamos(new ArrayList<Prestamo>());
   }

   /**
   * Establece el DNI del socio.
   *
   * @param p_dniSocio DNI del socio.
   */
   private void setDniSocio(int p_dniSocio) {
      this.dniSocio = p_dniSocio;
   }

   /**
   * Establece el nombre del socio.
   *
   * @param p_nombre Nombre del socio.
   */
   private void setNombre(String p_nombre) {
      this.nombre = p_nombre;
   }

   /**
   * Establece la cantidad de días permitidos para cada préstamo.
   *
   * @param p_diasPrestamo Número de días permitidos.
   */
   public void setDiasPrestamo(int p_diasPrestamo) {
      this.diasPrestamo = p_diasPrestamo;
   }

   /**
   * Establece la lista de préstamos del socio.
   *
   * @param p_prestamos Lista de préstamos.
   */
   private void setPrestamos(ArrayList<Prestamo> p_prestamos) {
      this.prestamos = p_prestamos;
   }

   /**
   * Obtiene el DNI del socio.
   *
   * @return DNI del socio.
   */
   public int getDniSocio() {
      return this.dniSocio;
   }

   /**
   * Obtiene el nombre del socio.
   *
   * @return Nombre del socio.
   */
   public String getNombre() {
      return this.nombre;
   }

   /**
   * Obtiene la cantidad de días permitidos para cada préstamo.
   *
   * @return Número de días permitidos.
   */
   public int getDiasPrestamo() {
      return this.diasPrestamo;
   }

   /**
   * Obtiene la lista de préstamos activos del socio.
   *
   * @return Lista de préstamos.
   */
   public ArrayList<Prestamo> getPrestamos() {
      return this.prestamos;
   }

   /**
   * Agrega un préstamo a la lista de préstamos del socio.
   *
   * @param p_prestamo Préstamo a agregar.
   * @return `true` si el préstamo se agregó exitosamente, `false` en caso contrario.
   */
   public boolean agregarPrestamo(Prestamo p_prestamo) {
      return this.getPrestamos().add(p_prestamo);
   }

   /**
   * Elimina un préstamo de la lista de préstamos del socio.
   *
   * @param p_prestamo Préstamo a eliminar.
   * @return `true` si el préstamo se eliminó exitosamente, `false` si no se encontraba en la lista.
   */
   public boolean quitarPrestamo(Prestamo p_prestamo) {
      return this.getPrestamos().remove(p_prestamo);
   }

   /**
    * Calcula y devuelve la cantidad de libros en poder del socio.
    * Si getFechaDevolucion == null quiere decir que el libro sigue estando "prestado", por ende el contador aumenta.
    * @return cantidad de libros prestados.
    */
   public int cantLibrosPrestados(){
      int libros = 0;

      for(Prestamo unPrestamo: this.getPrestamos()){
          if(unPrestamo.getFechaDevolucion() == null){
          libros ++;
          }
      }

      return libros;
   }

   /**
    * Devuelve una cade de caracteres con informacion relacionada al socio.
    * @return cadena de caracteres.
    */
   public String toString() {
      return "D.N.I.: " + this.getDniSocio() + " || " + this.getNombre() + " (" + this.soyDeLaClase() + ") || Libros Prestados: " + this.cantLibrosPrestados();
   }

   /**
    * Determina si el socio puede pedir un libro: no debe tener en su poder
    * ningun libro con el plazo de devolucion vencido a la fecha actual.
    * Solo se revisan los prestamos sin devolver (fechaDevolucion == null).
    * @return true en caso de no encontrar ningun prestamo vencido, y false en caso contrario.
    */
   public boolean puedePedir() {
      Calendar fechaHoy = new GregorianCalendar();

      for(Prestamo unPrestamo: this.getPrestamos()){
          if(unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(fechaHoy)){
              return false;
          }
      }
      return true;
   }

   public abstract String soyDeLaClase();
}


