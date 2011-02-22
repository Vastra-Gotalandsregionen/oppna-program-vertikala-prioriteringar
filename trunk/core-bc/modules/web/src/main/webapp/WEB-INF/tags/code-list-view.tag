<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="label" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="prio" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.entity.Prioriteringsobjekt"%>
<%@ attribute name="codeKey" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="styleClass" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="addItemLabel" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="removeItemLabel" required="true" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>

<div class="code-list-view ${styleClass}">
  <h4>${label}</h4>  
  <c:if test="${empty prio[codeKey]}">[Inga]</c:if>
  
  <c:forEach items="${prio[codeKey]}" var="item">
    <c:if test="${not empty item.beskrivning}">
      <div> 
        <c:choose>
          <c:when test="${util:canEdit(user, editDir)}">
            <input type="checkbox" name="removeCode" value="${codeKey}:${item.id}" />
          </c:when>
          <c:otherwise>*</c:otherwise>
        </c:choose>
        ${item.kod} ${item.beskrivning}
      </div>      
    </c:if>
  </c:forEach>

  <c:if test="${util:canEdit(user, editDir)}">
    <input type="submit" id="${codeKey}" value="${addItemLabel}" name="${codeKey}"/>
    <c:if test="${not empty prio[codeKey]}">
      <input type="submit" value="${removeItemLabel}" name="removeCodes"/>
    </c:if>
  </c:if>

  
</div>