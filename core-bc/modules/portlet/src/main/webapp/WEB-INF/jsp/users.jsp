<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:actionURL var="doUserAction">
    <portlet:param name="action" value="doUserAction"/>
</portlet:actionURL>

<div>
<%@ include file="head.jsp" %>
<div style="height:100%; ">

<div class="main-content">

  <form action="${doUserAction}" method="post">

  <input type="submit" name="edit" value="Ändra" />
  <input type="submit" name="delete" onclick="return confirm('Är du säker på att du du vill radera posten?')" value="Radera" />
  <input type="submit" name="create" value="Ny" />
  <input type="submit" name="toMain" value="Tillbaka" onclick="window.open('${doRowAction}','_self'); return false;" />

  <tags:message-out />

  <table>
    <thead class="headerRow">
      <tr>
        <td> <input type="radio" value="-1234567890" name="id" checked="checked" /> </td>
        <th>Vgr-Id</th>
        <th>Förnamn</th>
        <th>Efternamn</th>
        <th>Ändra</th>
        <th>Godkänna</th>
        <th>Ändra användare</th>
        <th>Sektorsråd</th>
      </tr>
    </thead>
    <tbody >
      <c:forEach items="${users}" var="otherUser">
        <tr>
          <td> <input type="radio" value="${otherUser.id}" name="id" /> </td>
          <td>${otherUser.vgrId}</td>
          <td>${otherUser.firstName}</td>
          <td>${otherUser.lastName}</td>
          <td>${otherUser.editor ? 'Ja' : 'Nej'}</td>
          <td>${otherUser.approver ? 'Ja' : 'Nej'}</td>
          <td>${otherUser.userEditor ? 'Ja' : 'Nej'}</td>
          <td>${otherUser.sektorRaad}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  
  </form>
  </div>

</div>
</div>