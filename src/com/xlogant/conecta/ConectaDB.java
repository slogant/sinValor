package com.xlogant.conecta;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona el pool de conexiones a la base de datos usando HikariCP.
 * La configuración se carga desde el archivo db.properties.
 *
 * @author oscar
 */
public final class ConectaDB {

    private static final HikariDataSource dataSource;

    static {
        Properties props = new Properties();
        // Carga las propiedades desde el archivo db.properties en el classpath
        try (InputStream input = ConectaDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Lo siento, no se pudo encontrar el archivo db.properties");
                // En una aplicación real, lanzar una excepción aquí es una buena idea.
                throw new IllegalStateException("No se pudo encontrar db.properties en el classpath");
            }
            props.load(input);
        } catch (IOException ex) {
            // Ocurrió un error al leer el archivo
            throw new RuntimeException("Error al cargar db.properties", ex);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("jdbcUrl"));
        config.setUsername(props.getProperty("dbUser"));
        config.setPassword(props.getProperty("dbPassword"));

        // Propiedades opcionales de HikariCP desde el archivo
        config.setPoolName(props.getProperty("hikari.poolName", "DefaultPool"));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimumIdle", "2")));
        config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connectionTimeout", "30000")));
        config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idleTimeout", "600000")));
        config.setMaxLifetime(Long.parseLong(props.getProperty("hikari.maxLifetime", "1800000")));

        dataSource = new HikariDataSource(config);
    }

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private ConectaDB() {}

    /**
     * Obtiene una conexión del pool de conexiones.
     * @return una Connection lista para ser usada.
     * @throws SQLException si ocurre un error al obtener la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
