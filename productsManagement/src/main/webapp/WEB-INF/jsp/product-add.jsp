<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add Product</title>
    </head>
    <body>
        <c:if test="${addProductSuccess}">
            <div>Successfully added Product with name: ${savedProduct.name}</div>
        </c:if>

        <c:url var="add_product_url" value="/product/new"/>
        <form:form action="${add_product_url}" method="post" modelAttribute="product">
            <form:label path="id">ID: </form:label>
            <form:input type="text" path="id"/>

            <form:label path="name">Product Name: </form:label>
            <form:input type="text" path="name"/>

            <form:label path="description">Description: </form:label>
            <form:input type="text" path="description"/>

            <form:label path="price">Price: </form:label>
            <form:input type="text" path="price"/>

            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>