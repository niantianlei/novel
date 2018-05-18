<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include  file="common/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${basePath}/static/js/jquery-3.2.0.js"></script>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/public.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/login.css"/>
<title>登录页面</title>

</head>
<body>

	<%@include file="common/header.jsp" %>
	
	
	<div class='content'>
		<div class='logo'>用户登录</div>
		<div class='inputBox mt50 ml32'>
			<input type="text" id="username" autofocus="autofocus" autocomplete="off" maxlength="60" placeholder="请输入账号/邮箱/手机号">
		</div>
		<div class='inputBox mt50 ml32'>
			<input type="password" id="password" autofocus="autofocus" autocomplete="off" maxlength="60" placeholder="请输入密码">
		</div>
		
		<div class='mt50 ml32'>
			<button class="login_btn" onclick="login()">登录</button>
		</div>
	</div>
	
<script>
		
		function login(){
			var username = $('#username').val();
			var password = $('#password').val();
			$.ajax({
				type:"post",//请求方式
				url:"${basePath}/controller/loginController.jsp",//请求地址
				data:{"username":username,"password":password},//传递给controller的json数据
				error:function(){//如果出错了，将事件重新绑定
					alert("登陆出错！");
				},
				success:function(data){ //返回成功执行回调函数。
					if(data == -1){
						alert('用户名和密码不能为空！');
					}else if(data == -2){
						alert('用户名不存在！');
					}else if(data == -3){
						alert('用户名或密码错误！');
					}else{
						//登录成功后返回首页
						window.location.href = "${basePath}"; 
					}
				}
			});
			
		}
		
</script>

</body>
</html>