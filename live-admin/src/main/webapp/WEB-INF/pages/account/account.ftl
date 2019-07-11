<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<#include  "${base}/common/meta.ftl">
	<script type="text/javascript" src="${base}/js/commons/YDataGrid.js"></script>
	<script type="text/javascript" src="${base}/js/ux/account/account.js"></script>
  </head>
  <body>
<div class="warp easyui-panel" data-options="border:false">
 	 <!-- Search panel start -->
 	 <div class="easyui-panel ui-search-panel" title="Search box" data-options="striped: true,collapsible:true,iconCls:'icon-search'">  
 	 <form id="searchForm">
 	 	<p class="ui-fields">
           <label class="ui-label">用户账号:</label> 
           <input name="userName" class="easyui-box ui-text" style="width:100px;">
           <label class="ui-label">提现进度:</label> 
           <select class="easyui-combobox" name="status">
            	<option value="" selected="selected">--全部--</option>
            	<option value="1">提现中</option>
            	<option value="2">已完成</option>
            	<option value="3">已拒绝</option>
            	<option value="4">失败</option>
        	</select>
        </p>
        <a href="#" id="btn-search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
      </form>  
     </div> 
     <!--  Search panel end -->
     <!-- Data List -->
     <form id="listForm" method="post">
     	<table id="data-list"></table>
	 </form>
</div>
  </body>
</html>
