package com.aluracursos.challenge_literatura.model;

/**
 * Enumeración de idiomas soportados por la API Gutendex.
 * Los códigos de dos letras corresponden al estándar ISO 639-1.
 */
public enum Idioma {

    es("Español"),
    en("Inglés"),
    fr("Francés"),
    pt("Portugués"),
    de("Alemán"),
    it("Italiano"),
    la("Latín"),
    zh("Chino"),
    ru("Ruso"),
    ar("Árabe"),
    nd("No disponible");

    private final String nombreCompleto;

    Idioma(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Convierte un código de idioma (String) al enum correspondiente.
     * Si no existe coincidencia, retorna {@code nd} (No disponible).
     *
     * @param codigo código ISO 639-1 del idioma (ej. "es", "en")
     * @return el enum {@code Idioma} correspondiente
     */
    public static Idioma fromCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) return nd;
        for (Idioma idioma : Idioma.values()) {
            if (idioma.name().equalsIgnoreCase(codigo.trim())) {
                return idioma;
            }
        }
        return nd;
    }

    /**
     * Imprime en consola todos los idiomas disponibles con su código y nombre.
     */
    public static void listarOpciones() {
        System.out.println("\n  Idiomas disponibles:");
        for (Idioma idioma : Idioma.values()) {
            if (idioma != nd) {
                System.out.printf("   [%s] - %s%n", idioma.name(), idioma.getNombreCompleto());
            }
        }
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}
