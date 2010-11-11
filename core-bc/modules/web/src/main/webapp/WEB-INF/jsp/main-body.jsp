<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div style="width:100%">
  <div style="background-color:yellow">
    Header ${types}
  </div>
  <div style="background-color:orange;width:100px;float:left;">
    <form:form commandName="form" action="check" method="POST">
      <c:forEach items="${form.sectors}" var="sector">
        <input style="display:none" id="Sektor${sector.id}" type="submit" name="id" value="${sector.id}" />
        <label for="Sektor${sector.id}"> ${sector.label} ${sector.selected}</label> <br/>
      </c:forEach>
      <input type="submit" value="select sector"/>
    </form:form>
  </div>
  
  <div style="background-color:#eeeeee;float:left;">
    <div id="buttons-row">
      <button>Visa prioriteringsobjekt</button>
      <form action="init-conf-columns">
        <input type="submit" value="Dölj/Visa kolumner" />
      </form>
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
          <c:forEach items="${form.columns}" var="column">
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
