$package('YiYa.couponSku');
YiYa.couponSku = function(){
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
                title:'商品列表',
                url:'dataList.do',
                columns:[[
                    {field:'id',checkbox:true},
                    {field:'skuCode',title:'商品编码',width:120,sortable:true},
                    {field:'skuName',title:'商品名称',width:120,sortable:true},
                    {field:'type',title:'券类型',width:80,align:'center',sortable:true,formatter:function(value,row,index){
                        if(value == 1){
                            return "平台券";
                        }else if(value == 2){
                            return "商家券";
                        }else{
                            return "其他";
                        }
                    }},
                    {field:'amount',title:'券面值',width:100,align:'right',sortable:true},
                    {field:'isDeleted',title:'是否删除',width:80,align:'center',sortable:true,formatter:function(value,row,index){
                        if(value == 0){
                            return "否";
                        }else if(value == 1){
                            return "是";
                        }else{
                            return "-";
                        }
                    }},
                    {field:'createTime',title:'创建时间',width:150,sortable:true},
                    {field:'updateTime',title:'更新时间',width:150,sortable:true}
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
    YiYa.couponSku.init();
});