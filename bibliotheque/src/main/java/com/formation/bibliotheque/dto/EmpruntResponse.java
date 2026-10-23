package com.formation.bibliotheque.dto;

import java.time.LocalDate;

public class EmpruntResponse {

    private Long id;
    private Long livreId;
    private String emprunteur;
    private String emailEmprunteur;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private Boolean rendu;

    public EmpruntResponse(Long id, Long livreId, String emprunteur, String emailEmprunteur, LocalDate dateEmprunt, LocalDate dateRetourPrevue, LocalDate dateRetourEffective, Boolean rendu) {
        this.id = id;
        this.livreId = livreId;
        this.emprunteur = emprunteur;
        this.emailEmprunteur = emailEmprunteur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.rendu = rendu;
    }

    public Long getId() { return id; }
    public Long getLivreId() { return livreId; }
    public String getEmprunteur() { return emprunteur; }
    public String getEmailEmprunteur() { return emailEmprunteur; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public Boolean getRendu() { return rendu; }
}
