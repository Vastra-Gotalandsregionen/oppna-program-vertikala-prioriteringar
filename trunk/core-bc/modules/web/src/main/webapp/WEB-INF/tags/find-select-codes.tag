<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="codeRefName" required="true" rtexprvalue="true" type="java.lang.String"%>

<%@ attribute name="label" required="true" rtexprvalue="true" type="java.lang.String"%>

<%@ attribute name="submitName" required="true" rtexprvalue="true" type="java.lang.String"%>

<%@ attribute name="codeRef" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.controllers.ManyCodesRef"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>

<div class="find-select-code">

  <div class="label">
    ${label} <form:input path="${codeRefName}.searchWord"/>
  </div>
  
  <div class="findings">
  <c:forEach items="${codeRef.findings}" var="item">
    <label for="">
      <form:checkbox path="${codeRefName}.selectedCodesId" value="${item.id}" disabled="${util:contains(codeRef.selectedCodesId, item.id)}" />
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  </div>
  
  <div>
  <input type="submit" value="Välj!" name="${submitName}" />
  </div>
  
  <div class="selected">
  <c:forEach items="${codeRef.codes}" var="item">
    <label for="">
      <input type="checkbox" name="${codeRefName}.selectedCodesId" value="${item.id}" checked="checked"/>
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  </div>
</div>