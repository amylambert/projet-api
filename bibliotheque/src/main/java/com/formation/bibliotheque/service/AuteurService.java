import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.stereotype.Service;

import com.formation.bibliotheque.model.Auteur;
import com.formation.bibliotheque.repository.AuteurRepository;
import com.formation.bibliotheque.repository.LivreRepository;

@Service
public class AuteurService {
    private final LivreService livreService;
    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository, LivreService livreService) {
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
        auteurRepository.findById(id);
    }

    /**
    * @return True si l'id correspond à un auteur dans la table
    */
    public boolean existe(Long id) {
        if (auteurRepository.findById(id)) {
            return true;
        }
        return false;
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
    * @param id L'ID du auteur à modifier.
    * @param nouvellesValeurs Les nouvelles valeurs.
    * @return Optional contenant le auteur mis à jour, ou vide si l'ID n'existe
    pas.
    */
    public Auteur mettreAJour(Auteur auteurExistant, Auteur nouvellesValeurs) {
        return auteurRepository.map(auteurExistant -> {
                auteurExistant.setNom(nouvellesValeurs.getNom());
                auteurExistant.setDescription(nouvellesValeurs.getDescription());
                auteurExistant.setPrix(nouvellesValeurs.getPrix());
                auteurExistant.setStock(nouvellesValeurs.getStock());
                return auteurRepository.save(auteurExistant);
            }
        );
    }

    /**
    * Vérifie si un auteur a écrit des livres
    * @return un boolean
    */
    public boolean aDesLivres(Long id) {
        if (livreRepository.existsByAuteurId(id)) {
            return true;
        }
        return false;
    }

    /**
    * Supprime un auteur par son ID.
    */
    public void supprimer(Long id) {
        auteurRepository.deleteById(id);
    }
}