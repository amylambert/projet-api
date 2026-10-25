package com.formation.bibliotheque.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import com.formation.bibliotheque.dto.EmpruntRequest;
import com.formation.bibliotheque.dto.EmpruntResponse;
import com.formation.bibliotheque.model.Emprunt;

/**
 * Convertit les objets des couches DTO à entity, et inversement
 * Logique de transformation
 */
public final class EmpruntMapper {
    private EmpruntMapper() {
    }

    public static Emprunt versEntite(EmpruntRequest request) {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(null);
        emprunt.setLivreId(request.getLivreId());
        emprunt.setEmprunteur(request.getEmprunteur());
        emprunt.setEmailEmprunteur(request.getEmailEmprunteur());

        return emprunt;
    }

    public static EmpruntResponse versResponse(Emprunt emprunt) {
        boolean enRetard = false;
        if (!emprunt.getRendu() && emprunt.getDateRetourPrevue().isBefore(LocalDate.now())) {
            enRetard = true;
        }

        return new EmpruntResponse(
                emprunt.getId(),
                emprunt.getLivreId(),
                emprunt.getEmprunteur(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                emprunt.getDateRetourEffective(),
                emprunt.getRendu(),
                enRetard);
    }

    public static List<EmpruntResponse> versResponses(List<Emprunt> emprunts) {
        List<EmpruntResponse> responses = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            responses.add(versResponse(emprunt));
        }
        return responses;
    }

    public static PagedModel<EmpruntResponse> versPage(Page<Emprunt> page) {
        List<EmpruntResponse> contenu = versResponses(page.getContent());
        Page<EmpruntResponse> pageDto = new PageImpl<>(contenu, page.getPageable(), page.getTotalElements());

        return new PagedModel<>(pageDto);
    }

}
