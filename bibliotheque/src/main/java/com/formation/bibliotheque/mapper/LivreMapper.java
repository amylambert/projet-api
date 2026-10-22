package com.formation.bibliotheque.mapper;

import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.dto.LivreResponse;
import com.formation.bibliotheque.dto.LivreRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertit les objets des couches DTO à entity, et inversement
 * Logique de transformation
 */
public final class LivreMapper {
    private LivreMapper() {}

    public static Livre versEntite(LivreRequest request){
        Livre livre = new Livre();
        livre.setAuteurId(request.getAuteurId());
        livre.setId(null);
        livre.setTitre(request.getTitre());
        livre.setIsbn(request.getIsbn());
        livre.setPrix(request.getPrix());
        livre.setAnneePublication(request.getAnneePublication());
        livre.setStock(request.getStock());

        return livre;
    }

    public static LivreResponse versResponse(Livre livre){
        LivreResponse response = new LivreResponse(
                livre.getId(),
                livre.getTitre(),
                livre.getIsbn(),
                livre.getPrix(),
                livre.getAnneePublication(),
                livre.getStock(),
                livre.getAuteurId()
        );
        return response;
    }

    public static List<LivreResponse> versResponses(List<Livre> livres){
        List<LivreResponse> responses = new ArrayList<>();
        for(Livre livre : livres){
            responses.add(versResponse(livre));
        }
        return responses;
    }

    public static PagedModel<LivreResponse> versPage(Page<Livre> page) {
        List<LivreResponse> contenu = versResponses(page.getContent());
        Page<LivreResponse> pageDto = new PageImpl<>(contenu, page.getPageable(), page.getTotalElements());

        return new PagedModel<>(pageDto);
    }
}
