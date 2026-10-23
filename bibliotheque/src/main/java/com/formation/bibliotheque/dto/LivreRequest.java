package com.formation.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LivreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @NotNull(message = "Le titre ne peut pas être null")
    private String titre;

    @NotBlank(message = "L'isbn est obligatoire")
    @NotNull(message = "L'isbn ne peut pas être null")
    private String isbn;

    @NotBlank(message = "Le prix est obligatoire")
    @NotNull(message = "Le prix ne peut pas être null")
    private Double prix;

    private int anneePublication;

    @NotBlank(message = "Le stock est obligatoire")
    @NotNull(message = "Le stock ne peut pas être null")
    private Integer stock;

    @NotBlank(message = "L'id de l'auteur est obligatoire")
    @NotNull(message = "L'id de l'auteur ne peut pas être null")
    @Positive(message = "L'id de l'auteur doit être positif")
    private Long auteurId;

    // Getters et setters

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getAuteurId() { return auteurId; }
    public void setAuteurId(Long auteurId) { this.auteurId = auteurId; }
}
