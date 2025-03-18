package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import main.GestionLogin;

class GestionLoginTest {
    private GestionLogin gestionLogin;
    private static final String TEST_DATABASE_URL = "jdbc:sqlite:test_users.db";

    @BeforeEach
    void setUp() {
        // Utiliser une base de données de test
        GestionLogin.DATABASE_URL = TEST_DATABASE_URL;
        gestionLogin = new GestionLogin();
        gestionLogin.initialiserBaseDeDonnees(); // Crée la table si elle n'existe pas
    }

    @Test
    void testValiderEmail() {
        // Emails valides
        assertTrue(gestionLogin.validerEmail("test@example.com"));
        assertTrue(gestionLogin.validerEmail("user.name+tag+sorting@example.com"));
        assertTrue(gestionLogin.validerEmail("user@sub.example.com"));

        // Emails invalides
        assertFalse(gestionLogin.validerEmail("test@example")); // Manque le domaine de haut niveau
        assertFalse(gestionLogin.validerEmail("test.example.com")); // Manque le @
        assertFalse(gestionLogin.validerEmail("test@.com")); // Domaine vide
    }

    @Test
    void testValiderMotDePasse() {
        // Mots de passe valides
        assertTrue(gestionLogin.validerMotDePasse("Password123@"));
        assertTrue(gestionLogin.validerMotDePasse("SecurePass123#"));

        // Mots de passe invalides
        assertFalse(gestionLogin.validerMotDePasse("password")); // Manque une majuscule, un chiffre et un caractère spécial
        assertFalse(gestionLogin.validerMotDePasse("Pass123")); // Moins de 12 caractères
        assertFalse(gestionLogin.validerMotDePasse("password123")); // Manque un caractère spécial
    }

    @Test
    void testAjouterUtilisateur() {
        // Ajouter un utilisateur valide
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Vérifier que l'utilisateur a été ajouté
        List<String[]> utilisateurs = gestionLogin.listerUtilisateurs();
        assertFalse(utilisateurs.isEmpty());
        assertEquals("testuser", utilisateurs.get(0)[0]); // Vérifie le nom d'utilisateur
    }

    @Test
    void testLireUtilisateur() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Lire les informations de l'utilisateur
        String[] utilisateur = gestionLogin.lireUtilisateur("test@example.com");
        assertNotNull(utilisateur);
        assertEquals("testuser", utilisateur[0]); // Vérifie le nom d'utilisateur
        assertEquals("Test User", utilisateur[1]); // Vérifie le nom complet
        assertEquals("test@example.com", utilisateur[2]); // Vérifie l'email
    }

    @Test
    void testMettreAJourUtilisateur() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Mettre à jour l'utilisateur
        gestionLogin.mettreAJourUtilisateur("test@example.com", "newuser", "New User", "new@example.com");

        // Vérifier que l'utilisateur a été mis à jour
        String[] utilisateur = gestionLogin.lireUtilisateur("new@example.com");
        assertNotNull(utilisateur);
        assertEquals("newuser", utilisateur[0]); // Vérifie le nouveau nom d'utilisateur
        assertEquals("New User", utilisateur[1]); // Vérifie le nouveau nom complet
    }

    @Test
    void testSupprimerUtilisateur() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Supprimer l'utilisateur
        gestionLogin.supprimerUtilisateur("test@example.com");

        // Vérifier que l'utilisateur a été supprimé
        String[] utilisateur = gestionLogin.lireUtilisateur("test@example.com");
        assertNull(utilisateur);
    }

    @Test
    void testModifierMotDePasse() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Modifier le mot de passe
        boolean resultat = gestionLogin.modifierMotDePasse("test@example.com", "Password123@", "NewPassword123@");
        assertTrue(resultat);

        // Vérifier que le mot de passe a été modifié
        boolean loginReussi = gestionLogin.verifierLogin("test@example.com", "NewPassword123@");
        assertTrue(loginReussi);
    }

    @Test
    void testReinitialiserMotDePasse() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Réinitialiser le mot de passe
        String motDePasseTemporaire = gestionLogin.reinitialiserMotDePasse("test@example.com");
        assertNotNull(motDePasseTemporaire);

        // Vérifier que le mot de passe temporaire fonctionne
        boolean loginReussi = gestionLogin.verifierLogin("test@example.com", motDePasseTemporaire);
        assertTrue(loginReussi);
    }

    @Test
    void testVerifierLogin() {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "Password123@", "Test User", "test@example.com");

        // Vérifier que le login fonctionne avec les bonnes informations
        assertTrue(gestionLogin.verifierLogin("test@example.com", "Password123@"));

        // Vérifier que le login échoue avec des informations incorrectes
        assertFalse(gestionLogin.verifierLogin("test@example.com", "WrongPassword"));
    }

    @Test
    void testListerUtilisateurs() {
        // Ajouter plusieurs utilisateurs
        gestionLogin.ajouterUtilisateur("user1", "Password123@", "User One", "user1@example.com");
        gestionLogin.ajouterUtilisateur("user2", "Password456#", "User Two", "user2@example.com");

        // Lister les utilisateurs
        List<String[]> utilisateurs = gestionLogin.listerUtilisateurs();
        assertEquals(2, utilisateurs.size()); // Vérifie que deux utilisateurs sont retournés
    }
}