<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Dashboard des Offres</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 30px;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-secondary mb-4">
    <div class="container-fluid">
        <span class="navbar-brand fw-bold">Jobs Portal</span>
        <div class="navbar-nav">
            <a class="nav-link" href="accueil">Accueil</a>
            <a class="nav-link active" href="dashboard">Dashboard</a>
        </div>
    </div>
</nav>



<div class="container">
    <h1 class="mb-4">Dashboard des Offres</h1>
    
    <!-- Total offres -->
    <div class="card mb-4">
        <div class="card-body">
            <h5>Total d'offres</h5>
            <p class="display-6 text-primary"><%= request.getAttribute("totalOffres") %></p>
        </div>
    </div>

    <!-- Top compétences -->
    <div class="card mb-4">
        <div class="card-body">
            <h5>Top compétences</h5>
            <ul class="list-group list-group-flush">
                <%
                    List<String> topComp = (List<String>) request.getAttribute("topCompetences");
                    for (String c : topComp) {
                %>
                <li class="list-group-item"><%= c %></li>
                <% } %>
            </ul>
        </div>
    </div>

    <!-- Statistiques compétences -->
    <div class="card mb-4">
        <div class="card-body">
            <h5>Effectif par compétence</h5>
            <canvas id="competenceChart" height="150"></canvas>
        </div>
    </div>
</div>

<script>
    // Récupérer les données depuis JSP
    const stats = {
        <% 
            Map<String, Integer> statsMap = (Map<String, Integer>) request.getAttribute("competenceStats");
            boolean first = true;
            for (Map.Entry<String, Integer> entry : statsMap.entrySet()) {
                if (!first) out.print(","); else first = false;
                out.print("\"" + entry.getKey() + "\":" + entry.getValue());
            }
        %>
    };

    const labels = Object.keys(stats);
    const data = Object.values(stats);

    const ctx = document.getElementById('competenceChart').getContext('2d');
    const competenceChart = new Chart(ctx, {
        type: 'bar', // histogramme
        data: {
            labels: labels,
            datasets: [{
                label: 'Nombre d’offres',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Nombre d’offres'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Compétences'
                    }
                }
            }
        }
    });
</script>



</body>
</html>
