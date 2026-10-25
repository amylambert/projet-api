package com.formation.bibliotheque.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.bibliotheque.dto.EmpruntRequest;
import com.formation.bibliotheque.dto.EmpruntResponse;
import com.formation.bibliotheque.mapper.EmpruntMapper;
import com.formation.bibliotheque.model.Emprunt;
import com.formation.bibliotheque.service.EmpruntService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Emprunts", description = "Emprunts et retours des livres")
@RestController
@RequestMapping("/api/v1/emprunts")
public class EmpruntController {
    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    @Operation(summary = "Récupère une liste paginée de tous les emprunts.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste paginée des emprunts")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EmpruntResponse>> listerTous(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererTous(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @Operation(summary = "Récupère un emprunt par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emprunt trouvé"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponse> obtenirParId(@PathVariable Long id) {
        return empruntService.recupererParId(id)
                .map(EmpruntMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crée un nouvel emprunt.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Emprunt créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou livre inexistant"),
            @ApiResponse(responseCode = "409", description = "Le livre n'est pas disponible")
    })

    @PostMapping
    public ResponseEntity<EmpruntResponse> creer(@Valid @RequestBody EmpruntRequest request) {
        if (!empruntService.livreExiste(request.getLivreId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!empruntService.livreDisponible(request.getLivreId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Emprunt empruntCree = empruntService.creer(EmpruntMapper.versEntite(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpruntMapper.versResponse(empruntCree));
    }

    @Operation(summary = "Enregistre le retour d'un emprunt.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emprunt rendu"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant"),
            @ApiResponse(responseCode = "409", description = "L'emprunt a déjà été retourné")
    })
    @PatchMapping("/{id}/retour")
    public ResponseEntity<EmpruntResponse> retourner(@PathVariable Long id) {
        Optional<Emprunt> optionalEmprunt = empruntService.recupererParId(id);
        if (optionalEmprunt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Emprunt emprunt = optionalEmprunt.get();
        if (emprunt.getRendu()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Emprunt empruntRendu = empruntService.retourner(emprunt);

        return ResponseEntity.ok(EmpruntMapper.versResponse(empruntRendu));
    }

    @Operation(summary = "Récupère une liste paginée des emprunts en cours.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste paginée des emprunts en cours")
    })
    @GetMapping("/en-cours")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnCours(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnCours(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @Operation(summary = "Récupère une liste paginée des emprunts en retard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste paginée des emprunts en retard")
    })
    @GetMapping("/en-retard")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnRetard(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnRetard(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }
}