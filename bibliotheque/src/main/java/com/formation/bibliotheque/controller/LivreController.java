package com.formation.bibliotheque.controller;

import com.formation.bibliotheque.dto.*;
import com.formation.bibliotheque.mapper.LivreMapper;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.service.LivreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    // EndPoint 7
    @GetMapping
    public ResponseEntity<PagedModel<LivreResponse>> listerTous(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererTous(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    // EndPoint 8
    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build(); //404
        }
        Livre livre = resultat.get();
        return ResponseEntity.ok(LivreMapper.versResponse(livre));
    }

    // EndPoint 9
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

    // EndPoint 10
    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(
            @PathVariable Long id,
            @Valid @RequestBody LivreRequest request) {

        if (livreService.existe(id)) {
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

    // EndPoint 11
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build(); //404
        }
        livreService.supprimer(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // EndPoint 12
    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        // Appeler le service pour chercher les livres
        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);

        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    // EndPoint 13
    @GetMapping("/disponibles")
    public ResponseEntity<PagedModel<LivreResponse>> listerDisponibles(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererDisponibles(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }


    // EndPoint 14
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