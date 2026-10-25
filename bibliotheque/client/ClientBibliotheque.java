import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class ClientBibliotheque {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static int nombreEtapes = 0;
    private static int nombreConformes = 0;

    // Les 4 méthodes d'appel
    private static HttpResponse<String> get(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    private static HttpResponse<String> post(String chemin, String corpsJson) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .header("Content-Type", "Application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpsJson))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    private static HttpResponse<String> patch(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .header("Accept", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    private static HttpResponse<String> delete(String chemin) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + chemin))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // afficher()
    private static void afficher(String description, String methode, String chemin, HttpResponse<String> reponse, int codeAttendu) {
        nombreEtapes++;
        boolean estConforme = reponse.statusCode() == codeAttendu;
        if (estConforme){
            nombreConformes++;
        }
        System.out.println("=== Étape " + nombreEtapes + " : " + description + " ===");
        System.out.println(methode + " " + chemin + " → " + reponse.statusCode() + " (attendu : " + codeAttendu + ") " + (estConforme ? "CONFORME" : "NON CONFORME"));
        System.out.println("Réponse : " + reponse.body());
        System.out.println();

    }

    // main()
    public static void main(String[] args) throws Exception {
        // Lister les 3 premiers livres disponibles
        HttpResponse<String> rep1 = get("/livres/disponibles?size=3");
        afficher("Lister les 3 premiers livres disponibles", "GET", "/livres/disponibles?size=3", rep1, 200);

        // Lire un livre inexistant
        HttpResponse<String> rep2 = get("/livres/99");
        afficher("Lire un livre inexistant", "GET", "/livres/99", rep2, 404);

        // Créer l'auteur Jules Verne
        String jsonEtape3 = """
        {
          "nom": "Verne",
          "prenom": "Jules",
          "nationalite": "Française"
        }
        """;
        HttpResponse<String> rep3 = post("/auteurs", jsonEtape3);
        afficher("Créer l'auteur Jules Verne", "POST", "/auteurs", rep3, 201);

        // Emprunter le livre 2
        String jsonEtape4 = """
        {
          "livreId": 2,
          "emprunteur": "Alice Durand",
          "emailEmprunteur": "alice.durand@email.com"
        }
        """;
        HttpResponse<String> rep4 = post("/emprunts", jsonEtape4);
        afficher("Emprunter le livre 2", "POST", "/emprunts", rep4, 201);

        // Emprunter le livre 8 (stock à 0)
        String jsonEtape5 = """
        {
          "livreId": 8,
          "emprunteur": "Alice Durand",
          "emailEmprunteur": "alice.durand@email.com"
        }
        """;
        HttpResponse<String> rep5 = post("/emprunts", jsonEtape5);
        afficher("Emprunter le livre 8 (stock à 0)", "POST", "/emprunts", rep5, 409);

        // Rendre l'emprunt 2
        HttpResponse<String> rep6 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2", "PATCH", "/emprunts/2/retour", rep6, 200);

        // Rendre l'emprunt 2 une deuxième fois
        HttpResponse<String> rep7 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2 une deuxième fois", "PATCH", "/emprunts/2/retour", rep7, 409);

        // Supprimer l'auteur 1 (il a des livres)
        HttpResponse<String> rep8 = delete("/auteurs/1");
        afficher("Supprimer l'auteur 1 (il a des livres)", "DELETE", "/auteurs/1", rep8, 409);

        // Créer un livre invalide (titre vide, prix négatif)
        String jsonEtape9 = """
        {
          "titre": "",
          "isbn": "978-2-0000-0000-0",
          "prix": -5,
          "stock": 1,
          "auteurId": 1
        }
        """;
        HttpResponse<String> rep9 = post("/livres", jsonEtape9);
        afficher("Créer un livre invalide (titre vide, prix négatif)", "POST", "/livres", rep9, 400);

        // Supprimer l'auteur 5 (aucun livre)
        HttpResponse<String> rep10 = delete("/auteurs/5");
        afficher("Supprimer l'auteur 5 (aucun livre)", "DELETE", "/auteurs/5", rep10, 204);

        System.out.println("Bilan : " + nombreConformes + "/" + nombreEtapes + " étapes conformes.");
    }
    }
