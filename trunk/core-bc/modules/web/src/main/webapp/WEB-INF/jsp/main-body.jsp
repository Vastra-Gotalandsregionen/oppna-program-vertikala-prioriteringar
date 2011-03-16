<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="yui3-g" id="layout">
    
    <% // Sidebar start %>
    <div class="yui3-u" id="filterNav">
        <div class="content">
            <%@ include file="filter-sidebar.jsp" %>
        </div>
    </div>
    <% // Sidebar end %>

    <% // Main content start %>
    <div class="yui3-u" id="main">
        <div class="content">
            <%@ include file="main-content.jsp" %>
        </div>
    </div>
    <% // Main content end %>

</div>
