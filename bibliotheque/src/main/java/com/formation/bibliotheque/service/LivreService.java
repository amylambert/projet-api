import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.stereotype.Service;

import com.formation.bibliotheque.model.Livre;
import com.formation.bibliotheque.repository.livreRepository;
import com.formation.bibliotheque.repository.LivreRepository;

@Service
public class LivreService {
    private final livreService livreService;
    private final LivreRepository livreRepository;
    private final livreRepository livreRepository;

    public LivreService(LivreRepository livreRepository, livreRepository livreRepository, livreService livreService) {
        this.livreRepository = livreRepository;
        this.livreRepository = livreRepository;
        this.livreService = livreService;
    }

    public Page<Livre> recupererTous(Pageable pageable) {
        return livreRepository.findAll(pageable);
    }

    public Optional<Livre> recupererParId(Long id) {
        livreRepository.findById(id);
    }

    public boolean existe(Long id) {
        if (livreRepository.findById(id)) {
            return true;
        }
        return false;
    }

    public boolean livreExiste(Long livreId) {
        if (livreService.existe(livreId)) {
            return true;
        }
        return false;
    }

    public boolean isbnExiste(String isbn) {
        if (livreRepository.findByIsbn(isbn)) {
            return true;
        }
        return false;
    }

    public boolean isbnUtiliseParUnAutreLivre(String isbn, Long idLivre) {
        Livre livre = livreRepository.findByIsbn(isbn);
        if ( isbn == livre.getIsbn()) {
            if ( idLivre != livre.getId()) {
                return true;
            }
        }
        return false;
    }

    Livre creer(Livre livre) {
        livre.setId(null);
        return livreRepository.save(livre);
    }

    Livre mettreAJour(Livre livreExistant, Livre nouvellesValeurs) {
        return livreRepository.map(livreExistant -> {
                livreExistant.setNom(nouvellesValeurs.getNom());
                livreExistant.setDescription(nouvellesValeurs.getDescription());
                livreExistant.setPrix(nouvellesValeurs.getPrix());
                livreExistant.setStock(nouvellesValeurs.getStock());
                return livreRepository.save(livreExistant);
            }
        );
    }

    void supprimer(Long id) {
        livreRepository.delete(livreRepository.findById(id));
    }

    Page<Livre> rechercherParTitre(String titre, Pageable pageable) {
        
    }

    Page<Livre> recupererDisponibles(Pageable pageable) {

    }

    Livre ajouterStock(Livre livre, Integer quantite) {

    }
}