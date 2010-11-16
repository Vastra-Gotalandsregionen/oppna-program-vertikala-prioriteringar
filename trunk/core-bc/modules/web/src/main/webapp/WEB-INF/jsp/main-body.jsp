<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>



<div class="main-body">
<div style="background-color: yellow">Vertikala prioriteringar</div>
<div class="sectors"><form:form commandName="form"
  action="check" method="POST">
  <tags:sectors items="${form.sectors}" />  
</form:form></div>

<div class="rowsAndButtons">
<div class="button-row">
<label for="select-prio"><button>Visa prioriteringsobjekt</button></label>
<form action="init-conf-columns"><input class="conf-columns" type="submit" value="Dölj/Visa kolumner" /></form>
<button class="cost">Kostnad</button>
<span class="export-data-buttons">
<button class="excel">Excel</button>
<button class="pdf">Pdf</button>
<button class="print">Skriv ut</button>
</span>
<button class="help">Hjälp</button>
</div>

<form action="select-prio" method="post">

<input type="submit" id="select-prio"/>

<table>
  <thead>
    <td>#</td>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td>${column.label}</td>
      </c:if>
    </c:forEach>
  </thead>
  <tbody>
    <c:forEach items="${rows}" var="row" varStatus="vs">
      <tr class="${vs.index % 2 == 0 ? 'even' : 'odd'}">
        <td><input type="radio" name="selected" value="row[id]"/></td>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <td>${row[column.name]}</td> 
          </c:if>
        </c:forEach>
      </tr>
    </c:forEach>
  </tbody>
</table>
</form>

</div>
<div class="footer">Footer</div>
</div>
