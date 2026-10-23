package com.formation.bibliotheque.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmpruntRequest {

    @NotBlank(message = "L'id du livre est obligatoire")
    @NotNull(message = "L'id du livre ne peut pas être null")
    private Long livreId;

    @NotBlank(message = "Le nom de l'emprunteur est obligatoire")
    @NotNull(message = "Le nom de l'emprunteur ne peut pas être null")
    private String emprunteur;

    @NotBlank(message = "L'email de l'emprunteur est obligatoire")
    @NotNull(message = "L'email de l'emprunteur ne peut pas être null")
    private String emailEmprunteur;

    // Getters et setters

    public Long getLivreId() { return livreId; }
    public void setLivreId(Long livreId) { this.livreId = livreId; }

    public String getEmprunteur() { return emprunteur; }
    public void setEmprunteur(String emprunteur) { this.emprunteur = emprunteur; }

    public String getEmailEmprunteur() { return emailEmprunteur; }
    public void setEmailEmprunteur(String emailEmprunteur) { this.emailEmprunteur = emailEmprunteur; }

}
