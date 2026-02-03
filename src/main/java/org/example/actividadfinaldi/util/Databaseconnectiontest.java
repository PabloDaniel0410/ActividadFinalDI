package org.example.actividadfinaldi.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase DatabaseConnection
 * Nota: Estos tests requieren una conexión activa a Supabase
 */
@DisplayName("Tests de DatabaseConnection")
class Databaseconnectiontest {

    @Test
    @DisplayName("Encode codifica espacios correctamente")
    void testEncodeEspacios() {
        String resultado = DatabaseConnection.encode("texto con espacios");
        assertEquals("texto+con+espacios", resultado, "Los espacios deberían codificarse como +");
    }

    @Test
    @DisplayName("Encode codifica caracteres especiales")
    void testEncodeCaracteresEspeciales() {
        String resultado = DatabaseConnection.encode("texto@especial#");
        assertTrue(resultado.contains("%40"), "El @ debería codificarse");
        assertTrue(resultado.contains("%23"), "El # debería codificarse");
    }

    @Test
    @DisplayName("Encode maneja cadena vacía")
    void testEncodeCadenaVacia() {
        String resultado = DatabaseConnection.encode("");
        assertEquals("", resultado, "Una cadena vacía debería devolver cadena vacía");
    }

    @Test
    @DisplayName("Encode maneja DNI con letra")
    void testEncodeDni() {
        String resultado = DatabaseConnection.encode("12345678A");
        assertEquals("12345678A", resultado, "Un DNI simple no debería modificarse");
    }

    @Test
    @DisplayName("Encode maneja matrícula")
    void testEncodeMatricula() {
        String resultado = DatabaseConnection.encode("1234ABC");
        assertEquals("1234ABC", resultado, "Una matrícula simple no debería modificarse");
    }

    @Test
    @DisplayName("Encode maneja texto con tildes")
    void testEncodeTextoConTildes() {
        String resultado = DatabaseConnection.encode("José María");
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.length() > 0, "El resultado no debería estar vacío");
    }

    @Test
    @DisplayName("Encode maneja caracteres UTF-8")
    void testEncodeCaracteresUTF8() {
        String resultado = DatabaseConnection.encode("año niño");
        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(resultado.contains("+"), "Debería codificar el espacio");
    }

    @Test
    @DisplayName("closeConnection no lanza excepciones")
    void testCloseConnection() {
        assertDoesNotThrow(() -> DatabaseConnection.closeConnection(),
                "closeConnection no debería lanzar excepciones");
    }

    @Test
    @DisplayName("inicializarTablas imprime mensaje informativo")
    void testInicializarTablas() {
        assertDoesNotThrow(() -> DatabaseConnection.inicializarTablas(),
                "inicializarTablas no debería lanzar excepciones");
    }

    @Test
    @DisplayName("Encode maneja símbolos en póliza")
    void testEncodePoliza() {
        String resultado = DatabaseConnection.encode("POL-2024-001");
        assertEquals("POL-2024-001", resultado, "Una póliza con guiones no debería modificarse");
    }

    @Test
    @DisplayName("Encode es consistente con múltiples llamadas")
    void testEncodeConsistente() {
        String texto = "texto prueba";
        String resultado1 = DatabaseConnection.encode(texto);
        String resultado2 = DatabaseConnection.encode(texto);

        assertEquals(resultado1, resultado2, "Encode debería ser consistente");
    }
}