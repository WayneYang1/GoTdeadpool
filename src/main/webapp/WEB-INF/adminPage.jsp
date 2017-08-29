<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Admin Page</title>
</head>
<body>
	<a href="/admin/characters/pages/1">Go to character page list</a>
	<fieldset>
		<legend>Update Kills</legend>
			<form method="POST" action="/admin/updateScores">
				<p><select name="killerId">
					<c:forEach items="${allCharacters}" var="character">
						<option value="${character.id}">${character.fname} ${character.lname}</option>
					</c:forEach>
				</select>
				 Kills 
				 <select name="victimId">
					<c:forEach items="${allCharacters}" var="character">
						<option value="${character.id}">${character.fname} ${character.lname}</option>
					</c:forEach>
				</select>
				</p>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="submit" value="Update scores!">
			</form>
	</fieldset>
	<a href="/">Homepage</a>
</body>
</html>