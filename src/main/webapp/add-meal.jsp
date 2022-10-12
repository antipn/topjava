<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>
    <title>Add Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add Meal</h2>
<div>
    <jsp:useBean id="processingStatus" scope="request" type="java.lang.String"/>
    <c:if test="${ not empty processingStatus}">
        ${processingStatus}
    </c:if>
    <form method="post">
        <table border="1">
            <tr>
                <th width="200" align="left">DateTime:</th>
                <td><input type="datetime-local" name="mealDatetime"
                           value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${meal.Datetime}"/>"/> <br/></td>
            </tr>
            <tr>
                <th align="left">Description:</th>
                <td><input type="text" name="mealDescription"
                           value="<c:out value="${meal.description}"/>"/> <br/></td>
            </tr>
            <tr>
                <th align="left">Calories:</th>
                <td><input type="text" name="mealCalories"
                           value="<c:out value="${meal.calories}"/>"/> <br/></td>
            </tr>
        </table>
        <button type="submit">Submit</button>
        <br/>
    </form>
    <button onclick="location.href='meals?action=listMeals'">Cancel</button>

</div>
</body>

</html>