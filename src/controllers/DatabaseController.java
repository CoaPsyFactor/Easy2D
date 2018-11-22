package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * Kontroler konekcije sa bazom
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class DatabaseController extends GameController {
    /**
     * Korisnicko ime koje sluzi za konekciju na bazu
     */
    public final String DB_USER = "game";

    /**
     * Lozinka koja se koristi uz DB_USER i sluzi za logovanje na bazu
     */
    public final String DB_PASS = "Aa4svx7E9b64EMmL";

    /**
     * Adresa na kojoj se MySQL server nalazi
     */
    public final String DB_HOST = "localhost";

    /**
     * Ime baze na koju igrica treba da se konektuje
     */
    public final String DB_NAME = "game";

    /**
     * Konekcija na bazu
     */
    private Connection connection;

    public DatabaseController() {
        try {
            String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useUnicode=true&characterEncoding=UTF-8", DB_HOST, DB_NAME, DB_USER, DB_PASS);

            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Vraca trenutno aktivnu konekciju sa bazom
     *
     * @return trenutno aktvina konekcija
     */
    public Connection getConnection() {
        return connection;
    }
}
