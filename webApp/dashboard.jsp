<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Dashboard Offres</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<h1>Dashboard des Offres d'Emploi</h1>

<!-- Formulaire de filtre -->
<form method="get" action="dashboard">
    Ville: <input type="text" name="ville" value="${param.ville}">
    Secteur: <input type="text" name="secteur" value="${param.secteur}">
    Compétence: <input type="text" name="competence" value="${param.competence}">
    <button type="submit">Filtrer</button>
</form>

<!-- Liste des offres -->
<table>
    <tr>
        <th>Titre</th>
        <th>Entreprise</th>
        <th>Ville</th>
        <th>Secteur</th>
        <th>Expérience</th>
        <th>Compétences</th>
        <th>Lien</th>
    </tr>
    <c:forEach var="o" items="${offres}">
        <tr>
            <td>${o.title}</td>
            <td>${o.entreprise}</td>
            <td>${o.ville}</td>
            <td>${o.secteur}</td>
            <td>${o.experience}</td>
            <td>
                <c:forEach var="c" items="${o.competences}">
                    ${c}<c:if test="${!c.equals(o.competences[o.competences.size()-1])}">, </c:if>
                </c:forEach>
            </td>
            <td><a href="${o.urlSource}" target="_blank">Voir</a></td>
        </tr>
    </c:forEach>
</table>

<!-- Statistiques compétences -->
<h2>Top Compétences</h2>
<table>
    <tr><th>Compétence</th><th>Nombre d'offres</th></tr>
    <c:forEach var="entry" items="${stats}">
        <tr>
            <td>${entry.key}</td>
            <td>${entry.value}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
