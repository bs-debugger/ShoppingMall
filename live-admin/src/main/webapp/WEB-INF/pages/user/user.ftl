<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<#include  "${base}/common/meta.ftl">
  </head>
  <body>
<div class="warp easyui-panel" data-options="border:false">
 	 <!-- Search panel start -->
 	 <div class="easyui-panel ui-search-panel" title="Search box" data-options="striped: true,collapsible:true,iconCls:'icon-search'">  
 	 <form id="searchForm">
 	 	<p class="ui-fields">
           <label class="ui-label">用户账号:</label> 
           <input name="userName" class="easyui-box ui-text" style="width:100px;">
        </p>
        <a href="#" id="btn-search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
      </form>  
     </div> 
     <!--  Search panel end -->

     <!-- Data List -->
     <form id="listForm" method="post">
     <table id="data-list"></table>
	 </form>
     <!-- Edit Win&Form -->
     <div id="edit-win" class="easyui-dialog" title="Basic window" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:500px;height:480px;">  
     	<form id="editForm" class="ui-form" method="post">  
     		 <input class="hidden" name="id">
     		 <div class="ui-edit">
		     	   <div class="ftitle">用户信息</div>    
		           <div class="fitem">  
		               <label>账号:</label>  
		               <input class="easyui-validatebox" type="text" name="userName" data-options="required:true" />
		           </div>  
		            <div class="fitem">  
		               <label>密码:</label>  
		               <input class="easyui-validatebox" type="password" name="password" data-options="required:true" />
		           </div> 
		            <div class="fitem">  
		               <label>昵称:</label>  
		               <input class="easyui-validatebox" type="text" name="nickName" data-options="required:true" />
		           </div> 		           
		           <div class="fitem">
		               <label>年龄:</label>  
		               <input class="easyui-numberbox" type="text" name="age" />
		           </div> 
		           <div class="fitem">  
		               <label>性别:</label>  
		               <select class="easyui-combobox" name="sex" data-options="required:true">
	                    	<option value="0" selected="selected">保密</option>
	                    	<option value="1">男</option>
	                    	<option value="2">女</option>
                    	</select>
		           </div> 
		           <div class="fitem">  
		               <label>出生日期:</label>  
		               <input class="easyui-datebox" type="text" name="birthday" />
		           </div>		           
		           <div class="fitem">
		               <label>头像:</label>  
		               <input class="easyui-validatebox" type="text" value="http://" name="iconUrl" data-options="required:true,validType:'url'">
		           </div> 			           	           
	         </div>
     	</form>
  	 </div> 
</div>

<script type="text/javascript" src="${base}/js/commons/YDataGrid.js"></script>
<script type="text/javascript" src="${base}/js/ux/user/user.js"></script>
  </body>
</html>
