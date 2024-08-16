<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add Product</title>
    <c:url value="/css/styles.css" var="stylesCss" />
    <link rel="stylesheet" href="${stylesCss}" />
</head>
<body>
    <div class="container-add-product-form">
        <h1>Add Product</h1>
        <c:if test="${addProductSuccess}">
            <div class="success-message">Sent product to be processed.</div>
        </c:if>

        <!-- Display general error message -->
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:url var="add_product_url" value="/product/new"/>
        <form:form action="${add_product_url}" method="post" modelAttribute="product">
            <div class="form-group">
                <form:label path="id">ID: </form:label>
                <form:input type="text" path="id" required="true"/>
            </div>

            <div class="form-group">
                <form:label path="name">Product Name: </form:label>
                <form:input type="text" path="name" required="true"/>
            </div>

            <div class="form-group">
                <form:label path="description">Description: </form:label>
                <form:input type="text" path="description"/>
            </div>

            <div class="form-group">
                <form:label path="price">Price: </form:label>
                <form:input type="text" path="price" required="true"/>
            </div>

            <div class="form-group">
                <input type="submit" value="Submit"/>
            </div>
        </form:form>

        <div class="form-group">
            <a href="../index.html" class="button-gray">Return to Home</a>
        </div>

    </div>
</body>
</html>
