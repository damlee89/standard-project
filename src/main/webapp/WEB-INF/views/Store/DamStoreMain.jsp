<%@page import="org.standard.project.magazine.MagazineVO"%>
<%@page import="java.io.Console"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<style>
body {
	margin-left: 100px;
	margin-right: 10%;
}

.leftbox{
	margin-left: 150px;
	margin-right: 10%;
	margin-top: 10%;
}

.rightbox{
	margin-left: 110px;
	margin-right: 8%;
		margin-top: 10%;
}

</style>
</head>
<%@ include file="../header.jsp"%>

<div class="content_wrap">


<div class="leftbox" style="width: 500px; height: 800px">
 <a href="/project/shop/StoreWomenMain">
 <img class="card-img-bottom" src="<%=request.getContextPath()%>/ResourcesFile/img/StoreMain/2women.PNG" style="width:100%">
 </a>
WOMEN 이건 나중에 지울거 
</div>



<div class="rightbox" style="width: 500px; height: 800px">
<a href="/project/shop/StoreManMain">
 <img class="card-img-bottom" src="<%=request.getContextPath()%>/ResourcesFile/img/StoreMain/2man.PNG" style="width:100%">
 </a>
MEN 이건 나중에 지울거
</div>




</div>


<%@ include file="../footer.jsp"%>