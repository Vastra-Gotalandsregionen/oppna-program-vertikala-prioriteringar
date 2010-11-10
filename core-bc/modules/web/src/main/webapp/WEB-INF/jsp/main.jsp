<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="height:100%; ">

<div style="width:100%">
  <div style="background-color:yellow">
    Header
  </div>
  <div style="background-color:orange;width:100px;float:left;">
    <form action="main" method="POST">
      <c:forEach items="${sectors}" var="sector">
          <input type="checkbox" id="${sector.id}" value="${sector.id}" name="sectors"/> 
          <label for="${sector.id}"> ${sector.label}</label> <br/>
      </c:forEach>
      <input type="submit" value="select sector"/>
    </form>
  </div>
  
  <div style="background-color:#eeeeee;float:left;">
    <div id="buttons-row">
      <button>Visa prioriteringsobjekt</button>
      <button>Dölj/Visa kolumner</button>
      <button>Kostnad</button>
      <span id="export-data-buttons">
        <button>Excel</button>
        <button>Pdf</button>    
        <button>Skriv ut</button>        
      </span>
      <button>Hjälp</button>
    </div>
    <table>
      <thead>
        <th>
          <c:forEach items="${columns}" var="column">
            <td>${column.label}</td>
          </c:forEach>
        </th>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
  <div style="background-color:yellow;clear:both">
    Footer
  </div>
</div>


</body>
</html>