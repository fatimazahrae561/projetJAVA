<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.jobs.model.Offre" %>

<!DOCTYPE html>
<html>
<head>
    <title>Accueil Jobs</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

<!-- ===== Topbar ===== -->
<nav class="navbar navbar-expand-lg navbar-dark bg-secondary mb-4">
    <div class="container-fluid">
        <span class="navbar-brand fw-bold">Jobs Portal</span>
        <div class="navbar-nav">
            <a class="nav-link" href="accueil">Accueil</a>
            <a class="nav-link" href="dashboard">Dashboard</a>
        </div>
    </div>
</nav>

<div class="container">

    <!-- ===== Titre ===== -->
    

    <!-- ===== Formulaire ===== -->
    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <h5 class="card-title mb-3">Filtrer les offres</h5>

            <form action="accueil" method="get" class="row g-3">

                <div class="col-md-4">
                    <label class="form-label">Secteur</label>
                    <input type="text" class="form-control" name="secteur"
                           value="<%= request.getParameter("secteur") != null ? request.getParameter("secteur") : "" %>">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Compétence</label>
                    <input type="text" class="form-control" name="competence"
                           value="<%= request.getParameter("competence") != null ? request.getParameter("competence") : "" %>">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Ville</label>
                    <input type="text" class="form-control" name="ville"
                           value="<%= request.getParameter("ville") != null ? request.getParameter("ville") : "" %>">
                </div>

                <div class="col-12 text-end">
                    <button type="submit" class="btn btn-primary">Filtrer</button>
                    <a href="accueil" class="btn btn-outline-secondary">Réinitialiser</a>
                </div>
            </form>
        </div>
    </div>

    <!-- ===== Résultat ===== -->
    <%
        List<Offre> jobs = (List<Offre>) request.getAttribute("offres");
        int size = request.getAttribute("size") != null
                   ? (int) request.getAttribute("size")
                   : 0;
    %>

    <p class="fw-bold text-muted mb-2">
        Nombre d’offres trouvées : <span class="text-primary"><%= size %></span>
    </p>

    <!-- ===== Tableau ===== -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                <tr>
                    <th>Titre</th>
                    <th>Description</th>
                    <th>Date</th>
                </tr>
                </thead>

                <tbody>
                <%
                    if (jobs != null && !jobs.isEmpty()) {
                        for (Offre job : jobs) {
                %>
                <tr>
                    <td>
                        <a href="<%= job.getUrlSource() %>" target="_blank"
						   class="text-decoration-none fw-semibold text-dark">
						    <%= job.getTitle() %>
						</a>
                    </td>
                    <td><%= job.getDescription() %></td>
                    <td style="width:120px;"><%= job.getDatePublication() %></td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="3" class="text-center text-muted py-4">
                        Aucune offre trouvée
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>

</div>

</body>
</html>
