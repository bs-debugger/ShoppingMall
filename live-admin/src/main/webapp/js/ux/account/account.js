$package('YiYa.account');
YiYa.account = function(){
	var _box = null;
	var _this = {
		payMoney:function(id){
			$.messager.confirm('Confirm','确认已经打款成功?',function(r){ 
				var option = '';
		    	var url = '/account/payMoney.do?id='+id
				YiYa.progress();
				YiYa.ajaxJson(url,option,function(data){
						YiYa.closeProgress();
						if(data.code == '0'){
							//Grid DataList
							var Grid = $('#data-list');
							var param = $("#searchForm").serializeObject();
							Grid.datagrid('reload',param);
						}else{
							YiYa.alert('提示',result.msg,'error');  
						}
				});
			});
		},			
		config:{
			event:{
				add:function(){
					$('#typeIds_combobox').combobox('reload');
					_box.handler.add();
				},
				edit:function(){
					$('#typeIds_combobox').combobox('reload');
					_box.handler.edit();
				}
			},
  			dataGrid:{
  				title:'提现列表',
	   			url:'dataList.do',
	   			columns:[[
					{field:'id',title:'操作',width:120,align:'center',formatter:function(value,row,index){
						var html ="<a href='#' onclick='YiYa.account.payMoney("+row.id+")'>打款完成</a>";
						return html;
					}},
					{field:'userName',title:'用户名',width:120,sortable:true},
					{field:'nickName',title:'昵称',width:120,sortable:true},
					{field:'accountName',title:'提现账户名',width:120,sortable:true},
					{field:'accountType',title:'账户类型',width:80,align:'center',sortable:true,formatter:function(value,row,index){
						if(value == 1){
							return "支付宝";
						}
						if(value == 2){
							return "微信";
						}
					}},
					{field:'money',title:'提现金额',align:'right',width:80,sortable:true,formatter:function(value,row,index){
						var money = formatNumber(value, 2, true);
						return '<font color="red">'+money+'</font>';
					}},
					{field:'deviceType',title:'设备类型',width:100,sortable:true},
					{field:'deviceId',title:'提现设备ID',width:100,sortable:true},
					{field:'status',title:'状态',width:100,sortable:true,formatter:function(value,row,index){
						if(value == 0){
							return "待提现";
						}else if(value == 1){
							return "提现中";
						}else if(value == 2){
							return "已完成";
						}else if(value == 3){
							return "失败";
						}else{
							return "未知";
						}
					}},
					{field:'createTime',title:'提现时间',width:130,sortable:true},
					{field:'updateTime',title:'更新时间',width:130,sortable:true},
					{field:'updateOperatorName',title:'更新人',width:100,sortable:true}
				]]
			}
		},
		init:function(){
			_box = new YDataGrid(_this.config); 
			_box.init();
		}
	}
	return _this;
}();

$(function(){
	YiYa.account.init();
});		
