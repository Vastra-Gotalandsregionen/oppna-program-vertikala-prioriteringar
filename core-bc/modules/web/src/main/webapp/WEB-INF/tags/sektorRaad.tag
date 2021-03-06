<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="key" required="true" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<span class="${key} kod">  
  <c:if test="${prio.child != null}">
    <span id="${key}ApprovedValue" style="display:none">${(prio.child[key] != null) ? prio.child[key].id : ''}</span>
  </c:if>
  <span id="${key}OldValue" style="display:none">${(prio[key] != null) ? prio[key].id : ''}</span>      
  <c:choose>
    <c:when test="${su:canEdit(user, editDir)}">
      <form:select path="${key}Id" cssClass="standardInput">
        <option value="">-- Ingen --</option>
        ${su:toRaadOptions(prio[su:concat(key, 'Id')], prio[su:concat(key, 'List')], user.sektorRaad)}
      </form:select>
    </c:when>
    <c:otherwise>
      ${su:labelFor(prio[su:concat(key, 'Id')], prio[su:concat(key, 'List')])} <br/>
    </c:otherwise>
  </c:choose> 
</span>