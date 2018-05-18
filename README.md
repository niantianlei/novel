# novel
写了个非常非常简陋的小说网站，主要目的是为了操作数据库，不在意网页美观程度。。。。。

博客地址：[https://niantianlei.github.io/](https://niantianlei.github.io/)

---------

> 开始觉得随便写写，搞点简单的东西就没有用框架，参考了好多优秀博客，出bug各种查，慢慢积累经验吧逐渐成长。


开发工具是eclipse，数据库用mysql，我也就会这个。。。

--------------

## 创建项目

### 新建项目

用eclipse 新建名为Article的 Dynamic Web Project，直接Finish。

<img src="/Article/git-img/article.png"/>

| 文件名 | 作用 |
| --- | --- |
| src | 存有各种包，且是一个源文件夹，相当于把文件存在classpath |
| 　annotation | 自定义两个注解，方便提取sql语句 |
| 　bean | 自定义两个注解，方便提取sql语句 |
| 　service | service层，业务实现 |
| 　test | 存放测试类的包 |
| 　util |  工具包 |
| WebContent | 存放各种页面 |
| 　common | 存放公共文件，避免代码大量重复 |
| 　controller | controller层，控制业务流程 |
| 　static | 存放项目的静态资源，如js css img等。 |
| 　WEB-INF | 外部浏览器无法访问，可以把jsp放在这里 |
| 　index.jsp | 首页 |
| 　login.jsp | 登录界面 |

### 新建web.xml
在WEB-INF目录下新建一个web.xml    
<img src="/Article/git-img/web.png"/>

web.xml对web项目进行基本的配置。然后编写web.xml  
```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">

 <!-- 欢迎页面 -->
 <welcome-file-list>
   <welcome-file>index.jsp</welcome-file>
 </welcome-file-list>

</web-app>
```
### 新建index.jsp  
在WebContent目录下新建一个index.jsp  
<img src="/Article/git-img/index.png"/>  
编写index.jsp  
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
　　<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
　　<title>Insert title here</title>
</head>
<body>
    项目搭成。
</body>
</html>
```
 
### 将项目部署到tomcat  
1.百度下载tomcat，配置好环境变量。（百度均可解决）  
2.打开eclipse，依次点击window-Preferences-Server-Runtime Environment。  
3.add一个tomcat版本，我下载的是8.5.就点这个，选择目录，即可。  
<img src="/Article/git-img/tomcat.png"/>    
运行。  
然后在浏览器地址栏输入*http://localhost:8080/Article/*  
网页输出“项目搭成。”

## 首页制作  
和html+css+js一样简单得很。之前写单页面的时候写过很多特效，我把之前写淘宝页面用到的幻灯片功能(jquery和javascript实现)移植到了这里。具体功能也不写了。  
关于页面设计这些前端的东西在这里略过。  

### 导入资源  

想提的就是调用资源的问题，即如何把资源文件导入到index.jsp  
在jsp页面的开头加上：  
```
<%
    String path = request.getContextPath();
    int port = request.getServerPort();
    String basePath  = null;
    if(port==80){
        basePath = request.getScheme()+"://"+request.getServerName()+path;
    }else{
        basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    }
    request.setAttribute("basePath", basePath);
%>
```
request.setAttribute("basePath", basePath) 表示将得到的basePath（项目根路径）存放到request作用域中  
此时，${basePath}即为根路径。  

### 简化
还有就是代码会有很多重复的地方，如顶部导航，写登录界面的时候也会用到。这样我把这些公共代码分离出来。放在common里面。    
<img src="/Article/git-img/common.png"/>  
这样，登录页面直接引入header.jsp，就少写了很多代码。  
taglib.jsp存放前面获取根路径的代码。  

## 登录功能  
从前台获取用户名和密码两个值传递到后端。为实现这一功能在在WebContent目录下新建一个controller包  
*总体流程：*先在前端获取用户名和密码，到controller层，这一层对传过来的用户名和密码进行基本的控制。  
然后继续将数据传递到service层（业务层），这一层会根据具体的业务对你的数据进行判断和分析。  
最后，才传递到dao层，这一层原则上就是和数据库进行交互的。多半是写sql语句然后操作数据库。  
最终，还需要将登录的标志返回给前台。  

dao -> servide -> controller -> JSP

### 从JSP到controller层  

给登录绑定一个click事件，详见login.jsp    
```
<button class="login_btn" onclick="login()">登录</button>
```
利用ajax方法将数据提交到controller层:
```
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
```
在controller包里新建loginController.jsp  
```
<%@ page language="java" import="java.util.*,service.LoginService,util.StringUtils,bean.*" pageEncoding="UTF-8"%>
<%

	//获取客户端传递过来参数
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	//System.out.println(username);//测试用
	//System.out.println(password);//测试用
	//如果用户名和密码不为空
	if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
		out.print("-1");//错误码-1 :   用户名和密码不能为空！
	}else{
		//初始化LoginService
		LoginService loginService = new LoginService();
		//接下来判断用户名是否存在
		User user = loginService.getUser(username);
		if(user == null){
			out.print("-2");//错误码-2 :   用户名不存在！
		}else
			//如果用户名存在，那么验证用户名和密码是否匹配
			if(!username.equals(user.getUsername()) || !password.equals(user.getPassword())){
				out.print("-3");//错误码-3 :   用户名或密码错误！
			}else{
				//如果能到这一步，就说明用户的确存在，而且账号密码也正确。那么就把user放在session中
				out.print("1");
				session.setAttribute("user", user);
				session.setAttribute("username", user.getUsername());
			}
	}
	
%>
```
<img src="/Article/git-img/controller.png"/>

### 利用javaBean得到数据库表  
就是将javaBean转换成建表语句。  
运行TestMain.java,可以得到如下输出  
<img src="/Article/git-img/sql.png"/>   
复制这些语句，登录数据库输入密码，新建database(create database article;)，use article使用此database  
再粘帖刚刚复制到的语句加;  
建好数据库。  

## 编写jdbc.properties
连接数据库必须知道用户名密码和库名。  
在src目录上新建config源文件夹，源文件夹之前提过，里面的文件直接在CLASSPATH根目录下，创建文件，改名为jdbc.properties，输入：    
```
db.username=root
db.password=admin
db.dataBaseName=article
```

## 读取properties文件
在test包下创建一个测试类:
```
InputStream inputStream =  TestProperties.class.getClassLoader().getResourceAsStream("jdbc.properties");
Properties p = new Properties();
try {
	p.load(inputStream);
	System.out.println(p);
	System.out.println(p.getProperty("db.username"));
	System.out.println(p.getProperty("db.password"));
	System.out.println(p.getProperty("db.dataBaseName"));
} catch (IOException e) {
	e.printStackTrace();
}
```
<img src="/Article/git-img/Properties.png"/>

## 封装各种操作方法  
创建DataBaseUtils类，里面编写了各种操作数据库的方法。  
在test包里创建一个TestDataBaseUtil测试类，方便测试各种方法。  
```
DataBaseUtils.config("jdbc.properties");
Connection conn = DataBaseUtils.getConnection();
System.out.println(conn);
```
输出：  
<img src="/Article/git-img/test1.png"/>  
说明成功获取了连接。 


进行添加：
```
String id = UUID.randomUUID() + "";
String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
update("INSERT INTO t_user(id,username,password,sex,create_time,is_delete,address,telephone) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", id,"李四",123456,0,createTime,0,"保密4","保密4");
```
这时，查看表结构，并查询表里是否存在数据。  
<img src="/Article/git-img/test2.png"/>  
添加成功。  


进行查询,测试代码:    
```
DataBaseUtils.config("jdbc.properties");
List list = DataBaseUtils.queryForList("select * from t_user");
System.out.println(list);
```
输出太长，就不截图了：
```
[{password=123456, update_time=2017-07-05 09:29:27.0, address=保密4, headerPic=null, create_time=2017-07-05 00:00:00.0, sex=0, telephone=保密4, id=751f6741-b793-42ef-877b-19709d41be42, email=null, username=李四, is_delete=0}, {password=123456, update_time=2017-07-05 09:25:02.0, address=保密, headerPic=null, create_time=2017-07-05 00:00:00.0, sex=0, telephone=保密, id=cf22bfbd-b9b0-41dc-beba-d83673cd4266, email=null, username=张三, is_delete=0}, {password=123456, update_time=2017-07-05 09:26:43.0, address=保密, headerPic=null, create_time=2017-07-05 00:00:00.0, sex=0, telephone=保密, id=f75dde17-bee4-477c-b293-f9fdf8a6700f, email=null, username=张三, is_delete=0}]
```

查询，queryForMap 实现，只查询一条数据  
```
DataBaseUtils.config("jdbc.properties");
Map map = DataBaseUtils.queryForMap("select * from t_user where username = ?","李四");
System.out.println(map);
```
**对了我刚刚添加的username是李四，也可以搜索张三。**  

## 从controller层到service层  
首先判断用户名和密码是否为空，如果为空，就直接返回一个错误码-1，接下来依次判断用户名是否存在，以及用户名密码是否都正确。只要有一个不符合，就直接返回对应的错误码。然后，如果账号密码都正确，那么就返回一个1，表示登录成功，同时，把user对象和用户名放到session中。session的话，就是浏览器作用域。  
创建LoginService.java    
**测试**  
<img src="/Article/git-img/none.png"/>  
<img src="/Article/git-img/password-error.png"/>  


密码：123456，用户名：李四
<img src="/Article/git-img/success.png"/>  

## 页面更新  
最后更改登录成功后，**登录|注册**字样应该被换掉。  
用到了lib文件夹中的两个jar包*jstl-1.2.jar*和*standard-1.1.2.jar*。  
在taglib.jsp中引入标签库  
```
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
```
此时显示：  
<img src="/Article/git-img/modification.png"/>  
## 登出  
在controller文件夹新建一个logoutController.jsp。删除session中的username并转到登录页面   
```
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%  
    //注销
    session.removeAttribute("username");
    session.invalidate();
    String path = request.getContextPath();
    //获取端口
    int port = request.getServerPort();
    String basePath  = null;
    if(port==80){
        basePath = request.getScheme()+"://"+request.getServerName()+path;
    }else{
        basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    }
    response.sendRedirect(basePath + "/login.jsp");
%>
```
。。。

