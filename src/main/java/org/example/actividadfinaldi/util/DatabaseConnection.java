package org.example.actividadfinaldi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexion con Supabase PostgreSQL
 */
public class DatabaseConnection {

    // Configuracion Supabase - CAMBIAR por tus credenciales
    private static final String URL = "jdbc:postgresql://db.xxxxxxxxxxxx.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tu_password";

    private static Connection connection;

    /**
     * Obtiene la conexion a la base de datos
     * @return conexion activa
     * @throws SQLException si falla la conexion
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL no encontrado", e);
            }
        }
        return connection;
    }

    /**
     * Cierra la conexion activa
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inicializa las tablas en la base de datos
     * @throws SQLException si falla la creacion
     */
    public static void inicializarTablas() throws SQLException {
        Connection conn = getConnection();

        String createClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id SERIAL PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                dni VARCHAR(20) UNIQUE NOT NULL,
                fecha_nacimiento DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
        """;

        String createVehiculos = """
            CREATE TABLE IF NOT EXISTS vehiculos (
                id SERIAL PRIMARY KEY,
                matricula VARCHAR(20) UNIQUE NOT NULL,
                poliza_seguro VARCHAR(50) NOT NULL,
                tipo VARCHAR(20) NOT NULL,
                fecha_matriculacion DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
        """;

        String createAlquileres = """
            CREATE TABLE IF NOT EXISTS alquileres (
                id SERIAL PRIMARY KEY,
                cliente_id INTEGER REFERENCES clientes(id),
                vehiculo_id INTEGER REFERENCES vehiculos(id),
                fecha_inicio DATE NOT NULL,
                fecha_fin DATE NOT NULL,
                activo BOOLEAN DEFAULT true
            );
        """;

        conn.createStatement().execute(createClientes);
        conn.createStatement().execute(createVehiculos);
        conn.createStatement().execute(createAlquileres);
    }
}
