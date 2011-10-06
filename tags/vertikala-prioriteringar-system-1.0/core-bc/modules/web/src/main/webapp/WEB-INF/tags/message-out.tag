<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<jsp:useBean id="messageHome" class="se.vgregion.verticalprio.controllers.MessageHome" scope="session"/>
<span style="color:red;"><jsp:getProperty property="message" name="messageHome"/></span>
<jsp:setProperty property="message" name="messageHome" value=""/>