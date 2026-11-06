import java.util.Calendar;

/**
 * Representa el prestamo de un libro a un socio, con su fecha de retiro
 * y su fecha de devolucion (null mientras el libro no se devuelva).
 *
 * @author (Gonzalez Francisco)
 * @version TrabajoIntegrador
 */
public class Prestamo {
   /** Fecha en la que se retira el libro en el prestamo. */
   private Calendar fechaRetiro;
   /** Fecha en la que se devuelve el libro, Inicialmente es null hasta que se registre la devolucion. */
   private Calendar fechaDevolucion;
   /** Socio que solicita el prestamo. */
   private Socio socio;
   /** Libro a ser prestado. */
   private Libro libro;

   /**
    * Constructor de la clase Prestamo. Inicializa un nuevo préstamo asignando
    * una fecha de retiro, un socio y un libro. La fecha de devolución se inicializa
    * como null, indicando que el libro aún no ha sido devuelto.
    *
    * @param p_fechaRetiro Fecha en la que se retira el libro.
    * @param p_socio Socio que realiza el préstamo.
    * @param p_libro Libro que es prestado.
   */
   public Prestamo(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
      this.setFechaRetiro(p_fechaRetiro);
      this.setSocio(p_socio);
      this.setLibro(p_libro);
      this.setFechaDevolucion((Calendar)null); // Indica que el libro aún no ha sido devuelto.
   }

   /**
   * Establece la fecha de retiro del préstamo.
   *
   * @param p_fecha Fecha en la que se retira el libro.
   */
   private void setFechaRetiro(Calendar p_fecha) {
      this.fechaRetiro = p_fecha;
   }

   /**
   * Establece la fecha de devolución del préstamo.
   *
   * @param p_fecha Fecha en la que se devuelve el libro.
   */
   private void setFechaDevolucion(Calendar p_fecha) {
      this.fechaDevolucion = p_fecha;
   }

   /**
   * Establece el socio que realiza el préstamo.
   *
   * @param p_socio Objeto Socio que realiza el préstamo.
   */
   private void setSocio(Socio p_socio) {
      this.socio = p_socio;
   }

   /**
   * Establece el libro asociado al préstamo.
   *
   * @param p_libro Objeto Libro que se presta.
   */
   private void setLibro(Libro p_libro) {
      this.libro = p_libro;
   }

   /**
   * Obtiene la fecha de retiro del libro en el préstamo.
   *
   * @return Fecha de retiro del libro.
   */
   public Calendar getFechaRetiro() {
      return this.fechaRetiro;
   }

   /**
   * Obtiene la fecha de devolución del libro en el préstamo.
   *
   * @return Fecha de devolución del libro.
   */
   public Calendar getFechaDevolucion() {
      return this.fechaDevolucion;
   }

   /**
   * Obtiene el socio asociado a este préstamo.
   *
   * @return Objeto Socio que realiza el préstamo.
   */
   public Socio getSocio() {
      return this.socio;
   }

   /**
   * Obtiene el libro asociado a este préstamo.
   *
   * @return Objeto Libro prestado.
   */
   public Libro getLibro() {
      return this.libro;
   }

   /**
   * Registra la fecha de devolución del libro.
   *
   * @param p_fecha Fecha en la que el libro es devuelto.
   */
   public void registrarFechaDevolucion(Calendar p_fecha) {
      this.fechaDevolucion = p_fecha;
   }

   /**
    * Verifica si el prestamo se encuentra vencido a la fecha recibida.
    * La fecha de vencimiento es la fecha de retiro mas los dias de prestamo del socio.
    * Se trabaja sobre una copia de la fecha de retiro para no modificarla.
    *
    * @param p_fecha Fecha contra la que se compara (normalmente, la fecha actual).
    * @return true si p_fecha es posterior a la fecha de vencimiento, false en caso contrario.
    */
   public boolean vencido(Calendar p_fecha) {
      Calendar vencimiento = (Calendar) this.getFechaRetiro().clone();
      vencimiento.add(Calendar.DAY_OF_YEAR, this.getSocio().getDiasPrestamo());
      return p_fecha.after(vencimiento);
   }

   /**
    * Devuelve una fecha con el formato dd/mm/aaaa.
    *
    * @param p_fecha Fecha a formatear.
    * @return Fecha formateada, o "-" si la fecha es null.
    */
   private String formatearFecha(Calendar p_fecha) {
      if (p_fecha == null) {
         return "-";
      }
      return p_fecha.get(Calendar.DAY_OF_MONTH) + "/" + (p_fecha.get(Calendar.MONTH) + 1)
             + "/" + p_fecha.get(Calendar.YEAR);
   }

   /**
    * Devuelve una cadena con los datos del prestamo.
    *
    * @return Datos del prestamo.
    */
   public String toString(){
      return "Retiro: " + this.formatearFecha(this.getFechaRetiro())
             + " - Devolucion: " + this.formatearFecha(this.getFechaDevolucion())
             + " - Libro: " + this.getLibro().getTitulo()
             + " - Socio: " + this.getSocio().getNombre();
   }
}


 