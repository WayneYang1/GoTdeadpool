<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/css/home.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>  
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body>
    <div id="header">
        <div id="GoT">
        #
        <br />
          D E A D P O O L
        </div>
        <div id="logout"> 
            <form id="logoutForm" method="POST" action="/logout" class="navbar-form navbar-right">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">log out</button>
            </form>
        </div>
    </div>
    <div id="main">
        <h1>Welcome ${currentUser.fname }</h1>
        <a href="/users/createPool">Create a Deadpool</a>
            <h3>Your deadpool invites:</h3>
                <c:forEach items="${currentUser.getInvitedDeadpools() }" var="deadpool">
                    ${deadpool.name } | <a href="/users/deadpool/${deadpool.id}/accept">Accept Invite</a>
                </c:forEach>
            <h3>Your active deadpools:</h3>
                <c:forEach items="${currentUser.getActiveDeadpools() }" var="deadpool">
                    <p><a href="/users/deadpool/${deadpool.id}">${deadpool.name}</a></p>
                </c:forEach>
                <br>
                <br>
                <br>
            <p><a href="/users/createPool">Create Deadpool</a></p>
            <br>
			<c:forEach items="${currentUser.getRoles() }" var="role">
			<c:if test="${role.getName() == 'ROLE_ADMIN'}">
			<p><a href="/admin">Admin Page</a></p>
		</c:if>
	</c:forEach>
    </div>
</body>
</html>