package com.aluracursos.challenge_literatura.ui;

import com.aluracursos.challenge_literatura.model.Autor;
import com.aluracursos.challenge_literatura.model.Idioma;
import com.aluracursos.challenge_literatura.model.Libro;
import com.aluracursos.challenge_literatura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@Component
public class MenuPrincipal {

    // ─── Constantes de opciones ───────────────────────────────────────────────
    private static final int BUSCAR_LIBRO         = 1;
    private static final int LISTAR_LIBROS        = 2;
    private static final int LISTAR_AUTORES       = 3;
    private static final int AUTORES_VIVOS        = 4;
    private static final int LIBROS_POR_IDIOMA    = 5;
    private static final int ESTADISTICAS         = 6;
    private static final int SALIR                = 0;

    @Autowired
    private LibroService libroService;

    private final Scanner scanner = new Scanner(System.in);

    // ─── Punto de entrada ─────────────────────────────────────────────────────

    /**
     * Muestra el menú principal en un bucle hasta que el usuario elige salir.
     */
    public void mostrarMenu() {
        imprimirBienvenida();

        int opcion;
        do {
            imprimirOpciones();
            opcion = leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != SALIR);

        System.out.println("\n  ¡Hasta luego! Gracias por usar LiterAlura.\n");
        scanner.close();
    }

    // ─── Lógica del menú ──────────────────────────────────────────────────────

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case BUSCAR_LIBRO      -> buscarLibroPorTitulo();
            case LISTAR_LIBROS     -> listarLibrosRegistrados();
            case LISTAR_AUTORES    -> listarAutoresRegistrados();
            case AUTORES_VIVOS     -> listarAutoresVivosEnAnio();
            case LIBROS_POR_IDIOMA -> listarLibrosPorIdioma();
            case ESTADISTICAS      -> mostrarEstadisticas();
            case SALIR             -> {}  // manejado en el bucle principal
            default                -> System.out.println("\n  ⚠  Opción inválida. Ingrese un número del 0 al 6.\n");
        }
    }

    // ─── Opción 1: Buscar libro por título ────────────────────────────────────

    private void buscarLibroPorTitulo() {
        imprimirSeparador("BUSCAR LIBRO POR TÍTULO");
        System.out.print("  Ingrese el título (o parte de él): ");
        String titulo = scanner.nextLine().trim();

        if (titulo.isBlank()) {
            System.out.println("  ⚠  Debe ingresar un título válido.");
            return;
        }

        System.out.println("  🔍 Buscando en Gutendex...");
        Optional<Libro> resultado = libroService.buscarYGuardarLibro(titulo);

        resultado.ifPresentOrElse(
                libro -> {
                    System.out.println("\n  ✔  Libro encontrado y guardado:");
                    libro.imprimirInformacion();
                },
                () -> System.out.println("\n  ✗  No se encontró ningún libro con ese título en la API.\n")
        );
    }

    // ─── Opción 2: Listar libros registrados ──────────────────────────────────

    private void listarLibrosRegistrados() {
        imprimirSeparador("LIBROS REGISTRADOS EN EL CATÁLOGO");
        List<Libro> libros = libroService.listarTodosLosLibros();

        if (libros.isEmpty()) {
            System.out.println("  No hay libros registrados aún. Use la opción 1 para buscar.\n");
            return;
        }

        System.out.printf("  Total: %d libro(s) — ordenados por número de descargas%n%n", libros.size());
        libros.forEach(Libro::imprimirInformacion);
    }

    // ─── Opción 3: Listar autores registrados ─────────────────────────────────

    private void listarAutoresRegistrados() {
        imprimirSeparador("AUTORES REGISTRADOS EN EL CATÁLOGO");
        List<Autor> autores = libroService.listarTodosLosAutores();

        if (autores.isEmpty()) {
            System.out.println("  No hay autores registrados aún.\n");
            return;
        }

        System.out.printf("  Total: %d autor(es) — ordenados alfabéticamente%n%n", autores.size());
        autores.forEach(Autor::imprimirInformacion);
    }

    // ─── Opción 4: Autores vivos en un año ───────────────────────────────────

    private void listarAutoresVivosEnAnio() {
        imprimirSeparador("AUTORES VIVOS EN UN DETERMINADO AÑO");
        System.out.print("  Ingrese el año: ");

        int anio = leerEntero();
        if (anio <= 0) {
            System.out.println("  ⚠  Año inválido.\n");
            return;
        }

        List<Autor> autores = libroService.listarAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.printf("  No se encontraron autores vivos en el año %d en el catálogo.%n%n", anio);
        } else {
            System.out.printf("%n  Autores vivos en %d (%d encontrado(s)):%n%n", anio, autores.size());
            autores.forEach(Autor::imprimirInformacion);
        }
    }

    // ─── Opción 5: Libros por idioma ─────────────────────────────────────────

    private void listarLibrosPorIdioma() {
        imprimirSeparador("LIBROS POR IDIOMA");
        Idioma.listarOpciones();
        System.out.print("\n  Ingrese el código del idioma: ");
        String codigo = scanner.nextLine().trim().toLowerCase();

        Idioma idiomaSeleccionado = Idioma.fromCodigo(codigo);
        if (idiomaSeleccionado == Idioma.nd) {
            System.out.println("  ⚠  Código de idioma no reconocido.\n");
            return;
        }

        List<Libro> libros = libroService.listarLibrosPorIdioma(codigo);

        if (libros.isEmpty()) {
            System.out.printf("  No hay libros en %s registrados en el catálogo.%n%n",
                    idiomaSeleccionado.getNombreCompleto());
        } else {
            System.out.printf("%n  Libros en %s (%d encontrado(s)):%n",
                    idiomaSeleccionado.getNombreCompleto(), libros.size());
            libros.forEach(Libro::imprimirInformacion);
        }
    }

    // ─── Opción 6: Estadísticas ───────────────────────────────────────────────

    private void mostrarEstadisticas() {
        imprimirSeparador("ESTADÍSTICAS DEL CATÁLOGO");

        long totalLibros  = libroService.contarLibros();
        long totalAutores = libroService.contarAutores();

        System.out.printf("  📚 Total de libros registrados:   %d%n", totalLibros);
        System.out.printf("  ✍  Total de autores registrados:  %d%n%n", totalAutores);

        List<Object[]> estadisticas = libroService.estadisticasPorIdioma();
        if (!estadisticas.isEmpty()) {
            System.out.println("  Distribución por idioma:");
            estadisticas.forEach(fila -> {
                Idioma idioma = (Idioma) fila[0];
                Long cantidad  = (Long) fila[1];
                System.out.printf("   %-20s → %d libro(s)%n",
                        idioma.getNombreCompleto(), cantidad);
            });
        }
        System.out.println();
    }

    // ─── Helpers de UI ───────────────────────────────────────────────────────

    private void imprimirBienvenida() {
        System.out.println();
        System.out.println("  ╔═════════════════════════════════════════════╗");
        System.out.println("  ║          📚  BIENVENIDO A LiterAlura  📚    ║");
        System.out.println("  ║      Catálogo interactivo de libros          ║");
        System.out.println("  ╚═════════════════════════════════════════════╝");
        System.out.println();
    }

    private void imprimirOpciones() {
        System.out.println("  ─────────────────────────────────────────────");
        System.out.println("   Elija la opción a través de su número:");
        System.out.println("  ─────────────────────────────────────────────");
        System.out.println("   1 - Buscar libro por título");
        System.out.println("   2 - Listar libros registrados");
        System.out.println("   3 - Listar autores registrados");
        System.out.println("   4 - Listar autores vivos en un determinado año");
        System.out.println("   5 - Listar libros por idioma");
        System.out.println("   6 - Estadísticas del catálogo");
        System.out.println("   0 - Salir");
        System.out.println("  ─────────────────────────────────────────────");
        System.out.print("   → ");
    }

    private void imprimirSeparador(String titulo) {
        System.out.println();
        System.out.println("  ══════════════════════════════════════════════");
        System.out.println("   " + titulo);
        System.out.println("  ══════════════════════════════════════════════");
    }

    private int leerOpcion() {
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
