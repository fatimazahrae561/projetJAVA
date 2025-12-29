package com.jobs.model;

import java.util.Date;
import java.util.List;

public class Offre {

    private int id;
    private String entreprise;
    private String ville;      // Stocké sous forme de chaîne de caractères
    private String secteur;
    private String experience; // Ex: "1 à 3 ans", "Débutant", "Senior"
    private String urlSource;
    private String description;
    private String title;
    private List<String> competences;
    private Date datePublication;

    // ================= Getters et Setters =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCompetences() {
        return competences;
    }

    public void setCompetences(List<String> competences) {
        this.competences = competences;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    // ================= Optionnel : toString pour debug =================
    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", entreprise='" + entreprise + '\'' +
                ", ville='" + ville + '\'' +
                ", secteur='" + secteur + '\'' +
                ", experience='" + experience + '\'' +
                ", urlSource='" + urlSource + '\'' +
                ", title='" + title + '\'' +
                ", competences=" + competences +
                ", datePublication=" + datePublication +
                '}';
    }
}
