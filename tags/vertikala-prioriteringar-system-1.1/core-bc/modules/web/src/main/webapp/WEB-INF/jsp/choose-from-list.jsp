<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="head.jsp" %>
<body style="height: 100%;">

<div class="window prio-view choose-from-list">

<form:form cssStyle="margin:auto" action="choose-from-list" method="post" cssClass="choose-code" modelAttribute="ChooseListForm">

<div class="main-wrap">

  <div class="yui3-g">
  
  <div class="yui3-u-5-12 choosepage_leftsection enhancedToolTip">
	  <c:if test="${ChooseListForm.findingVisible}">
	      <h3 class="choosepage-header">
            ${ChooseListForm.filterLabel}
            <c:if test="${not empty ChooseListForm.filterLabelToolTip}">
              <span title="${ChooseListForm.filterLabelToolTip}">
                <img src='img/information.png'/>
              </span>
            </c:if>

            
          </h3>
	      <div>
	      	<form:input path="filterText" id="filterText" cssClass="text-input" />
	      	<input class="button" type="submit" name="filter" value="Filtrera"/>
	      </div>
	  </c:if>
  </div>

  <div class="yui3-u-1-6">
  	&nbsp;
  </div>
  
  <div class="yui3-u-5-12 choosepage_rightsection">
  	&nbsp;
  </div>
  
  </div>

  <div class="yui3-g">
  
  <div class="yui3-u-5-12 choosepage_leftsection">
  <div><h3>${ChooseListForm.notYetChosenLabel}</h3> (${ChooseListForm.sizeOfAllToChoose} stycken)</div>
  </div>

  <div class="yui3-u-1-6">
  	&nbsp;
  </div>
  
  <div class="yui3-u-5-12 choosepage_rightsection">
  	<h3>${ChooseListForm.chosenLabel}</h3> (${ChooseListForm.sizeOfChosen} stycken)
  </div>
  
  </div>

  <div class="yui3-g">
  
  <div class="yui3-u-5-12 choosepage_leftsection">
	  <div>
		  <select name="notYetChosenKeys" multiple="multiple">
		    <c:forEach items="${ChooseListForm.allToChoose}" var="column">

		        <option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>

		    </c:forEach>
		  </select>
	  </div>
  </div>

  <div class="yui3-u-1-6" style="height:100%">
    <div class="padding_small"><input  class="button" type="submit" name="addAll" value="Lägg till alla &rArr;" /></div>
    <div class="padding_small"><input class="button" type="submit" name="add" value="Lägg till &rArr;" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="remove" value="&lArr; Ta bort" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="removeAll" value="&lArr; Ta bort alla" /></div>	
  </div>
  
  <div class="yui3-u-5-12 choosepage_rightsection">
  	<div>
  	    <select name="chosenKeys" multiple="multiple">
      	<c:forEach items="${ChooseListForm.chosen}" var="column">
        	<option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>
      	</c:forEach>
    	</select>  	
  	</div>
	<div class="choose-right-btn-toolbar">
		<input class="button" type="submit" name="cancel" value="Avbryt" />
	    <input class="button" type="submit" name="ok" value="${ChooseListForm.okLabel}" />
	</div>
  	<tags:message-out/>
  </div>
  
  <%-- HAGY
  <c:if test="${ChooseListForm.findingVisible}">
    <div class="yui3-u-1-3">
      <h3>${ChooseListForm.filterLabel}</h3>
      <form:input path="filterText" id="filterText" />
      <input class="button" type="submit" name="filter" value="Filtrera"/>
    </div>
    <div class="yui3-u-1-3"></div>
    <div class="yui3-u-1-3"></div>
  </c:if>
  
  <div class="yui3-u-1-3"><h3>${ChooseListForm.notYetChosenLabel}</h3> (${ChooseListForm.sizeOfAllToChoose-ChooseListForm.sizeOfChosen} stycken)</div>
  <div class="yui3-u-1-3"></div>
  <div class="yui3-u-1-3"><h3>${ChooseListForm.chosenLabel}</h3> (${ChooseListForm.sizeOfChosen} stycken)</div>
    
  <div class="yui3-u-1-3">
  <select name="notYetChosenKeys" multiple="multiple">
    <c:forEach items="${ChooseListForm.allToChoose}" var="column">
      <c:if test="${not su:contains(ChooseListForm.chosen, column)}">
        <option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>
      </c:if>
    </c:forEach>
  </select>
  </div>
    
  <div class="yui3-u" style="height:100%" >
  
    <div class="padding_small"><input  class="button" type="submit" name="addAll" value="Lägg till alla &rArr;" /></div>
    <div class="padding_small"><input class="button" type="submit" name="add" value="Lägg till &rArr;" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="remove" value="&lArr; Ta bort" /> </div>
    <div class="padding_small"><input class="button" type="submit" name="removeAll" value="&lArr; Ta bort alla" /></div>
  </div>
    
  <div class="yui3-u-1-3">
    <select name="chosenKeys" multiple="multiple">
      <c:forEach items="${ChooseListForm.chosen}" var="column">
        <option value="${column[ChooseListForm.idKey]}">${column[ChooseListForm.displayKey]}</option>
      </c:forEach>
    </select>
  </div>
  
  <div class="yui3-u-1-3"></div>
  <div class="yui3-u-1-3"></div>
  <div class="yui3-u-1-3">
    <input class="button" type="submit" name="cancel" value="Avbryt" />
    <input class="button" type="submit" name="ok" value="${ChooseListForm.okLabel}" />
  </div>
  
  </div>

<tags:message-out/>
HAGY --%>
</div>

</form:form>

</div>


</body>
</html>