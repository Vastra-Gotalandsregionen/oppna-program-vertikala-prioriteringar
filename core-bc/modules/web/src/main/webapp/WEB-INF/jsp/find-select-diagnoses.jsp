<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%-- 
  <div>
    Sök diagnos: <form:input path="diagnosSearchWord"/>
  </div>
  
  <c:forEach items="${prio.foundDiagnoses}" var="item">
    <label for="">
      <form:checkbox path="selectedDiagnosesId" value="${item.id}" disabled="${util:contains(prio.selectedDiagnosesId, item.id)}" />
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  
  <div>
  <input type="submit" value="Välj!" name="findDiagnoses" />
  </div>
  
  <c:forEach items="${prio.diagnoser}" var="item">
    <label for="">
      <input type="checkbox" name="selectedDiagnosesId" value="${item.id}" checked="checked"/>
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
--%>
<tags:find-select-codes label="Sök diagnoser:" codeRefName="diagnosRef" codeRef="${prio.diagnosRef}" submitName="findDiagnoses"/>
