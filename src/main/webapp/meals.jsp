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
    <table>
        <caption><h2>List of meals</h2></caption>
        <tr>
            <th class="not_mapped_style" style="text-align:left">DateTime</th>
            <th class="not_mapped_style" style="text-align:left">Description</th>
            <th class="not_mapped_style" style="text-align:left">Calories</th>
            <th class="not_mapped_style" style="text-align:left">Excess</th>
        </tr>
        <jsp:useBean id="listMeals" scope="request" type="java.util.List"/>
        <c:forEach var="meal" items="${listMeals}">
            <c:if test="${meal.excess==true}">
                <tr class="not_mapped_style" style="background-color: red">
                    <td><c:out value=""/>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                    <td><c:out value="${meal.description}"/></td>
                    <td><c:out value="${meal.calories}"/></td>
                    <td><c:out value="${meal.excess}"/></td>
                </tr>
            </c:if>
            <c:if test="${meal.excess==false}">
                <tr class="not_mapped_style" style="background-color: green">
                    <td><c:out value=""/>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                    <td><c:out value="${meal.description}"/></td>
                    <td><c:out value="${meal.calories}"/></td>
                    <td><c:out value="${meal.excess}"/></td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
</body>
</html>