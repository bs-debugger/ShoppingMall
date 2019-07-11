$package('YiYa.user');
YiYa.user = function(){
	var _box = null;
	var _this = {
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
  				title:'用户列表',
	   			url:'dataList.do',
	   			columns:[[
					{field:'id',checkbox:true},
					{field:'userName',title:'用户名',width:120,sortable:true},
					{field:'nickName',title:'昵称',width:120,sortable:true},
					{field:'sex',title:'性别',width:80,align:'center',sortable:true,formatter:function(value,row,index){
						if(value == 1){
							return "男";
						}else if(value == 2){
							return "女";
						}else{
							return "保密";
						}
					}},
                    {field:'iconUrl',title:'头像url',width:200,sortable:true},
					{field:'birthday',title:'生日',width:100,sortable:true,formatter: formatDatebox},
					{field:'createTime',title:'创建时间',width:150,sortable:true}
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
	YiYa.user.init();
});		