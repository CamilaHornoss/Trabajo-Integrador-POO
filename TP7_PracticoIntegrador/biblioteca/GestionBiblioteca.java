package biblioteca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * Clase ejecutable que permite gestionar una biblioteca por consola:
 * alta de libros y socios, préstamos, devoluciones, consultas y listados.
 *
 * @author Equipo completo
 * @version TrabajoIntegrador
 */
public class GestionBiblioteca {

    /** Lector de teclado compartido por todo el programa. */
    private static Scanner teclado = new Scanner(System.in);

    /**
     * Punto de entrada del programa. Muestra el menú hasta que el usuario elige salir.
     *
     * @param args No se utilizan.
     */
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        int opcion;

        do {
            mostrarMenu(biblioteca);
            opcion = leerEntero("Opción: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    agregarLibro(biblioteca);
                    break;
                case 2:
                    agregarEstudiante(biblioteca);
                    break;
                case 3:
                    agregarDocente(biblioteca);
                    break;
                case 4:
                    prestarLibro(biblioteca);
                    break;
                case 5:
                    devolverLibro(biblioteca);
                    break;
                case 6:
                    System.out.println(biblioteca.listaDeSocios());
                    break;
                case 7:
                    System.out.println(biblioteca.listaDeLibros());
                    break;
                case 8:
                    System.out.println(biblioteca.listaDeTitulos());
                    break;
                case 9:
                    System.out.println(biblioteca.listaDeDocentesResponsables());
                    break;
                case 10:
                    quienTieneElLibro(biblioteca);
                    break;
                case 11:
                    listarPrestamosVencidos(biblioteca);
                    break;
                case 12:
                    ampliarDiasDocente(biblioteca);
                    break;
                case 13:
                    buscarSocio(biblioteca);
                    break;
                case 14:
                    cargarDatosDePrueba(biblioteca);
                    break;
                case 0:
                    System.out.println("Fin del programa.");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
            System.out.println();
        } while (opcion != 0);
    }

    /**
     * Muestra las opciones del menú principal.
     *
     * @param p_biblioteca Biblioteca que se gestiona (para mostrar su nombre).
     */
    private static void mostrarMenu(Biblioteca p_biblioteca) {
        System.out.println("========== " + p_biblioteca.getNombre() + " ==========");
        System.out.println(" 1) Agregar libro");
        System.out.println(" 2) Agregar socio Estudiante");
        System.out.println(" 3) Agregar socio Docente");
        System.out.println(" 4) Prestar libro");
        System.out.println(" 5) Devolver libro");
        System.out.println(" 6) Lista de socios");
        System.out.println(" 7) Lista de libros");
        System.out.println(" 8) Lista de títulos");
        System.out.println(" 9) Lista de docentes responsables");
        System.out.println("10) ¿Quién tiene un libro?");
        System.out.println("11) Préstamos vencidos");
        System.out.println("12) Ampliar días de préstamo a un docente responsable");
        System.out.println("13) Buscar socio por DNI");
        System.out.println("14) Cargar datos de prueba");
        System.out.println(" 0) Salir");
    }

    // ------------------------------------------------------------------
    // Opciones del menú
    // ------------------------------------------------------------------

    private static void agregarLibro(Biblioteca p_biblioteca) {
        String titulo = leerTexto("Título: ");
        int edicion = leerEntero("Edición: ");
        String editorial = leerTexto("Editorial: ");
        int anio = leerEntero("Año: ");
        p_biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
        System.out.println("Libro agregado.");
    }

    private static void agregarEstudiante(Biblioteca p_biblioteca) {
        int dni = leerEntero("DNI: ");
        if (p_biblioteca.buscarSocio(dni) != null) {
            System.out.println("Ya existe un socio con ese DNI.");
            return;
        }
        String nombre = leerTexto("Nombre y apellido: ");
        String carrera = leerTexto("Carrera: ");
        p_biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
        System.out.println("Estudiante agregado.");
    }

    private static void agregarDocente(Biblioteca p_biblioteca) {
        int dni = leerEntero("DNI: ");
        if (p_biblioteca.buscarSocio(dni) != null) {
            System.out.println("Ya existe un socio con ese DNI.");
            return;
        }
        String nombre = leerTexto("Nombre y apellido: ");
        String area = leerTexto("Área: ");
        p_biblioteca.nuevoSocioDocente(dni, nombre, area);
        System.out.println("Docente agregado.");
    }

    private static void prestarLibro(Biblioteca p_biblioteca) {
        Socio socio = pedirSocio(p_biblioteca);
        if (socio == null) {
            return;
        }
        Libro libro = elegirLibro(p_biblioteca);
        if (libro == null) {
            return;
        }
        if (libro.prestado()) {
            System.out.println("El libro ya está prestado.");
            return;
        }
        if (!socio.puedePedir()) {
            System.out.println(socio.getNombre() + " no puede pedir libros "
                    + "(tiene préstamos vencidos o alcanzó el límite).");
            return;
        }
        Calendar fechaRetiro = leerFecha("Fecha de retiro dd/mm/aaaa (Enter = hoy): ");
        if (p_biblioteca.prestarLibro(fechaRetiro, socio, libro)) {
            System.out.println("Préstamo registrado: " + libro.getTitulo()
                    + " a " + socio.getNombre() + " por " + socio.getDiasPrestamo() + " días.");
        } else {
            System.out.println("No se pudo realizar el préstamo.");
        }
    }

    private static void devolverLibro(Biblioteca p_biblioteca) {
        Libro libro = elegirLibro(p_biblioteca);
        if (libro == null) {
            return;
        }
        try {
            p_biblioteca.devolverLibro(libro);
            System.out.println("Libro devuelto: " + libro.getTitulo());
        } catch (LibroNoPrestadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void quienTieneElLibro(Biblioteca p_biblioteca) {
        Libro libro = elegirLibro(p_biblioteca);
        if (libro == null) {
            return;
        }
        try {
            System.out.println("El libro lo tiene: " + p_biblioteca.quienTieneElLibro(libro));
        } catch (LibroNoPrestadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listarPrestamosVencidos(Biblioteca p_biblioteca) {
        ArrayList<Prestamo> vencidos = p_biblioteca.prestamosVencidos();
        System.out.println("Préstamos vencidos: \n");
        if (vencidos.isEmpty()) {
            System.out.println("No hay préstamos vencidos.");
        }
        for (Prestamo prestamo : vencidos) {
            System.out.println("* " + prestamo.toString());
        }
    }

    private static void ampliarDiasDocente(Biblioteca p_biblioteca) {
        Socio socio = pedirSocio(p_biblioteca);
        if (socio == null) {
            return;
        }
        if (!(socio instanceof Docente)) {
            System.out.println("El socio no es Docente.");
            return;
        }
        Docente docente = (Docente) socio;
        if (!docente.esResponsable()) {
            System.out.println("El docente no es responsable, no se le pueden ampliar los días.");
            return;
        }
        int dias = leerEntero("Días a agregar: ");
        if (dias <= 0 || dias > Integer.MAX_VALUE - docente.getDiasPrestamo()) {
            System.out.println("Ingrese una ampliación positiva válida.");
            return;
        }
        docente.cambiarDiasDePrestamo(dias);
        System.out.println("Nuevo plazo de " + docente.getNombre() + ": "
                + docente.getDiasPrestamo() + " días.");
    }

    private static void buscarSocio(Biblioteca p_biblioteca) {
        Socio socio = pedirSocio(p_biblioteca);
        if (socio != null) {
            System.out.println(socio.toString());
        }
    }

    /**
     * Carga socios, libros y préstamos de ejemplo para probar rápidamente el sistema.
     * Incluye un préstamo viejo para que aparezca como vencido.
     */
    private static void cargarDatosDePrueba(Biblioteca p_biblioteca) {
        if (!p_biblioteca.getSocios().isEmpty() || !p_biblioteca.getLibros().isEmpty()) {
            System.out.println("Los datos de prueba se cargan solo en una biblioteca vacía.");
            return;
        }
        p_biblioteca.nuevoSocioDocente(14524782, "Juan Perez", "Matematica");
        p_biblioteca.nuevoSocioDocente(17982110, "Juan Fernandez", "Fisica");
        p_biblioteca.nuevoSocioEstudiante(28987498, "Francisco Paenza", "Sistemas");
        p_biblioteca.nuevoSocioEstudiante(31987123, "Cesar Milstein", "Quimica");

        p_biblioteca.nuevoLibro("JAVA. Como Programar", 1, "Pearson", 2010);
        p_biblioteca.nuevoLibro("Programando con JAVA", 2, "Anaya", 2012);
        p_biblioteca.nuevoLibro("Programando con JAVA", 2, "Anaya", 2012);
        p_biblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);

        ArrayList<Libro> libros = p_biblioteca.getLibros();
        int cant = libros.size();
        p_biblioteca.prestarLibro(new GregorianCalendar(2014, Calendar.OCTOBER, 15),
                p_biblioteca.buscarSocio(14524782), libros.get(cant - 4));
        p_biblioteca.prestarLibro(new GregorianCalendar(),
                p_biblioteca.buscarSocio(28987498), libros.get(cant - 3));
        System.out.println("Datos de prueba cargados (4 socios, 4 libros, 2 préstamos).");
    }

    // ------------------------------------------------------------------
    // Auxiliares de selección y lectura
    // ------------------------------------------------------------------

    /**
     * Pide un DNI y devuelve el socio correspondiente, o null si no existe.
     */
    private static Socio pedirSocio(Biblioteca p_biblioteca) {
        int dni = leerEntero("DNI del socio: ");
        Socio socio = p_biblioteca.buscarSocio(dni);
        if (socio == null) {
            System.out.println("No existe un socio con DNI " + dni + ".");
        }
        return socio;
    }

    /**
     * Muestra la lista de libros y pide elegir uno por su número.
     * Se elige por número porque puede haber varios ejemplares con el mismo título.
     *
     * @return El libro elegido, o null si no hay libros o el número es inválido.
     */
    private static Libro elegirLibro(Biblioteca p_biblioteca) {
        ArrayList<Libro> libros = p_biblioteca.getLibros();
        if (libros.isEmpty()) {
            System.out.println("La biblioteca no tiene libros.");
            return null;
        }
        System.out.println(p_biblioteca.listaDeLibros());
        int numero = leerEntero("Número de libro: ");
        if (numero < 1 || numero > libros.size()) {
            System.out.println("Número de libro inválido.");
            return null;
        }
        return libros.get(numero - 1);
    }

    private static String leerTexto(String p_mensaje) {
        System.out.print(p_mensaje);
        return teclado.nextLine().trim();
    }

    /**
     * Lee un número entero, repitiendo la pregunta hasta que el dato sea válido.
     */
    private static int leerEntero(String p_mensaje) {
        while (true) {
            String linea = leerTexto(p_mensaje);
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    /**
     * Lee una fecha con formato dd/mm/aaaa. Si se deja vacío, devuelve la fecha actual.
     */
    private static Calendar leerFecha(String p_mensaje) {
        while (true) {
            String linea = leerTexto(p_mensaje);
            if (linea.isEmpty()) {
                return new GregorianCalendar();
            }
            String[] partes = linea.split("/");
            try {
                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);
                GregorianCalendar fecha = new GregorianCalendar(anio, mes - 1, dia);
                fecha.setLenient(false);
                fecha.getTime(); // valida la fecha (por ejemplo, rechaza 31/02)
                return fecha;
            } catch (RuntimeException e) {
                System.out.println("Fecha inválida, use el formato dd/mm/aaaa.");
            }
        }
    }
}
