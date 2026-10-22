package com.formation.bibliotheque.dto;

public class AuteurResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String nationalite;

    public AuteurResponse(Long id, String nom, String prenom, String nationalite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNationalite() {
        return nationalite;
    }
}