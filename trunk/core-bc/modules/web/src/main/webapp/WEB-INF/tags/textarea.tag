<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="key" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="label" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="cssInputBoxStyle" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<span class="${key} kod> 
  <span class="kod-label ${key}-label"> ${label} </span> 
  <c:if test="${prio.child != null}">
    <span id="${key}ApprovedValue" style="display:none">${(prio.child[key] != null) ? prio.child[key] : ''}</span>
  </c:if>
  <span id="${key}OldValue" style="display:none">${(prio[key] != null) ? prio[key] : ''}</span>

  <c:choose >
	  <c:when test="${su:canEdit(user, editDir)}">
	    <form:textarea cssStyle="${cssInputBoxStyle}" cssClass="standardInput white" path="${key}" />
	  </c:when>
	  <c:otherwise>
	  	 ${su:convert2br(prio[key])}
	  </c:otherwise>
  </c:choose>
  
   
  
</span>