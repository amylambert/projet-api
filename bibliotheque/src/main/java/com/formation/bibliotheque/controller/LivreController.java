package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.*;
import com.formation.bibliotheque.mapper.LivreMapper;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.service.LivreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Livres", description = "Catalogue des livres")
@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @Operation(summary = "Récupère une liste paginée de tous les livres.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste paginée des livres")
    })
    @GetMapping
    public ResponseEntity<PagedModel<LivreResponse>> listerTous(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererTous(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    @Operation(summary = "Récupère un livre par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Livre trouvé"),
        @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build(); //404
        }
        Livre livre = resultat.get();
        return ResponseEntity.ok(LivreMapper.versResponse(livre));
    }

    @Operation(summary = "Crée un nouveau livre.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Livre créé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<LivreResponse> creer(@Valid @RequestBody LivreRequest request) {
        // 1. Vérifier, dans l'ordre du tableau
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build(); // 400
        }
        if (livreService.isbnExiste(request.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
        // 2. Agir
        Livre nouveauLivre = LivreMapper.versEntite(request);
        Livre livreSauvegarde = livreService.creer(nouveauLivre);
        // 3. Répondre
        return ResponseEntity.status(HttpStatus.CREATED).body(LivreMapper.versResponse(livreSauvegarde));
    }

    @Operation(summary = "Met à jour un livre par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Livre mis à jour"),
        @ApiResponse(responseCode = "404", description = "Livre inexistant"),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé par un autre livre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(
            @PathVariable Long id,
            @Valid @RequestBody LivreRequest request) {

        if (!livreService.existe(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build(); // 400
        }
        if (livreService.isbnUtiliseParUnAutreLivre(request.getIsbn(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
        Optional<Livre> resultat = livreService.recupererParId(id);
        Livre livreExistant = resultat.get();
        Livre nouvellesValeurs = LivreMapper.versEntite(request);
        Livre livreMisAJour = livreService.mettreAJour(livreExistant, nouvellesValeurs);

        return ResponseEntity.ok(LivreMapper.versResponse(livreMisAJour));

    }

    @Operation(summary = "Supprime un livre par son ID après vérification de son existence.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Livre supprimé"),
        @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build(); //404
        }
        livreService.supprimer(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @Operation(summary = "Recherche des livres par titre.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste paginée des livres correspondant à la recherche")
    })
    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        // Appeler le service pour chercher les livres
        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);

        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    @Operation(summary = "Récupère une liste paginée des livres disponibles.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste paginée des livres disponibles")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<PagedModel<LivreResponse>> listerDisponibles(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererDisponibles(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }


    @Operation(summary = "Ajoute du stock à un livre par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock ajouté au livre"),
        @ApiResponse(responseCode = "400", description = "Quantité invalide"),
        @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<LivreResponse> ajouterStock(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (quantite <= 0) {
            return ResponseEntity.badRequest().build(); //400
        }
        if (!livreService.existe(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        Livre livre = resultat.get();
        Livre livreMisAJour = livreService.ajouterStock(livre, quantite);

        return ResponseEntity.ok(LivreMapper.versResponse(livreMisAJour)); // 200 avec le livre mis à jour
    }
}