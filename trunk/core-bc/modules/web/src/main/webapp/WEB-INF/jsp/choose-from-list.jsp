<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="head.jsp" %>
<body style="height: 100%;">

<span class="window prio-view choose-from-list">

<form:form cssStyle="margin:auto" action="choose-from-list" method="post" cssClass="choose-code" modelAttribute="ChooseListForm">

  <div class="yui3-g">
  
  <c:if test="${ChooseListForm.findingVisible}">
    <span class="yui3-u-1-3">
      <h3>${ChooseListForm.filterLabel}</h3>
      <form:input path="filterText" id="filterText" />
      <input class="button" type="submit" name="filter" value="Filtrera"/>
    </span>
    <span class="yui3-u-1-3"></span>
    <span class="yui3-u-1-3"></span>
  </c:if>
  
  <span class="yui3-u-1-3"><h3>${ChooseListForm.notYetChoosenLabel}</h3> (${ChooseListForm.sizeOfAllToChoose-ChooseListForm.sizeOfChoosen} stycken)</span>
  <span class="yui3-u-1-3"></span>
  <span class="yui3-u-1-3"><h3>${ChooseListForm.choosenLabel}</h3> (${ChooseListForm.sizeOfChoosen} stycken)</span>
    
  <span class="yui3-u-1-3">
  <select name="notYetChoosenKeys" multiple="multiple">
    <c:forEach items="${ChooseListForm.allToChoose}" var="column">
      <c:if test="${not su:contains(ChooseListForm.choosen, column)}">
        <option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>
      </c:if>
    </c:forEach>
  </select>
  </span>
    
  <span class="yui3-u" style="height:100%" >
  
    <div class="padding_small"><input  class="button" type="submit" name="addAll" value="Lägg till alla &rArr;" /></div>
    <div class="padding_small"><input class="button" type="submit" name="add" value="Lägg till &rArr;" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="remove" value="&lArr; Ta bort" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="removeAll" value="&lArr; Ta bort alla" /></div>
  </span>
    
  <span class="yui3-u-1-3">
    <select name="choosenKeys" multiple="multiple">
      <c:forEach items="${ChooseListForm.choosen}" var="column">
        <option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>
      </c:forEach>
    </select>
  </span>
  
  <span class="yui3-u-1-3"></span>
  <span class="yui3-u-1-3"></span>
  <span class="yui3-u-1-3">
    <input class="button" type="submit" name="cancel" value="Avbryt" />
    <input class="button" type="submit" name="ok" value="${ChooseListForm.okLabel}" />
  </span>
  
  </div>

<tags:message-out/>

</form:form>

</span>


</body>
</html>