<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="name" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<c:if test="${su:canEdit(user, editDir)}">
  <input type="submit" value="${value}" name="${name}" />
</c:if>
