package main;

/**import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
*/

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.Random;

/**
 * Cette classe gère la logique de sauvegarde et de lecture des informations de login.
 * Elle utilise une base de données SQLite pour stocker les utilisateurs et hache les mots de passe avec BCrypt.
 * Elle implémente les opérations CRUD pour gérer les utilisateurs et la réinitialisation du mot de passe.
 */
public class GestionLogin {
    private static final String DATABASE_URL = "jdbc:sqlite:users.db";
	public static String FICHIER_BASE_DONNEES;

    /**
     * Constructeur de la classe GestionLogin.
     * Initialise la base de données si elle n'existe pas.
     */
    public GestionLogin() {
        initialiserBaseDeDonnees();
    }

    /**
     * Initialise la base de données en créant la table `users` si elle n'existe pas.
     */
    private void initialiserBaseDeDonnees() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "fullname TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valide le format d'un email en utilisant une expression régulière.
     *
     * @param email L'email à valider.
     * @return true si l'email est valide, false sinon.
     */
    public boolean validerEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(EMAIL_REGEX);
    }

    /**
     * Valide la complexité d'un mot de passe en utilisant une expression régulière.
     *
     * @param motDePasse Le mot de passe à valider.
     * @return true si le mot de passe est valide, false sinon.
     */
    public boolean validerMotDePasse(String motDePasse) {
        String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*]).{12,}$";
        return motDePasse != null && motDePasse.matches(PASSWORD_REGEX);
    }

    /**
     * Hache un mot de passe avec BCrypt.
     *
     * @param motDePasse Le mot de passe à hacher.
     * @return Le mot de passe haché.
     */
    private String hacherMotDePasse(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }

    /**
     * Ajoute un nouvel utilisateur à la base de données.
     *
     * @param utilisateur Le nom d'utilisateur.
     * @param motDePasse  Le mot de passe.
     * @param nomComplet  Le nom complet de l'utilisateur.
     * @param email       L'email de l'utilisateur.
     * @throws IllegalArgumentException Si l'email ou le mot de passe est invalide.
     */
    public void ajouterUtilisateur(String utilisateur, String motDePasse, String nomComplet, String email) {
        if (!validerEmail(email)) {
            throw new IllegalArgumentException("Email invalide");
        }
        if (!validerMotDePasse(motDePasse)) {
            throw new IllegalArgumentException("Mot de passe invalide. Il doit contenir au moins 12 caractères, une majuscule, un chiffre et un caractère spécial (@#$%^&*).");
        }

        String motDePasseHache = hacherMotDePasse(motDePasse);
        String sql = "INSERT INTO users (username, password, fullname, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur);
            pstmt.setString(2, motDePasseHache);
            pstmt.setString(3, nomComplet);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère les informations d'un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur.
     * @return Un tableau de chaînes contenant [username, fullname, email], ou null si l'utilisateur n'existe pas.
     */
    public String[] lireUtilisateur(String email) {
        String sql = "SELECT username, fullname, email FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String[] utilisateur = new String[3];
                utilisateur[0] = rs.getString("username");
                utilisateur[1] = rs.getString("fullname");
                utilisateur[2] = rs.getString("email");
                return utilisateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param email       L'email de l'utilisateur à mettre à jour.
     * @param utilisateur Le nouveau nom d'utilisateur.
     * @param nomComplet  Le nouveau nom complet.
     * @param nouveauEmail Le nouvel email.
     * @return true si la mise à jour est réussie, false sinon.
     */
    public boolean mettreAJourUtilisateur(String email, String utilisateur, String nomComplet, String nouveauEmail) {
        if (!validerEmail(nouveauEmail)) {
            throw new IllegalArgumentException("Nouvel email invalide");
        }

        String sql = "UPDATE users SET username = ?, fullname = ?, email = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur);
            pstmt.setString(2, nomComplet);
            pstmt.setString(3, nouveauEmail);
            pstmt.setString(4, email);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     * @return true si la suppression est réussie, false sinon.
     */
    public boolean supprimerUtilisateur(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vérifie si les informations de login (email et mot de passe) sont valides.
     *
     * @param email      L'email de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @return true si les informations sont valides, false sinon.
     */
    public boolean verifierLogin(String email, String motDePasse) {
        if (!validerEmail(email)) {
            return false; // Email invalide
        }

        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String motDePasseHache = rs.getString("password");
                return BCrypt.checkpw(motDePasse, motDePasseHache); // Vérifie le mot de passe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Génère un mot de passe temporaire aléatoire.
     *
     * @return Un mot de passe temporaire.
     */
    private String genererMotDePasseTemporaire() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
        Random random = new Random();
        StringBuilder motDePasse = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            motDePasse.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return motDePasse.toString();
    }

    /**
     * Réinitialise le mot de passe d'un utilisateur et génère un mot de passe temporaire.
     *
     * @param email L'email de l'utilisateur.
     * @return Le mot de passe temporaire généré, ou null si l'utilisateur n'existe pas.
     */
    public String reinitialiserMotDePasse(String email) {
        if (!validerEmail(email)) {
            throw new IllegalArgumentException("Email invalide");
        }

        String motDePasseTemporaire = genererMotDePasseTemporaire();
        String motDePasseHache = hacherMotDePasse(motDePasseTemporaire);

        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, motDePasseHache);
            pstmt.setString(2, email);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                return motDePasseTemporaire; // Retourne le mot de passe temporaire
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Utilisateur non trouvé
    }

    /**
     * Permet à un utilisateur de définir un nouveau mot de passe après réinitialisation.
     *
     * @param email           L'email de l'utilisateur.
     * @param motDePasseActuel Le mot de passe temporaire actuel.
     * @param nouveauMotDePasse Le nouveau mot de passe.
     * @return true si la mise à jour est réussie, false sinon.
     */
    public boolean definirNouveauMotDePasse(String email, String motDePasseActuel, String nouveauMotDePasse) {
        if (!validerMotDePasse(nouveauMotDePasse)) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne respecte pas les règles de complexité.");
        }

        // Vérifie que le mot de passe temporaire est correct
        if (!verifierLogin(email, motDePasseActuel)) {
            return false;
        }

        String motDePasseHache = hacherMotDePasse(nouveauMotDePasse);
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, motDePasseHache);
            pstmt.setString(2, email);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}