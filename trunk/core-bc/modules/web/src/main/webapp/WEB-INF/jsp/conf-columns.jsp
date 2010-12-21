<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:include page="jsp/head.jsp"/>

<body style="height: 100%;">

<%-- jsp:include page="jsp/main-body.jsp" / --%>


<jsp:useBean id="confCols" scope="session" class="se.vgregion.verticalprio.ConfColumnsForm"></jsp:useBean>

<div class="popup-overlay">
</div>

<span class="window">
<form action="conf-columns"><select name="hiddenColumns" multiple="multiple">
  <c:forEach items="${confCols.hiddenColumns}" var="column">
    <option value="${column.name}" id="${column.id}">${column.label}</option>
  </c:forEach>
</select> 
  <input type="submit" name="command" value="show" id="show-selected" style="display: none;"/>
  <label for="show-selected">-></label> 
  
  <input type="submit" name="command"  style="display: none;"
  value="hide" id="hide-selected" /> 
  <label for="hide-selected"><-</label>
  
  <select name="visibleColumns" multiple="multiple">
  <c:forEach items="${confCols.visibleColumns}" var="column">
    <option value="${column.name}" id="${column.id}">${column.label}</option>
  </c:forEach>
  </select>


  <input type="submit" name="command"  style="display: none;"
  value="save" id="save-change" /> 
  <label for="save-change">Ok</label>
  
    <input type="submit" name="command"  style="display: none;"
  value="cancel" id="cancel-change" /> 
  <label for="save-change">Cancel</label>

</form>

</span>



</body>
</html>