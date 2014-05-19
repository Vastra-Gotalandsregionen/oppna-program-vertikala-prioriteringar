<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="column" required="true" rtexprvalue="true"
	type="se.vgregion.verticalprio.entity.Column"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<c:if test="${column.sortable}">
	<span> 
		<c:choose>
			<c:when test="${column.sorting}">
				<img src="${pageContext.request.contextPath}/img/arrow-down-active.png"
					title="Sortering är gjord med denna kolumn" />
			</c:when>
			<c:otherwise>
                <portlet:renderURL var="sortUrl">
                    <portlet:param name="sortField" value="${column.name}"/>
                </portlet:renderURL>
				<a title="Sortera" href="${sortUrl}"
					style="text-decoration: none;"><img src="${pageContext.request.contextPath}/img/arrow-down.png" /></a>
			</c:otherwise>
		</c:choose> 
	</span>
</c:if>