<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<#include  "${base}/common/meta.ftl">
	<script>	
		function uploadUser(){
			  document.getElementById("questionTypesManage").action="${base}/importUser";
			  document.getElementById("questionTypesManage").submit();   
		}
		
		function uploadOrder(){
			document.getElementById("questionTypesManage").action="${base}/uploadOrder";
			document.getElementById("questionTypesManage").submit();  
		}
	</script>
  </head>
  <body>
	<form id="questionTypesManage"  method="post" enctype="multipart/form-data">
	   导入用户表：<input type="file" name="uploadUser" id="uploadUser"/>
		   　　<a href="#" class="easyui-linkbutton" style="width:122px" onclick="uploadUser()" >导入user</a> 　　     　
		   
	<br/>
	导入订单表:　    <input type="file" name="uploadOrder" id="uploadOrder"/>
	<a href="#" class="easyui-linkbutton" style="width:122px" onclick="uploadOrder()" >导入订单</a> 　
	</form> 
  </body>
</html>