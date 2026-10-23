package com.formation.bibliotheque.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmpruntRequest {

    @NotBlank(message = "L'id de l'emprunt est obligatoire")
    @NotNull(message = "L'id de l'emprunt ne peut pas être null")
    private Long id;

    @NotBlank(message = "L'id du livre est obligatoire")
    @NotNull(message = "L'id du livre ne peut pas être null")
    private Long livreId;

    @NotBlank(message = "Le nom de l'emprunteur est obligatoire")
    @NotNull(message = "Le nom de l'emprunteur ne peut pas être null")
    private String emprunteur;

    @NotBlank(message = "L'email de l'emprunteur est obligatoire")
    @NotNull(message = "L'email de l'emprunteur ne peut pas être null")
    private String emailEmprunteur;

    @NotBlank(message = "La date d'emprunt est obligatoire")
    @NotNull(message = "La date d'emprunt ne peut pas être null")
    private LocalDate dateEmprunt;

    @NotBlank(message = "La date de retour prévue est obligatoire")
    @NotNull(message = "La date de retour prévue ne peut pas être null")
    private LocalDate dateRetourPrevue;

    private LocalDate dateRetourEffective;

    @NotBlank(message = "Le statut de rendu est obligatoire")
    @NotNull(message = "Le statut de rendu ne peut pas être null")
    private Boolean rendu;

    @NotBlank(message = "Le statut de retard est obligatoire")
    @NotNull(message = "Le statut de retard ne peut pas être null")
    private Boolean enRetard;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public String getEmprunteur() { return emprunteur; }
    public void setEmprunteur(String emprunteur) { this.emprunteur = emprunteur; }

    public String getEmailEmprunteur() { return emailEmprunteur; }
    public void setEmailEmprunteur(String emailEmprunteur) { this.emailEmprunteur = emailEmprunteur; }

    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }

    public Boolean getRendu() { return rendu; }
    public void setRendu(Boolean rendu) { this.rendu = rendu; }

    public Boolean getEnRetard() { return enRetard; }
    public void setEnRetard(Boolean enRetard) { this.enRetard = enRetard; }
}
