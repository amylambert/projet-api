package com.formation.bibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class LivreResponse {

    private final Long id;
    private final String titre;
    private final String isbn;
    private final Integer anneePublication;
    private final Double prix;
    private final Integer stock;
    private final Long auteurId;

    @JsonPropertyOrder({"id", "titre", "isbn", "prix", "anneePublication", "stock", "auteurId"})
    public LivreResponse(Long id, String titre, String isbn, Double prix, Integer anneePublication, Integer stock, Long auteurId) {
        this.id = id;
        this.titre = titre;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.prix = prix;
        this.stock = stock;
        this.auteurId = auteurId;
    }


    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getIsbn() { return isbn; }
    public int getAnneePublication() { return anneePublication; }
    public Double getPrix() { return prix; }
    public Integer getStock() { return stock; }
    public Long getAuteurId() { return auteurId; }
}
