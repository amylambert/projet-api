package com.formation.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuteurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @NotNull(message = "Le nom ne peut pas être null")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @NotNull(message = "Le prénom ne peut pas être null")
    private String prenom;

    private String nationalite;

    // Getters et setters

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
}