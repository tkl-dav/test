package main;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Cette classe gère la logique de sauvegarde et de lecture des informations de login.
 * Elle utilise une base de données SQLite pour stocker les utilisateurs et hache les mots de passe avec BCrypt.
 */
public class GestionLogin {
    public static String DATABASE_URL = "jdbc:sqlite:users.db";

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
    public void initialiserBaseDeDonnees() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "fullname TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "role TEXT DEFAULT 'user')"; 

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
        	System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            throw new RuntimeException("Erreur de base de données", e);
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
        	System.err.println("Erreur lors de la vérification du login : " + e.getMessage());
            throw new RuntimeException("Erreur de base de données", e);
        }
        return false;
    }

    /**
     * Vérifie si un utilisateur est un admin.
     *
     * @param email L'email de l'utilisateur.
     * @return true si l'utilisateur est un admin, false sinon.
     */
    public boolean estAdmin(String email) {
        String sql = "SELECT role FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                return "admin".equals(role); // Vérifie si le rôle est "admin"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
        	System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            throw new RuntimeException("Erreur de base de données", e);
        }
    }

    /**
     * Récupère la liste de tous les utilisateurs dans la base de données.
     *
     * @return Une liste de tableaux contenant les informations des utilisateurs.
     */
    public List<String[]> listerUtilisateurs() {
        List<String[]> utilisateurs = new ArrayList<>();
        String sql = "SELECT username, fullname, email, role FROM users";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String[] utilisateur = new String[]{
                        rs.getString("username"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("role")
                };
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param email       L'email de l'utilisateur à mettre à jour.
     * @param utilisateur Le nouveau nom d'utilisateur.
     * @param nomComplet  Le nouveau nom complet.
     * @param nouveauEmail Le nouvel email.
     */
    public void mettreAJourUtilisateur(String email, String utilisateur, String nomComplet, String nouveauEmail) {
        String sql = "UPDATE users SET username = ?, fullname = ?, email = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utilisateur);
            pstmt.setString(2, nomComplet);
            pstmt.setString(3, nouveauEmail);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    public void supprimerUtilisateur(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifie le mot de passe d'un utilisateur.
     *
     * @param email           L'email de l'utilisateur.
     * @param ancienMotDePasse L'ancien mot de passe pour confirmation.
     * @param nouveauMotDePasse Le nouveau mot de passe.
     * @return true si la modification est réussie, false sinon.
     */
    public boolean modifierMotDePasse(String email, String ancienMotDePasse, String nouveauMotDePasse) {
        if (!validerMotDePasse(nouveauMotDePasse)) {
            throw new IllegalArgumentException("Le nouveau mot de passe est invalide.");
        }

        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String motDePasseHache = rs.getString("password");
                if (BCrypt.checkpw(ancienMotDePasse, motDePasseHache)) {
                    String nouveauMotDePasseHache = hacherMotDePasse(nouveauMotDePasse);
                    String updateSql = "UPDATE users SET password = ? WHERE email = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, nouveauMotDePasseHache);
                        updateStmt.setString(2, email);
                        updateStmt.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Génère un mot de passe temporaire.
     *
     * @return Un mot de passe temporaire.
     */
    public String genererMotDePasseTemporaire() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
        Random random = new Random();
        StringBuilder motDePasse = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            motDePasse.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return motDePasse.toString();
    }

    /**
     * Réinitialise le mot de passe d'un utilisateur avec un mot de passe temporaire.
     *
     * @param email L'email de l'utilisateur.
     * @return Le mot de passe temporaire généré.
     */
    public String reinitialiserMotDePasse(String email) {
        String motDePasseTemporaire = genererMotDePasseTemporaire();
        String motDePasseHache = hacherMotDePasse(motDePasseTemporaire);

        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, motDePasseHache);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return motDePasseTemporaire;
    }

	public String[] lireUtilisateur(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}