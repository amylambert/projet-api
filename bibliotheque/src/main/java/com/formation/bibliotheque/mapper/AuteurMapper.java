package com.formation.bibliotheque.mapper;

import com.formation.bibliotheque.dto.AuteurResponse;
import com.formation.bibliotheque.dto.AuteurRequest;
import com.formation.bibliotheque.model.Auteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertit les objets des couches DTO à entity, et inversement
 * Logique de transformation
 */
public final class AuteurMapper {
    private AuteurMapper() {
    }

    public static Auteur versEntite(AuteurRequest request) {
        Auteur auteur = new Auteur();
        auteur.setId(null);
        auteur.setNom(request.getNom());
        auteur.setPrenom(request.getPrenom());
        auteur.setNationalite(request.getNationalite());

        return auteur;
    }

    public static AuteurResponse versResponse(Auteur auteur) {
        AuteurResponse response = new AuteurResponse(
                auteur.getId(),
                auteur.getNom(),
                auteur.getPrenom(),
                auteur.getNationalite()
        );
        return response;
    }

    public static List<AuteurResponse> versResponses(List<Auteur> auteurs) {
        List<AuteurResponse> responses = new ArrayList<AuteurResponse>();
        for (Auteur auteur : auteurs) {
            responses.add(versResponse(auteur));
        }
        return responses;
    }

    public static PagedModel<AuteurResponse> versPage(Page<Auteur> page) {
        // 1. On convertit le contenu de la page en DTOs
        List<AuteurResponse> contenu = versResponses(page.getContent());
        // 2. On reconstruit une page de DTOs avec les mêmes informations de pagination
        Page<AuteurResponse> pageDto = new PageImpl<>(contenu, page.getPageable(), page.getTotalElements());
        // 3. PagedModel produit un JSON stable : { "content": [...], "page": {...} }
        return new PagedModel<>(pageDto);
    }
}
