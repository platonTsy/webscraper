
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
</head>
<body>
    <table>
        <thead>
            <tr>
                <td>Company name</td>
                <td>Category name</td>
                <td>Category uri</td>
                <td>Search title</td>
                <td>Search uri</td>
            </tr>
        </thead>
        <c:forEach items="${companies}" var="company">
            <c:forEach items="company.links" var="searchLink">
                <tr>
                    <td>${company.name}</td>
                    <td>${company.category.name}</td>
                    <td>${company.category.uri}</td>
                    <td>${searchLink.title}</td>
                    <td>${searchLink.uri}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</body>
</html>