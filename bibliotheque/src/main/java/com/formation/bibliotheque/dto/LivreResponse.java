package com.formation.bibliotheque.dto;

public class LivreResponse {

    private Long id;
    private String titre;
    private String isbn;
    private Double prix;
    private int anneePublication;
    private Integer stock;
    private Long auteurId;

    public LivreResponse(Long id, String titre, String isbn, int anneePublication, Double prix, Integer stock, Long auteurId) {
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
