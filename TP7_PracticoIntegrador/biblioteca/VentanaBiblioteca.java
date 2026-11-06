package biblioteca;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.function.Consumer;
import javax.swing.*;

public final class VentanaBiblioteca {
    private final Biblioteca biblioteca;
    private final Path archivo;
    private final JFrame ventana = new JFrame("Gestión de Biblioteca");
    private final JComboBox<Socio> socios = new JComboBox<>();
    private final JComboBox<String> ejemplares = new JComboBox<>();
    private final JTextArea resultado = new JTextArea(17, 64);

    private VentanaBiblioteca(Biblioteca biblioteca, Path archivo) {
        this.biblioteca = biblioteca;
        this.archivo = archivo;
        JPanel selectores = new JPanel(new GridLayout(2, 2, 6, 6));
        selectores.add(new JLabel("Socio"));
        selectores.add(socios);
        selectores.add(new JLabel("Ejemplar"));
        selectores.add(ejemplares);

        JPanel acciones = new JPanel(new GridLayout(0, 3, 6, 6));
        boton(acciones, "Nuevo libro", this::altaLibro);
        boton(acciones, "Nuevo estudiante", () -> altaSocio(false));
        boton(acciones, "Nuevo docente", () -> altaSocio(true));
        boton(acciones, "Prestar", this::prestar);
        boton(acciones, "Devolver", this::devolver);
        boton(acciones, "Quién lo tiene", this::consultarTitular);
        boton(acciones, "Socios", () -> mostrar(biblioteca.listaDeSocios()));
        boton(acciones, "Libros", () -> mostrar(biblioteca.listaDeLibros()));
        boton(acciones, "Títulos", () -> mostrar(biblioteca.listaDeTitulos()));
        boton(acciones, "Docentes responsables", () -> mostrar(biblioteca.listaDeDocentesResponsables()));
        boton(acciones, "Vencidos", this::mostrarVencidos);
        boton(acciones, "Ampliar plazo", this::ampliarPlazo);
        boton(acciones, "Guardar", () -> {
            if (guardar()) {
                mostrar("Datos guardados en " + archivo);
            }
        });

        resultado.setEditable(false);
        ventana.add(selectores, BorderLayout.NORTH);
        ventana.add(new JScrollPane(resultado), BorderLayout.CENTER);
        ventana.add(acciones, BorderLayout.SOUTH);
        ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evento) {
                if (guardar()) {
                    ventana.dispose();
                }
            }
        });
        actualizar();
        mostrar(biblioteca.listaDeLibros());
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        Path archivo = Path.of(System.getProperty("user.home"), ".tp7-biblioteca", "biblioteca.dat");
        SwingUtilities.invokeLater(() -> {
            try {
                new VentanaBiblioteca(ArchivoBiblioteca.cargar(archivo), archivo);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "No se pudo abrir " + archivo + "\n" + e.getMessage(),
                        "Error de lectura", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void boton(JPanel panel, String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(evento -> {
            try {
                accion.run();
            } catch (IllegalArgumentException e) {
                mostrar(e.getMessage());
            }
        });
        panel.add(boton);
    }

    private void formulario(String titulo, Consumer<String[]> accion, String... etiquetas) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField[] campos = new JTextField[etiquetas.length];
        for (int i = 0; i < etiquetas.length; i++) {
            campos[i] = new JTextField(18);
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        while (JOptionPane.showConfirmDialog(ventana, panel, titulo, JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {
            try {
                String[] valores = new String[campos.length];
                for (int i = 0; i < campos.length; i++) {
                    valores[i] = campos[i].getText().trim();
                    if (valores[i].isEmpty()) {
                        throw new IllegalArgumentException("Completá el campo " + etiquetas[i] + ".");
                    }
                }
                accion.accept(valores);
                actualizar();
                return;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(ventana, e.getMessage());
            }
        }
    }

    private int enteroPositivo(String texto, String campo) {
        try {
            int valor = Integer.parseInt(texto);
            if (valor > 0) {
                return valor;
            }
        } catch (NumberFormatException e) {
            // Se informa el mismo mensaje para texto, desbordamiento y valores no positivos.
        }
        throw new IllegalArgumentException(campo + " debe ser un número entero positivo.");
    }

    private void altaLibro() {
        formulario("Nuevo libro", valores -> {
            biblioteca.nuevoLibro(valores[0], enteroPositivo(valores[1], "Edición"),
                    valores[2], enteroPositivo(valores[3], "Año"));
            mostrar(biblioteca.listaDeLibros());
        }, "Título", "Edición", "Editorial", "Año");
    }

    private void altaSocio(boolean docente) {
        formulario(docente ? "Nuevo docente" : "Nuevo estudiante", valores -> {
            int dni = enteroPositivo(valores[0], "DNI");
            if (biblioteca.buscarSocio(dni) != null) {
                throw new IllegalArgumentException("Ya existe un socio con ese DNI.");
            }
            if (docente) {
                biblioteca.nuevoSocioDocente(dni, valores[1], valores[2]);
            } else {
                biblioteca.nuevoSocioEstudiante(dni, valores[1], valores[2]);
            }
            mostrar(biblioteca.listaDeSocios());
        }, "DNI", "Nombre y apellido", docente ? "Área" : "Carrera");
    }

    private void prestar() {
        Socio socio = socioSeleccionado();
        Libro libro = libroSeleccionado();
        if (biblioteca.prestarLibro(Calendar.getInstance(), socio, libro)) {
            actualizar();
            mostrar(libro.getTitulo() + " prestado a " + socio.getNombre() + ".");
        } else {
            mostrar("El ejemplar está prestado o el socio alcanzó su cupo o tiene un préstamo vencido.");
        }
    }

    private void devolver() {
        Libro libro = libroSeleccionado();
        try {
            biblioteca.devolverLibro(libro);
            actualizar();
            mostrar("Devolución registrada de " + libro.getTitulo() + ".");
        } catch (LibroNoPrestadoException e) {
            mostrar(e.getMessage());
        }
    }

    private void consultarTitular() {
        try {
            mostrar("Tiene el ejemplar " + biblioteca.quienTieneElLibro(libroSeleccionado()) + ".");
        } catch (LibroNoPrestadoException e) {
            mostrar(e.getMessage());
        }
    }

    private void mostrarVencidos() {
        StringBuilder listado = new StringBuilder();
        for (Prestamo prestamo : biblioteca.prestamosVencidos()) {
            listado.append(prestamo).append("\n\n");
        }
        mostrar(listado.length() == 0 ? "No hay préstamos vencidos." : listado.toString());
    }

    private void ampliarPlazo() {
        Socio socio = socioSeleccionado();
        if (!(socio instanceof Docente) || !((Docente) socio).esResponsable()) {
            mostrar("La ampliación requiere seleccionar un docente responsable.");
            return;
        }
        Docente docente = (Docente) socio;
        formulario("Ampliar plazo", valores -> {
            int dias = enteroPositivo(valores[0], "Días");
            if (dias > Integer.MAX_VALUE - docente.getDiasPrestamo()) {
                throw new IllegalArgumentException("La cantidad de días es demasiado grande.");
            }
            docente.cambiarDiasDePrestamo(dias);
            mostrar("Nuevo plazo para futuros préstamos: " + docente.getDiasPrestamo() + " días.");
        }, "Días adicionales");
    }

    private Libro libroSeleccionado() {
        int indice = ejemplares.getSelectedIndex();
        if (indice < 0) {
            throw new IllegalArgumentException("Seleccioná un ejemplar.");
        }
        return biblioteca.getLibros().get(indice);
    }

    private Socio socioSeleccionado() {
        Socio socio = (Socio) socios.getSelectedItem();
        if (socio == null) {
            throw new IllegalArgumentException("Seleccioná un socio.");
        }
        return socio;
    }

    private void actualizar() {
        Object socioElegido = socios.getSelectedItem();
        int ejemplarElegido = ejemplares.getSelectedIndex();
        socios.removeAllItems();
        ejemplares.removeAllItems();
        for (Socio socio : biblioteca.getSocios()) {
            socios.addItem(socio);
        }
        int numero = 1;
        for (Libro libro : biblioteca.getLibros()) {
            ejemplares.addItem(numero++ + ") " + libro.getTitulo()
                    + (libro.prestado() ? " (prestado)" : " (disponible)"));
        }
        if (socioElegido != null) {
            socios.setSelectedItem(socioElegido);
        }
        if (ejemplarElegido >= 0 && ejemplarElegido < ejemplares.getItemCount()) {
            ejemplares.setSelectedIndex(ejemplarElegido);
        }
    }

    private boolean guardar() {
        try {
            ArchivoBiblioteca.guardar(archivo, biblioteca);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ventana, "No se guardaron los cambios. " + e.getMessage(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void mostrar(String texto) {
        resultado.setText(texto);
        resultado.setCaretPosition(0);
    }
}
