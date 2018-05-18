<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="service.ArticleService"%>
<% ArticleService articleService = new ArticleService(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include  file="common/taglib.jsp"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<script src="${basePath}/static/js/jquery-3.2.0.js"></script>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/public.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/index.css"/>
<link rel="stylesheet" type="text/css" href="${basePath}/static/css/font-awesome.min.css"/>

</head>
<body>

<%@include file="common/header.jsp" %>


<div class="img">
	<div class="content">
        <ul class="imgBox">
            <li><a href="javascript:void(0)"><img src="${basePath}/static/img/1.png" height="280" width="1000"/></a></li>
            <li><a href="javascript:void(0)"><img src="${basePath}/static/img/2.jpg" height="280" width="1000"/></a></li>
            <li><a href="javascript:void(0)"><img src="${basePath}/static/img/3.jpg" height="280" width="1000"/></a></li>
            <li><a href="javascript:void(0)"><img src="${basePath}/static/img/4.jpg" height="280" width="1000"/></a></li>
            <li><a href="javascript:void(0)"><img src="${basePath}/static/img/5.jpg" height="280" width="1000"/></a></li>
        </ul>
        <div class="currentNum">
            <span class="imgNum mark-color"></span>
            <span class="imgNum"></span>
            <span class="imgNum"></span>
            <span class="imgNum"></span>
            <span class="imgNum"></span>
        </div>
        <!-- 控制按键 -->
        <div class="control to-left"><i class="fa fa-angle-left"></i></div>
        <div class="control to-right"><i class="fa fa-angle-right"></i></div>
    </div>
</div>


<!-- 内容区 -->

<div class='main'>
	<br/><br/>
	<div class='category'>
		<div class='title'>校园都市</div>
		<ul class='items'>
		
			<li class='item' id="first">
				<img alt="书" src="http://chuantu.biz/t5/126/1499237621x3396406132.jpg" width="230" height="250"/>
				<div class="tag">最佳畅销书</div>
			</li>
			
			<li class='item'>
				<div class='item-banner'>
					<div class='item-header'>自由自在</div>
					<div class='item-name'>纽约生活</div>
					<div class='item-author'>@李四 著</div>
				</div>
				<div class='item-description'>向往自由的李四</div>
			</li>
			<li class='item'></li>
			<div style='clear:both'></div>
		</ul>
	</div>
	
	
	
	
	<div class='category'>
		<div class='title'>科技玄幻</div>
		<ul class='items'>
			<li class='item'></li>
			<li class='item'></li>
			<li class='item'></li>
			<div style='clear:both'></div>
		</ul>
	</div>

</div>




<script src="${basePath}/static/js/banner-show.js"></script>


</body>
</html>