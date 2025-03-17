package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import main.GestionLogin;
class GestionLoginTest {
    private GestionLogin gestionLogin;
    private static String TEST_FILE = "test_base_de_donnees.txt";

    @BeforeEach
    void setUp() {
        // Créer un fichier de test avant chaque test
        gestionLogin = new GestionLogin();
        GestionLogin.FICHIER_BASE_DONNEES = TEST_FILE; // Utiliser un fichier de test
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
    void testAjouterUtilisateur() throws IOException {
        // Ajouter un utilisateur
        gestionLogin.ajouterUtilisateur("testuser", "password123", "Test User", "test@example.com");

        // Vérifier que l'utilisateur a été ajouté au fichier
        String contenuFichier = Files.readString(Paths.get(TEST_FILE));
        assertTrue(contenuFichier.contains("testuser:password123:Test User:test@example.com"));
    }

    @Test
    void testVerifierLogin() {
        // Ajouter un utilisateur pour le test
        gestionLogin.ajouterUtilisateur("testuser", "password123", "Test User", "test@example.com");

        // Vérifier que le login fonctionne avec les bonnes informations
        assertTrue(gestionLogin.verifierLogin("test@example.com", "password123"));

        // Vérifier que le login échoue avec des informations incorrectes
        assertFalse(gestionLogin.verifierLogin("test@example.com", "wrongpassword"));
        assertFalse(gestionLogin.verifierLogin("wrong@example.com", "password123"));
    }

    @Test
    void testVerifierLoginAvecEmailInvalide() {
        // Vérifier que le login échoue avec un email invalide
        assertFalse(gestionLogin.verifierLogin("invalid-email", "password123"));
    }

    @Test
    void testAjouterUtilisateurAvecEmailInvalide() {
        // Vérifier qu'une exception est levée si l'email est invalide
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestionLogin.ajouterUtilisateur("testuser", "password123", "Test User", "invalid-email");
        });
        assertEquals("Email invalide", exception.getMessage());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Supprimer le fichier de test après chaque test
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
