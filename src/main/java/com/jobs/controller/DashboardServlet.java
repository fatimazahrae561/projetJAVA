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
        int size;

        boolean hasFilter =
                (ville != null && !ville.trim().isEmpty()) ||
                (secteur != null && !secteur.trim().isEmpty()) ||
                (competence != null && !competence.trim().isEmpty());

        List<Offre> offres;

        if (hasFilter) {
            offres = dashboard.filterCombined(ville, secteur, competence);
        } else {
            offres = dashboard.getOffers();
        }
        size=offres.size();
        req.setAttribute("offres", offres);
        req.setAttribute("size", size);

        req.getRequestDispatcher("index.jsp").forward(req, resp);
        
    }
    
    
}

