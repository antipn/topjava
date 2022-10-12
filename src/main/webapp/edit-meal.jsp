<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>
<div>
    <jsp:useBean id="mealForEditing" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
    <c:if test="${ not empty mealForEditing}">
        ${mealForEditing}
    </c:if>

    <form method="post" action="">
        <table border="1">
            <tr>
                <th width="200" align="left">DateTime:</th>
                <td><input type="datetime-local" name="mealDatetime" placeholder="${mealForEditing.dateTime}"
                           value="<c:out value="${mealForEditing.dateTime}"/>"/> <br/></td>
            </tr>
            <tr>
                <th align="left">Description:</th>
                <td><input type="text" name="mealDescription" placeholder="${mealForEditing.description}"
                           value="<c:out value="${mealForEditing.description}"/>"/> <br/></td>
            </tr>
            <tr>
                <th align="left">Calories:</th>
                <td><input type="text" name="mealCalories" placeholder="${mealForEditing.calories}"
                           value="<c:out value="${mealForEditing.calories}"/>"/> <br/></td>
            </tr>

        </table>
        <input  type="text" hidden name="mealtoDateTimeMarker" value="${mealForEditing.dateTime}">
        <br />
        <button type="submit">Submit</button>
        <br/>
    </form>

    <button onclick="location.href='meals?action=listMeals'">Cancel</button>

</div>
</body>

</html>
