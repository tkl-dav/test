package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {

    // URL de la base de données SQLite
    private static final String URL = "jdbc:sqlite:utilisateurs.db"; // Nom du fichier de base de données

    // Méthode pour obtenir la connexion
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Charger le driver SQLite JDBC
            Class.forName("org.sqlite.JDBC");

            // Établir la connexion à la base de données
            connection = DriverManager.getConnection(URL);
            System.out.println("Connexion à la base de données réussie !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données !");
            e.printStackTrace();
        }

        return connection;
    }

    // Méthode pour fermer la connexion
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion !");
            e.printStackTrace();
        }
    }
}
