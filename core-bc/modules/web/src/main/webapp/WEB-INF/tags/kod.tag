<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="key" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="label" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<span class="${key} kod"> 
  <span class="kod-label ${key}-label"> ${prio.columns[label].label} </span> 
  <c:choose>
    <c:when test="${su:canEdit(user, editDir)}">
      <form:select path="${key}Id">
        <option value="">-- Ingen --</option>
        <form:options items="${prio[su:concat(key, 'List')]}" itemLabel="label" itemValue="id" />
      </form:select>
    </c:when>
    <c:otherwise>
      ${su:labelFor(prio[su:concat(key, 'Id')], prio[su:concat(key, 'List')])} <br/>
    </c:otherwise>
  </c:choose> 
</span>