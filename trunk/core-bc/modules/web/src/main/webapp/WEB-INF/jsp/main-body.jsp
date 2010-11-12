<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div style="width: 100%">
<div style="background-color: yellow">Header ${types}</div>
<div style="background-color: orange; width: 100px; float: left;"><form:form commandName="form"
  action="check" method="POST">
  <c:forEach items="${form.sectors}" var="sector">
    <input style="display: none" id="Sektor${sector.id}" type="submit" name="id" value="${sector.id}" />
    <label for="Sektor${sector.id}"> ${sector.label}
      <c:choose>
        <c:when test="${sector.selected}"> <input type="checkbox" checked="checked"/> </c:when>
        <c:otherwise> <input type="checkbox"/> </c:otherwise>
      </c:choose> 
    </label>
    <br />
  </c:forEach>
</form:form></div>

<div style="background-color: #eeeeee; float: left;">
<div id="buttons-row" class="button-row">
<button>Visa prioriteringsobjekt</button>
<form action="init-conf-columns"><input type="submit" value="Dölj/Visa kolumner" /></form>
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
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td>${column.label}</td>
      </c:if>
    </c:forEach>
  </thead>
  <tbody>
    <c:forEach items="${rows}" var="row">
      <tr>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <td>${row[column.name]}</td>
          </c:if>
        </c:forEach>
      </tr>
    </c:forEach>
  </tbody>
</table>
</div>
<div style="background-color: yellow; clear: both">Footer</div>
</div>
hejKnekt