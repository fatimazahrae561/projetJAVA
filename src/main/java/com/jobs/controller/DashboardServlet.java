package com.jobs.controller;

import com.jobs.model.Offre;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DashboardServlet extends HttpServlet {

    private DashboardController dashboard;

    @Override
    public void init() {
        dashboard = new DashboardController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1️⃣ Recuperer toutes les offres
        List<Offre> offres = dashboard.getOffers();
        int totalOffres = offres.size();

        // 2️⃣ Statistiques competences
        Map<String, Integer> competenceStats = dashboard.getCompetenceStats();

        // 3️⃣ Top competences
        List<String> topCompetences = dashboard.getTopCompetences(10);

        
        // 5️⃣ Mettre tout dans request
        req.setAttribute("totalOffres", totalOffres);
        req.setAttribute("competenceStats", competenceStats);
        req.setAttribute("topCompetences", topCompetences);

        // 6️⃣ Forward vers dashboard.jsp
        req.getRequestDispatcher("dashboard.jsp").forward(req, resp);
    }
}
