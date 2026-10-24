package com.formation.bibliotheque.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.formation.bibliotheque.model.Auteur;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.repository.AuteurRepository;
import com.formation.bibliotheque.repository.LivreRepository;

@Service
public class AuteurService {
    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
    }

    /**
    * Retourne tous les auteurs.
    */
    public Page<Auteur> recupererTous(Pageable pageable){
        return auteurRepository.findAll(pageable);
    }

    /**
    * Retourne un auteur par son ID.
    * @return Optional vide si le auteur n'existe pas.
    */
    public Optional<Auteur> recupererParId(Long id) {
        return auteurRepository.findById(id);
    }

    /**
    * @return True si l'id correspond à un auteur dans la table
    */
    public boolean existe(Long id) {
        return auteurRepository.existsById(id);
    }

    /**
    * Crée un nouveau auteur.
    * @param auteur L'auteur à créer (sans ID).
    * @return L'auteur créé (avec ID généré).
    */ 
    public Auteur creer(Auteur auteur) {
        // On s'assure que l'ID est null pour forcer une création (pas une mise à jour)
        auteur.setId(null);
        return auteurRepository.save(auteur);
    }

    /**
    * Met à jour un auteur existant.
    * @param nouvellesValeurs Les nouvelles valeurs.
    * @return L'auteur mis à jour.
    */
    public Auteur mettreAJour(Auteur auteurExistant, Auteur nouvellesValeurs) {
        auteurExistant.setNom(nouvellesValeurs.getNom());
        auteurExistant.setPrenom(nouvellesValeurs.getPrenom());
        auteurExistant.setNationalite(nouvellesValeurs.getNationalite());
        return auteurRepository.save(auteurExistant);
    }

    /**
    * Vérifie si un auteur a écrit des livres
    * @return un boolean
    */
    public boolean aDesLivres(Long id) {
        return livreRepository.existsByAuteurId(id);
    }

    /**
    * Supprime un auteur par son ID.
    */
    public void supprimer(Long id) {
        auteurRepository.deleteById(id);
    }

    /**
    * Récupère les livres d'un auteur par son ID.
    * @param id L'ID de l'auteur.
    */
    public Page<Livre> recupererLivres(Long id, Pageable pageable) {
        return livreRepository.findByAuteurId(id, pageable);
    }
}