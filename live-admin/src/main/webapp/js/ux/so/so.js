$package('YiYa.so');
YiYa.so = function(){
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
                title:'订单列表',
                url:'dataList.do',
                columns:[[
                    {field:'id',title:'订单id',width:80,sortable:true},
                    {field:'soAmount',title:'订单金额',width:120,align:'right'},
                    {field:'soStatus',title:'订单状态',width:80,align:'center',formatter:function(value,row,index){
                        if(value == 1){
                            return "待支付";
                        }else if(value == 2){
                            return "已支付";
                        }else if(value == 3){
                            return "已核销";
                        }else if(value == 2){
                            return "已取消";
                        }else{
                            return "其他";
                        }
                    }},
                    {field:'soType',title:'订单类型',width:80,align:'center',formatter:function(value,row,index){
                        if(value == 1){
                            return "普通订单";
                        }else if(value == 2){
                            return "商家订单";
                        }else{
                            return "其他";
                        }
                    }},
                    {field:'soType',title:'支付方式',width:80,align:'center',formatter:function(value,row,index){
                        if(value == 1){
                            return "享七支付";
                        }else if(value == 2){
                            return "微信支付";
                        }else if(value == 3){
                            return "支付宝";
                        }else{
                            return "其他";
                        }
                    }},
                    {field:'paidTime',title:'支付时间',width:100},
                    {field:'hxTime',title:'核销时间',width:100},
                    {field:'skuCode',title:'商品编码',width:100,align:'center'},
                    {field:'skuName',title:'商品名称',width:100},
                    {field:'skuNum',title:'购买数量',width:100,align:'right'},
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
    YiYa.so.init();
});