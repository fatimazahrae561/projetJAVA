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

public class AccueilServlet extends HttpServlet {

    private DashboardController dashboard;
    private static final int PAGE_SIZE = 20;

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

        int page = 1;
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

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

        int totalOffres = offres.size();
        int totalPages = (int) Math.ceil((double) totalOffres / PAGE_SIZE);

        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalOffres);

        if (start > totalOffres) {
            start = 0;
            end = Math.min(PAGE_SIZE, totalOffres);
            page = 1;
        }

        List<Offre> offresPage = offres.subList(start, end);
        ville = (ville != null && !ville.trim().isEmpty()) ? ville.trim() : null;
        secteur = (secteur != null && !secteur.trim().isEmpty()) ? secteur.trim() : null;
        competence = (competence != null && !competence.trim().isEmpty()) ? competence.trim() : null;
        req.setAttribute("offres", offresPage);
        req.setAttribute("size", totalOffres);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("ville", ville);
        req.setAttribute("secteur", secteur);
        req.setAttribute("competence", competence);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}


