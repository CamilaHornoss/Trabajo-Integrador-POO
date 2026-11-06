package biblioteca;

/**
 * Excepción que se lanza cuando se intenta operar sobre un libro como si
 * estuviera prestado (devolverlo o consultar quién lo tiene) pero el libro
 * se encuentra en la biblioteca.
 *
 * @author Equipo
 * @version TrabajoIntegrador
 */
public class LibroNoPrestadoException extends Exception {

    /**
     * Crea la excepción con el mensaje indicado.
     *
     * @param p_mensaje Descripción del error.
     */
    public LibroNoPrestadoException(String p_mensaje) {
        super(p_mensaje);
    }
}
