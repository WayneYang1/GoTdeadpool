<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>  
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${deadpool.name }</title>
</head>
<body>
	<fieldset>
		<legend><h2>Deadpool: ${deadpool.name }</h2></legend>
		<c:forEach items="${ud}" var="ud2">
		<c:if test="${ud2.getDeadpool().getId() == deadpool.getId()}">
        		<ul class="list-group">
            		<li class="user"><b>${ud2.getUser().fname} ${ud2.getUser().lname}</b> | Total Score: ${ud2.totalScore}</li>
            		<ol>
            			<c:forEach items="${ud2.getUser().getUserPicks() }" var="pick">
            			<c:if test="${pick.relatedDeadpool.getId() == deadpool.getId()}">
            				<li>${pick.getKiller().fname} ${pick.getKiller().lname} kills ${pick.getVictim().fname } ${pick.getVictim().lname } | Score: ${pick.getScore() }</li>
            			</c:if>
            			</c:forEach>
            		</ol>
        		</ul>
    		</c:if>
		</c:forEach>
        <c:if test="${ host.getId() == currentUser.getId() }">
            <form method="POST" action="/users/addUser">
                <select name="invitedUserId">
                    <c:forEach items="${allUsers }" var="user">
                            <option name="invitedUserId" value="${user.getId() }">${user.fname} ${user.lname}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" value="${deadpool.getId() }" name="deadpoolId">
                <input type="submit" value="Invite User">
            </form>
        </c:if>  
	</fieldset>
	 <c:forEach items="${allUsersInPool }" var="user">
        <c:if test="${user == currentUser }">
            <p><a href="/users/deadpool/${deadpool.id}/addpicks">Add picks</a></p>
        </c:if>
    </c:forEach>
    <c:forEach items="${currentUser.getRoles() }" var="role">
		<c:if test="${role.getName() == 'ROLE_ADMIN'}">
			<p><a href="/admin">Admin Page</a></p>
		</c:if>
	</c:forEach>
	<a href="/">Home Page</a>
</body>
</html>
