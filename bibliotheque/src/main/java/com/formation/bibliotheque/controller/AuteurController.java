package com.formation.bibliotheque.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.bibliotheque.dto.AuteurRequest;
import com.formation.bibliotheque.dto.AuteurResponse;
import com.formation.bibliotheque.dto.LivreResponse;
import com.formation.bibliotheque.mapper.AuteurMapper;
import com.formation.bibliotheque.mapper.LivreMapper;
import com.formation.bibliotheque.model.Auteur;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.service.AuteurService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auteurs", description = "Gestion des auteurs")
@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {
    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    @Operation(summary = "Récupère une liste paginée de tous les auteurs.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste paginée des auteurs")
    })
    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(
            @PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page));
    }

    
    @Operation(summary = "Supprime un auteur (impossible s'il a encore des livres)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Auteur supprimé"),
        @ApiResponse(responseCode = "404", description = "Auteur inexistant"),
        @ApiResponse(responseCode = "409", description = "L'auteur a encore des livres")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        if (auteurService.aDesLivres(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        auteurService.supprimer(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupère un auteur par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Auteur trouvé"),
        @ApiResponse(responseCode = "404", description = "Auteur inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        return auteurService.recupererParId(id)
                .map(AuteurMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crée un nouvel auteur.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Auteur créé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur entite = AuteurMapper.versEntite(request);
        Auteur auteurCree = auteurService.creer(entite);
        AuteurResponse response = AuteurMapper.versResponse(auteurCree);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Met à jour un auteur par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Auteur mis à jour"),
        @ApiResponse(responseCode = "404", description = "Auteur inexistant"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id, @Valid @RequestBody AuteurRequest request) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        Auteur entiteModifiee = AuteurMapper.versEntite(request);
        return auteurService.recupererParId(id)
                .map(auteur -> auteurService.mettreAJour(auteur, entiteModifiee))
                .map(AuteurMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Récupère une liste paginée des livres d'un auteur.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste paginée des livres de l'auteur"),
        @ApiResponse(responseCode = "404", description = "Auteur inexistant")
    })
    @GetMapping("/{id}/livres")
    public ResponseEntity<PagedModel<LivreResponse>> listerLivres(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        if (!auteurService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        Page<Livre> page = auteurService.recupererLivres(id, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }
}
