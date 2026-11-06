package biblioteca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Biblioteca: administra los libros, los socios y los préstamos.
 *
 * @author Equipo (cuarto grupo)
 * @version TrabajoIntegrador
 */
public class Biblioteca implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /** Nombre de la biblioteca. */
    private String nombre;
    /** Socios registrados en la biblioteca. */
    private ArrayList<Socio> socios;
    /** Libros (ejemplares) de la biblioteca. */
    private ArrayList<Libro> libros;

    /**
     * Crea una biblioteca vacía, sin socios ni libros.
     *
     * @param p_nombre Nombre de la biblioteca.
     */
    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
    }

    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    private void setSocios(ArrayList<Socio> p_socios) {
        this.socios = p_socios;
    }

    private void setLibros(ArrayList<Libro> p_libros) {
        this.libros = p_libros;
    }

    /**
     * @return Nombre de la biblioteca.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * @return Lista de socios de la biblioteca.
     */
    public ArrayList<Socio> getSocios() {
        return this.socios;
    }

    /**
     * @return Lista de libros de la biblioteca.
     */
    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    /**
     * Agrega un socio a la biblioteca.
     *
     * @param p_socio Socio a agregar.
     * @return true si se agregó.
     */
    public boolean agregarSocio(Socio p_socio) {
        return this.getSocios().add(p_socio);
    }

    /**
     * Quita un socio de la biblioteca.
     *
     * @param p_socio Socio a quitar.
     * @return true si estaba y se quitó.
     */
    public boolean quitarSocio(Socio p_socio) {
        return this.getSocios().remove(p_socio);
    }

    /**
     * Agrega un libro a la biblioteca.
     *
     * @param p_libro Libro a agregar.
     * @return true si se agregó.
     */
    public boolean agregarLibro(Libro p_libro) {
        return this.getLibros().add(p_libro);
    }

    /**
     * Quita un libro de la biblioteca.
     *
     * @param p_libro Libro a quitar.
     * @return true si estaba y se quitó.
     */
    public boolean quitarLibro(Libro p_libro) {
        return this.getLibros().remove(p_libro);
    }

    // ------------------------------------------------------------------
    // Altas
    // ------------------------------------------------------------------

    /**
     * Crea un libro y lo agrega a la biblioteca.
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        this.agregarLibro(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    /**
     * Crea un socio Estudiante y lo agrega a la biblioteca.
     */
    public void nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        this.agregarSocio(new Estudiante(p_dniSocio, p_nombre, p_carrera));
    }

    /**
     * Crea un socio Docente y lo agrega a la biblioteca.
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area) {
        this.agregarSocio(new Docente(p_dniSocio, p_nombre, p_area));
    }

    // ------------------------------------------------------------------
    // Préstamos y devoluciones
    // ------------------------------------------------------------------

    /**
     * Crea el préstamo y lo agrega en el libro y en el socio.
     * Devuelve false si el socio no puede pedir o si el libro ya está prestado.
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        if (p_socio.puedePedir() && !p_libro.prestado()) {
            Prestamo prestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);
            p_libro.agregarPrestamo(prestamo);
            p_socio.agregarPrestamo(prestamo);
            return true;
        }
        return false;
    }

    /**
     * Asigna la fecha de devolución (fecha actual) al último préstamo del libro.
     * Si el libro no está prestado, lanza LibroNoPrestadoException.
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + p_libro.getTitulo()
                    + " no se puede devolver ya que se encuentra en la biblioteca");
        }
        p_libro.ultimoPrestamo().registrarFechaDevolucion(new GregorianCalendar());
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    /**
     * Cantidad de socios del tipo indicado ("Docente" o "Estudiante").
     */
    public int cantidadDeSociosPorTipo(String p_objeto) {
        int cantidad = 0;
        for (Socio socio : this.getSocios()) {
            if (socio.soyDeLaClase().equals(p_objeto)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /**
     * Préstamos vencidos al día de la fecha (libros prestados con plazo vencido).
     */
    public ArrayList<Prestamo> prestamosVencidos() {
        ArrayList<Prestamo> vencidos = new ArrayList<Prestamo>();
        Calendar hoy = new GregorianCalendar();
        for (Libro libro : this.getLibros()) {
            if (libro.prestado()) {
                Prestamo prestamo = libro.ultimoPrestamo();
                if (prestamo.vencido(hoy)) {
                    vencidos.add(prestamo);
                }
            }
        }
        return vencidos;
    }

    /**
     * Docentes que nunca tuvieron ni tienen un préstamo vencido.
     */
    public ArrayList<Docente> docentesResponsables() {
        ArrayList<Docente> responsables = new ArrayList<Docente>();
        for (Socio socio : this.getSocios()) {
            if (socio instanceof Docente) {
                Docente docente = (Docente) socio;
                if (docente.esResponsable()) {
                    responsables.add(docente);
                }
            }
        }
        return responsables;
    }

    /**
     * Nombre del socio que tiene el libro. Si el libro está en la biblioteca,
     * lanza LibroNoPrestadoException.
     */
    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }
        return p_libro.ultimoPrestamo().getSocio().getNombre();
    }

    /**
     * Devuelve el socio con el DNI indicado, o null si no existe.
     */
    public Socio buscarSocio(int p_dni) {
        for (Socio socio : this.getSocios()) {
            if (socio.getDniSocio() == p_dni) {
                return socio;
            }
        }
        return null;
    }

    /**
     * Devuelve el primer libro con el título indicado, o null si no existe.
     * (Auxiliar para GestionBiblioteca.)
     */
    public Libro buscarLibro(String p_titulo) {
        for (Libro libro : this.getLibros()) {
            if (libro.getTitulo().equals(p_titulo)) {
                return libro;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Listados
    // ------------------------------------------------------------------

    /**
     * Formato1: listado de socios.
     */
    public String listaDeSocios() {
        String lista = "Lista de Socios: \n\n";
        int numero = 1;
        for (Socio socio : this.getSocios()) {
            lista += numero + ") " + socio.toString() + "\n";
            numero++;
        }
        lista += "**************************************\n";
        lista += "Cantidad de Socios del tipo Estudiante: "
                + cantidadDeSociosPorTipo("Estudiante") + "\n";
        lista += "Cantidad de Socios del tipo Docente: "
                + cantidadDeSociosPorTipo("Docente") + "\n";
        lista += "**************************************\n";
        return lista;
    }

    /**
     * Formato2: listado de libros.
     */
    public String listaDeLibros() {
        String lista = "Lista de Libros: \n\n";
        int numero = 1;
        for (Libro libro : this.getLibros()) {
            lista += numero + ") " + libro.toString() + " || Prestado: ("
                    + (libro.prestado() ? "Si" : "No") + ")\n";
            numero++;
        }
        return lista;
    }

    /**
     * Lista de títulos de la biblioteca, sin repetir (puede haber más de un ejemplar).
     */
    public String listaDeTitulos() {
        ArrayList<String> titulos = new ArrayList<String>();
        for (Libro libro : this.getLibros()) {
            if (!titulos.contains(libro.getTitulo())) {
                titulos.add(libro.getTitulo());
            }
        }
        String lista = "Lista de Titulos: \n\n";
        int numero = 1;
        for (String titulo : titulos) {
            lista += numero + ") " + titulo + "\n";
            numero++;
        }
        return lista;
    }

    /**
     * Formato3: listado de docentes responsables.
     */
    public String listaDeDocentesResponsables() {
        String lista = "Lista de Docentes Responsables: \n\n";
        for (Docente docente : docentesResponsables()) {
            lista += "* " + docente.toString() + "\n";
        }
        return lista;
    }
}
