package com.formation.bibliotheque.service;

import com.formation.bibliotheque.model.Emprunt;
import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.repository.EmpruntRepository;
import com.formation.bibliotheque.repository.LivreRepository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmpruntService {
    /** Durée maximale d'un emprunt, en jours (RM5). */
    public static final int DUREE_MAX_JOURS = 14;
    
    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;

    public EmpruntService(EmpruntRepository empruntRepository, LivreRepository livreRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
    }
    
    public Page<Emprunt> recupererTous(Pageable pageable) {
        return empruntRepository.findAll(pageable);
    }

    public Optional<Emprunt>recupererParId(Long id) {
        return empruntRepository.findById(id);
    }

    public boolean livreExiste(Long livreId) {
        return livreRepository.existsById(livreId);
    }

    public boolean livreDisponible(Long livreId) {
        Optional<Livre> livre = livreRepository.findById(livreId);
        if (livre.isPresent() && livre.get().getStock() > 0) {
            return true;
        }
        return false;
    }

    public Emprunt creer(Emprunt emprunt) {
        emprunt.setId(null);
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(DUREE_MAX_JOURS));
        emprunt.setDateRetourEffective(null);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setRendu(false);
        Livre livre = livreRepository.findById(emprunt.getLivreId()).orElse(null);
        livre.setStock(livre.getStock() - 1);
        livreRepository.save(livre);
        return empruntRepository.save(emprunt);
    }

    public Emprunt retourner(Emprunt emprunt) {
        emprunt.setRendu(true);
        emprunt.setDateRetourEffective(LocalDate.now());

        Optional<Livre> livre = livreRepository.findById(emprunt.getLivreId());
        if (livre.isPresent()) {
            livre.get().setStock(livre.get().getStock() + 1);
            livreRepository.save(livre.get());
        }
        return empruntRepository.save(emprunt);
    }

    public Page<Emprunt> recupererEnCours(Pageable pageable) {
        return empruntRepository.findByRenduFalse(pageable);
    }

    public Page<Emprunt> recupererEnRetard(Pageable pageable) {
        return empruntRepository.findByRenduFalseAndDateRetourPrevueBefore(LocalDate.now(), pageable);
    }
}