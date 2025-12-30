package com.jobs.controller;

import com.jobs.controller.DashboardController;
import com.jobs.model.Offre;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private DashboardController dashboard;

    @Override
    public void init() {
        dashboard = new DashboardController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ville = req.getParameter("ville");
        String secteur = req.getParameter("secteur");
        String competence = req.getParameter("competence");

        List<Offre> offres;

        if(ville != null || secteur != null || competence != null){
            offres = dashboard.filterCombined(ville, secteur, competence);
        } else {
            offres = dashboard.getOffers();
        }

        req.setAttribute("offres", offres);

        Map<String,Integer> stats = dashboard.getCompetenceStats();
        req.setAttribute("stats", stats);

        req.getRequestDispatcher("dashboard.jsp").forward(req, resp);
    }
}

