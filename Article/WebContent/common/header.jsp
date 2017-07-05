<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="header">
	<div class='logo'>Tiler</div>
	<ul>
		<li class='first'><a href="index.jsp">首页</a></li>
		<li class='item'><a href="javascript:void(0)">书库</a></li>
		<li class='item'><a href="javascript:void(0)">排行</a></li>
		<li class='item'><a href="javascript:void(0)">福利</a></li>
		<li class='item'><a href="javascript:void(0)">赞助</a></li>
	</ul>
	
	<div class='login'>
		<c:choose>
			<c:when  test="${empty sessionScope.username}">
				<span><a href="login.jsp">登录</a></span>  
				<span>|</span>
				<span><a href="javascript:void(0)">注册</a></span>
			</c:when>
			<c:otherwise>
				<span><a href="javascript:void(0)">欢迎您，${sessionScope.username}</a></span>  
				<span>&nbsp;|&nbsp;</span>
				<span><a href="${basePath}/controller/logoutController.jsp">登出</a></span>
			</c:otherwise>
		</c:choose>
	</div>
</div>