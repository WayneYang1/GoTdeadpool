<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/css/splash.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GoT Deadpool</title>
</head>
<body>
    <div class="header">
    #
    <br />
      D E A D P O O L

    </div>
    <div id="maincontent">
            <c:if test="${logoutMessage != null}">
        <c:out value="${logoutMessage}"></c:out>
    </c:if>
    <c:if test="${errorMessage != null}">
        <c:out value="${errorMessage}"></c:out>
    </c:if>

    <div class=" container col">
    <h1>Login</h1>
    <form action="/login" method="POST">
        <div class="form-group">
            <label for="email">Email Address
            <input type="text" class="form-control" id="email" name="username" placeholder="Email">
            </label>
        </div>
        <div class="form-group">
           <label for="password">Password
           <input type="password" class="form-control" id="password" name="password"
             placeholder="Password">
            </label>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit"  class="btn btn-default">Login</button>
    </form>
    </div>

    <div class="container col">
    <h1>Register</h1>
    <p><form:errors path="user.*"/></p>
    <form:form method="POST" action="/registration" modelAttribute="user" style="width:300px">
        <div class="form-group">
            <form:label path="fname" for="fname">First Name:</form:label>
            <form:input path="fname" class="form-control" id="fname" placeholder="First Name"/>
        </div>
        <div class="form-group">
            <form:label path="lname" for="lname">Last Name:</form:label>
            <form:input path="lname" class="form-control" id="lname" placeholder="Last Name"/>
        </div>
        <div class="form-group">
            <form:label path="email" for="email">Email:</form:label>
            <form:input path="email" class="form-control" id="email" placeholder="Email"/>
        </div>
        <div class="form-group">
            <form:label path="password" for="password">Password:</form:label>
            <form:password path="password" class="form-control" id="password" placeholder="Password"/>
        </div>
        <div class="form-group">
            <form:label path="passwordConfirmation" for="passwordConfirmation">Confirm Password</form:label>
            <form:password path="passwordConfirmation" class="form-control" id="passwordconfirmation" placeholder="Password Confirmation"/>
        </div>
        <button type="submit"  class="btn btn-default">Register</button>
    </form:form>
    </div>
    </div>
    <audio src="/video/GoT.mp3" autoplay>
	<embed src="/video/GoT.mp3" width="180" height="90" hidden="true" />
	</audio>
</body>
</html>