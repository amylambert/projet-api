import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientBibliotheque {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static int nombreEtapes = 0;
    private static int nombreConformes = 0;

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
                .header("Content-Type", "application/json")
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

    private static void afficher(String description, String methode, String chemin, HttpResponse<String> reponse,
            int codeAttendu) {
        nombreEtapes++;
        boolean estConforme = reponse.statusCode() == codeAttendu;
        if (estConforme) {
            nombreConformes++;
        }
        System.out.println("=== Étape " + nombreEtapes + " : " + description + " ===");
        System.out.println(methode + " " + chemin + " → " + reponse.statusCode() + " (attendu : " + codeAttendu + ") "
                + (estConforme ? "CONFORME" : "NON CONFORME"));
        System.out.println("Réponse : " + reponse.body());
        System.out.println();

    }

    // main()
    public static void main(String[] args) throws Exception {
        HttpResponse<String> rep1 = get("/livres/disponibles?size=3");
        afficher("Lister les 3 premiers livres disponibles", "GET", "/livres/disponibles?size=3", rep1, 200);

        HttpResponse<String> rep2 = get("/livres/99");
        afficher("Lire un livre inexistant", "GET", "/livres/99", rep2, 404);

        String jsonEtape3 = """
                {
                  "nom": "Verne",
                  "prenom": "Jules",
                  "nationalite": "Française"
                }
                """;
        HttpResponse<String> rep3 = post("/auteurs", jsonEtape3);
        afficher("Créer l'auteur Jules Verne", "POST", "/auteurs", rep3, 201);

        String jsonEtape4 = """
                {
                  "livreId": 2,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        HttpResponse<String> rep4 = post("/emprunts", jsonEtape4);
        afficher("Emprunter le livre 2", "POST", "/emprunts", rep4, 201);

        String jsonEtape5 = """
                {
                  "livreId": 8,
                  "emprunteur": "Alice Durand",
                  "emailEmprunteur": "alice.durand@email.com"
                }
                """;
        HttpResponse<String> rep5 = post("/emprunts", jsonEtape5);
        afficher("Emprunter le livre 8 (stock à 0)", "POST", "/emprunts", rep5, 409);

        HttpResponse<String> rep6 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2", "PATCH", "/emprunts/2/retour", rep6, 200);

        HttpResponse<String> rep7 = patch("/emprunts/2/retour");
        afficher("Rendre l'emprunt 2 une deuxième fois", "PATCH", "/emprunts/2/retour", rep7, 409);

        HttpResponse<String> rep8 = delete("/auteurs/1");
        afficher("Supprimer l'auteur 1 (il a des livres)", "DELETE", "/auteurs/1", rep8, 409);

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

        HttpResponse<String> rep10 = delete("/auteurs/5");
        afficher("Supprimer l'auteur 5 (aucun livre)", "DELETE", "/auteurs/5", rep10, 204);

        System.out.println("Bilan : " + nombreConformes + "/" + nombreEtapes + " étapes conformes.");
    }
}
