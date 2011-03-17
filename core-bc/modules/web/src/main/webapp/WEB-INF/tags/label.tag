<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="key" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<span class="kod-label ${key}-label" title="${prio.child != null ? su:mkChangedToolTip(prio.child[key], prio[key]) : ''}">
  ${prio.columns[key].label} 
  <c:if test="${prio.child != null and prio[key] != prio.child[key]}">
    <img src='img/flag_white.gif'/>
  </c:if>
</span> 
