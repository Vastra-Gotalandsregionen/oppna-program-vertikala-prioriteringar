<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="row" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.entity.Prioriteringsobjekt"%>
<%@ attribute name="index" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="trCssClass" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<tr class="${index % 2 == 0 ? 'even' : 'odd'}">
  <td>
    <input title="id=${row.id}" type="radio" name="id" value="${row.id}"${vs.index == 0 ? ' checked' : ''}/>
    <c:if test="${row.godkaend == null && (row.child == null || row.child.godkaend == null)}">
      <div style="color: red; text-align: center;" title="Ännu ej godkänd">*</div>
    </c:if>
  </td>
  <c:forEach items="${form.columns}" var="column">
    <c:if test="${column.visible}">
      <td class="${column.name}">
        <c:choose>
          <c:when test="${row.child == null}">
            <tags:cell value="${row[column.name]}"/>
          </c:when>
          <c:otherwise>
            <tags:cell value="${row.child[column.name]}"/>
          </c:otherwise>
        </c:choose>
      </td>
    </c:if>
  </c:forEach>
</tr>

<c:if test="${row.child != null and su:isPriosDifferent(row, row.child)}">
  <tr class="${index % 2 == 0 ? 'even' : 'odd'}">
    <td>&nbsp;</td>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td class="${column.name}">
          <c:if test="${row.child[column.name] != row[column.name]}">
            <span></span><tags:cell value="${row[column.name]}"/></span>
            <img src='img/flag_white.gif' title="Värde i utkast-version." style="float:right; display:inline;"/>
          </c:if>
        </td>
      </c:if>
    </c:forEach>
  </tr>
</c:if>


          