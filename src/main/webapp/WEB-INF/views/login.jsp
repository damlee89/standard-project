<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�α���</title>
</head>
<body>
<h1>�α���</h1>
<hr>
<form action="login" method="post">
	<table border="1" cellpadding="0" cellpspacing="0">
	<tr>
		<th bgcolor="grey">���̵�</th>
		<td><input type="text" name="c_Id"/></td>
	</tr>
	<tr>	
		<th bgcolor="grey">��й�ȣ</th>
		<td><input type="password" name="c_Password"/></td>
	</tr>
	
	</table>
	<input type="submit" value="�α���"/>
</form>
</body>
</html>