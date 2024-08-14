<%@page contentType = "text/html;charset = UTF-8" language = "java" %>
<%@page isELIgnored = "false" %>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
   <head>
      <title>Spring MVC Form Handling</title>
   </head>

   <body>
      <h2>Submitted Product Information</h2>
      <table>
      <c:forEach items="${productList}" var="product">
               <tr>
                  <td>Name </td>
                  <td>${product.name}</td>
               </tr>
                <tr>
                                 <td>Id </td>
                                 <td>${product.id}</td>
                              </tr>
          </c:forEach>
      </table>
   </body>

</html>