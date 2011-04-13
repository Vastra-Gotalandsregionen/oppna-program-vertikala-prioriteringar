<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<div class="main-content">
	<form action="main" method="post">
    
   		<c:if test="${not empty rows}">
      		<div class="button-row">
        
          		<input type="submit" id="select-prio" name="select-prio" value="Visa prioriteringsobjekt" class="button"/>
        
        		<c:if test="${loginResult && user != null and user.editor}">
            		<span class="rPadding2em">
						<tags:editSubmit name="edit-prio" value="Ändra" cssClass="button"/>
						<tags:editSubmit name="prio-create" value="Lägg till nytt" cssClass="button" />
						<tags:editSubmit name="delete-prio" value="Ta bort" cssClass="button"/>
					</span>
				</c:if>
      
        		<c:if test="${su:canEdit(user, editDir) and user.approver}">
            		<tags:editSubmit name="approve-prio" value="Godkänn" cssClass="button"/>
       			</c:if>
          		<input name="init-conf-columns" class="conf-columns button" type="submit" value="Dölj/Visa kolumner" />
                <input type="submit" name="excel" class="excel button" onclick="window.open('table.xls', '_blank'); return false;" value="Excel"/> 
      		</div>
		</c:if>
    
		<tags:message-out/>
    
    <c:if test="${not empty rows}">
    <%@ include file="main-table.jsp" %>
    </c:if>
    
  </form>
  <c:if test="${empty rows}">
    <%@ include file="help.jsp" %>
   </c:if>
              
</div>