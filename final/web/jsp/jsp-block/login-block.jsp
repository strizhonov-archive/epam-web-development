<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="language" value="${not empty language ? language : param.language ? param.language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="content"/>
<div class="login-holder container">
    <div class="login-page">
        <c:if test="${requestScope.afterRegister.equals(true)}">
            <div class="notice">Now you can log in</div>
        </c:if>
        <div class="login">
            <form class="register-form" action="${pageContext.request.contextPath}/?command=register" method="post">
                <input id="username"
                       name="username"
                       type="text"
                       placeholder="<fmt:message key="username"/>"
                       pattern="^[A-Za-z0-9]+$"
                       oninput="checkUsername();"/>
                <input id="password"
                       name="password"
                       type="password"
                       placeholder="<fmt:message key="password"/>"
                       pattern="^[\w!@#\$%\^&]{6,}$"
                       oninput="checkPassword();"/>
                <input id="password_confirmation"
                       name="password_confirmation"
                       type="password"
                       placeholder="<fmt:message key="password.confirmation"/>"
                       oninput="checkPasswordsEquality();"/>
                <input id="email"
                       name="email"
                       type="text"
                       pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$"
                       oninput="checkEmail();"
                       placeholder="<fmt:message key="email"/>" />
                <button id="submit">Register</button>
                <p class="message">Already registered? <a href="#">Sign In</a></p>
            </form>
            <form class="login-form" action="${pageContext.request.contextPath}/?command=login" method="post">
                <input type="text" placeholder="<fmt:message key="username"/>" name="username"/>
                <input type="password" placeholder="<fmt:message key="password"/>" name="password"/>
                <button>Log In</button>
                <p class="message"><fmt:message key="not.registered"/>&nbsp;<a href="#"><fmt:message key="create.account"/></a></p>
            </form>
        </div>
    </div>
</div>
