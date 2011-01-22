<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="codeRefName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="label" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="submitName" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="codeRef" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.controllers.ManyCodesRef"%>
<%@ attribute name="styleClass" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>

<div class="find-select-code ${styleClass} yui3-g">
  <c:choose>
    <c:when test="${util:canEdit(user, editDir)}">
    
  <div class="selected yui3-u-1-2">
  <c:forEach items="${codeRef.codes}" var="item">
    <label for="">
      <input type="checkbox" name="${codeRefName}.selectedCodesId" value="${item.id}" checked="checked" class="standardInputCheckBox"/>
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  </div>

  <div class="selected yui3-u-1-2">
     <span style="white-space: normal; width: 45%"> ${label} </span>
     <span style="white-space: nowrap; width: 45%">
       <form:input cssStyle="width:5em" path="${codeRefName}.searchKodText" cssClass="standardInput"/>
       <form:input path="${codeRefName}.searchBeskrivningText" cssClass="standardInput"/> 
       <input type="submit" value="Sök/Välj" name="${submitName}" class="button" />
     </span>
  </div>
  
  <div class="findings yui3-u-1">
  <c:forEach items="${codeRef.findings}" var="item" varStatus="vs">
    <label for="">
      <form:checkbox cssClass="standardInputCheckBox" path="${codeRefName}.selectedCodesId" value="${item.id}" disabled="${util:contains(codeRef.selectedCodesId, item.id)}" />
      ${item.kod} ${item.beskrivning}
    </label>
    <br/>
  </c:forEach>
  </div>
    </c:when>
    <c:otherwise>
    <ul class="yui3-u-1">
  <c:forEach items="${codeRef.codes}" var="item">
  <li>
    <label for="">
      <div>${item.label}</div> 
    </label>
  </li>
  </c:forEach>
    </ul>
    </c:otherwise>
  </c:choose>
   ${(empty codeRef.codes) ? '-':' '}
</div>