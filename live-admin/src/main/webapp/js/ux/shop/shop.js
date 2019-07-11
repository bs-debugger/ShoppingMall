$package('YiYa.Shop');
YiYa.Shop = function(){
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
                title:'商家列表',
                url:'dataList.do',
                columns:[[
                    {field:'id',checkbox:true},
                    {field:'shopName',title:'商家名称',width:120,sortable:false},
                    {field:'city',title:'城市',width:120,sortable:false},
                    {field:'applyStatus',title:'审核状态',width:100,align:'center',sortable:false,formatter:function(value,row,index){
                        if(value == 0){
                            return "待审批";
                        }else if(value == 1){
                            return "审批通过";
                        }else{
                            return "审批不通过";
                        }
                    }},
                    {field:'address',title:'地址',width:100,sortable:false},
                    {field:'mobile',title:'联系电话',width:100,sortable:false},
                    {field:'isDeleted',title:'是否删除',width:80,align:'center',sortable:false,formatter:function(value,row,index){
                        if(value == 0){
                            return "否";
                        }else if(value == 1){
                            return "是";
                        }else{
                            return "-";
                        }
                    }},
                    {field:'logoUrl',title:'商家LOGO',width:150,sortable:false},
                    {field:'createTime',title:'创建时间',width:150,sortable:false},
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
    YiYa.Shop.init();
});