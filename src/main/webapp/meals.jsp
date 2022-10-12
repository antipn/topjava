<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<li><a href="meals?action=insert">Add meal</a></li>
<style>
     tr,td, th {
        border: 1px solid black;
    }
</style>
<table style="width:30%">
    <caption><h2>List of meals</h2></caption>
    <tr>
        <th class="not_mapped_style" style="text-align:left">DateTime</th>
        <th class="not_mapped_style" style="text-align:left">Description</th>
        <th class="not_mapped_style" style="text-align:left">Calories</th>
        <th class="not_mapped_style" style="text-align:left">Excess</th>
        <th class="not_mapped_style" style="text-align:left">Update meal</th>
        <th class="not_mapped_style" style="text-align:left">Delete meal</th>
    </tr>
    <jsp:useBean id="listMeals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${listMeals}">
        <c:if test="${meal.excess==true}">
            <tr class="not_mapped_style" style="color: red">
        </c:if>
        <c:if test="${meal.excess==false}">
            <tr class="not_mapped_style" style="color: green">
        </c:if>
        <td><c:out value=""/>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
        <td><c:out value="${meal.description}"/></td>
        <td><c:out value="${meal.calories}"/></td>
        <td><c:out value="${meal.excess}"/></td>
        <td><a href="meals?action=edit&mealTimeStamp=<c:out value="${meal.dateTime}"/>">Update</a></td>
        <td><a href="meals?action=delete&mealTimeStamp=<c:out value="${meal.dateTime}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>