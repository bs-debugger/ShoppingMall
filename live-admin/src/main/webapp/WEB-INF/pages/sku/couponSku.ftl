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
                <label class="ui-label">面额:</label>
                <input name="amount" class="easyui-box ui-text" style="width:100px;">
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
                <div class="ftitle">券的信息</div>
                <div class="fitem">
                    <label>商品编码:</label>
                    <input class="easyui-validatebox" type="text" name="skuCode" data-options="required:true" />
                </div>
                <div class="fitem">
                    <label>券的类型:</label>
                    <select class="easyui-combobox" name="type" data-options="required:true">
                        <option value="1">平台券</option>
                        <option value="2">商家券</option>
                    </select>
                </div>
                <div class="fitem">
                    <label>面值:</label>
                    <input class="easyui-numberbox" type="text" name="amount" data-options="required:true"/>
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" src="${base}/js/commons/YDataGrid.js"></script>
<script type="text/javascript" src="${base}/js/ux/sku/couponSku.js"></script>
</body>
</html>
