package biblioteca;

import java.io.*;
import java.nio.file.*;

final class ArchivoBiblioteca {
    private ArchivoBiblioteca() {}

    static Biblioteca cargar(Path archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo))) {
            entrada.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                    "maxdepth=30;maxrefs=200000;maxbytes=20000000;biblioteca.*;java.base/*;!*"));
            Object datos = entrada.readObject();
            if (!(datos instanceof Biblioteca)) {
                throw new IOException("El archivo no contiene una biblioteca.");
            }
            return (Biblioteca) datos;
        } catch (NoSuchFileException e) {
            return new Biblioteca("Biblioteca Central");
        }
    }

    static void guardar(Path archivo, Biblioteca biblioteca) throws IOException {
        Path destino = archivo.toAbsolutePath();
        Files.createDirectories(destino.getParent());
        Path temporal = Files.createTempFile(destino.getParent(), "biblioteca-", ".tmp");
        try {
            try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(temporal))) {
                salida.writeObject(biblioteca);
            }
            Files.move(temporal, destino, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
}
