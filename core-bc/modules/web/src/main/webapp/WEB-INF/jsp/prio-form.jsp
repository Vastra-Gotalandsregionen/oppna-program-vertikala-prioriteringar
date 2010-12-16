<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

  <div class="values">
    Diagnostexter
    <tags:find-select-codes label="Sök diagnoser:" codeRefName="diagnosRef" codeRef="${prio.diagnosRef}" submitName="findDiagnoses" styleClass="diagnosRef"/>

    
    <hr style="clear:both"/>

    <form:hidden path="id"/>
    
    <table style="width:100%">
      <tr>
        <td><tags:kod key="tillstaandetsSvaarighetsgradKod" label="tillstaandetsSvaarighetsgradKod" /></td> 
        <td><tags:kod key="aatgaerdsRiskKod" label="aatgaerdsRiskKod" /></td>
        <td><tags:kod key="patientnyttaEffektAatgaerdsKod" label="patientnyttaEffektAatgaerdsKod" /></td>
        <td><tags:kod key="patientnyttoEvidensKod" label="patientnyttoEvidensKod" /></td>
        <td><tags:kod key="rangordningsKod" label="rangordningsKod" /></td>
      </tr>
      
      <tr>
        <td><tags:kod key="haelsonekonomiskEvidensKod" label="haelsonekonomiskEvidensKod" /></td> 
        <td></td>
        <td><tags:kod key="vaentetidsKod" label="vaentetidsKod" /></td>
        <td><tags:text key="vaentetidVeckor" label="Väntetid veckor" /></td>
        <td><tags:kod key="sektorRaad" label="Sektorsråd" /></td>
      </tr>
    
      <tr>
        <td>Atc-text</td> 
        <td>Atc-kod</td>
        <td><tags:kod key="vaardnivaaKod" label="vaardnivaaKod" /></td>
        <td>Vårdformskoder</td>
        <td><tags:textarea key="kommentar" label="Kommentar"/></td>
      </tr>
    
    
    </table>
    
    
    
    
    
    
    
    
    
    
  
    <tags:textarea key="vaardgivare" label="Vårdgivare"/>
    
    
    <input type="submit" value="Save" name="save" />
    <a href="main">Stäng</a>
    
    
    

  </div>
