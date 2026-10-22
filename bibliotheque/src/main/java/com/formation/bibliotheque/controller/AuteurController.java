import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {
    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    // Écrivez ici les méthodes des endpoints 1 à 5
    // Endpoint 1 — GET /api/v1/auteurs : une liste paginée
    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(
            @PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page)); // 200
    }

    // Endpoint 5 — DELETE /api/v1/auteurs/{id} : deux vérifications, puis l'action
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        // 1. Vérifier, dans l'ordre du tableau
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        if (auteurService.aDesLivres(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 (RM3)
        }
        // 2. Agir
        auteurService.supprimer(id);
        // 3. Répondre
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        return auteurService.recupererParId(id)
                .map(AuteurMapper.versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur entite = AuteurMapper.versEntite(request);
        Auteur auteurCree = auteurService.creer(entite);
        AuteurResponse response = AuteurMapper.versResponse(auteurCree);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id, @Valid @RequestBody AuteurRequesst request) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        Auteur entite = AuteurMapper.versEntite(request);
        return auteurService.mettreAJour(id, entite)
                .map(AuteurMapper::versResponse)
                .map(ResponseEntity.ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
