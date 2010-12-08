<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:include page="jsp/head.jsp"/>

<body style="height: 100%;">

<jsp:include page="jsp/main-body.jsp" />



<div class="popup-overlay">

<span class="window prio-view">


<form:form action="prio-save" method="post" modelAttribute="prio">

  <div class="values">
  
    <form:hidden path="id"/>
    <tags:kod key="patientnyttaEffektAatgaerdsKod" label="patientnyttaEffektAatgaerdsKod" />
    <tags:kod key="sektorRaad" label="sektorRaad" />
    <tags:kod key="patientnyttoEvidensKod" label="patientnyttoEvidensKod" />
    <tags:kod key="tillstaandetsSvaarighetsgradKod" label="tillstaandetsSvaarighetsgradKod" />
    <tags:kod key="haelsonekonomiskEvidensKod" label="haelsonekonomiskEvidensKod" />
    <tags:kod key="vaardnivaaKod" label="vaardnivaaKod" />
    <tags:kod key="vaentetidsKod" label="vaentetidsKod" />
    <tags:kod key="aatgaerdsRiskKod" label="aatgaerdsRiskKod" />
    <tags:kod key="rangordningsKod" label="rangordningsKod" />
  
    <tags:textarea key="vaardgivare" label="Vårdgivare"/>
    <tags:text key="vaentetidVeckor" label="Väntetid veckor" />
    
    
    <input type="submit" value="Save" />
    <a href="main">Stäng</a>
    
  </div>

</form:form>


</span>

</div>

</body>
</html>