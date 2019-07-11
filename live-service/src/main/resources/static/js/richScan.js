$(function() {
    //从链接获取qrCode和userId参数
    var qrCode = GetQueryString('qrCode') || '';
    var cashierId = GetQueryString('userId') || '';
    var compareMoney = 0, discountMoney = 0, hxData = {};
    var flag = true;

    //验证input中输入的是否为数字：
    bindKeyEvent($('#accountInp'));

    $.ajax({
        type: "GET",
        url: "/cp/getByCode/" + qrCode,
        dataType: "json",
        success: function(res) {
            console.log(res)
            if(res.code == 0) {
                var data = res.data;
                console.log(data);
                discountMoney = data.couponAmount;
                $('#discountTicket').html(data.skuName + '(' + data.promotionRules[0].ruleDesc + ')');
                $('#discountMoney').html('-' + discountMoney);
                compareMoney = (data.promotionRules[0].ruleDesc).replace(/[^0-9]/ig,"");
                hxData = {
                    soId: data.soId,	        //订单id	Long
                    shopId: "",	       //商家id	Long
                    shopName: "",	   //店铺名称	Date
                    shopAmount: "",	   //消费总额	BigDecimal
                    couponId: data.id,   //电子券id	Long
                    couponCode: data.couponCode,	   //电子券code	String
                    skuId: data.skuId,	       //商品id	Long
                    couponAmount: data.couponAmount,	//电子券面额	BigDecimal
                    userId: data.userId,	//消费人id	Long
                    userName: data.userName,  //消费人账号	String
                    cashierId: cashierId,	    //收银id	Long
                    cashierName: ""	    //收银账号	String
                }
            }
        },
        error: function(err) {
            console.log("error");
        }
    });

    //实时获取输入金额并计算实际价格
    $('#accountInp').bind('input', function() {
        var val = $(this).val();
        if(isNaN(val) || val == "") {
            $('.okBtn').removeClass('active');
        } else {
            $('.okBtn').addClass('active');
            if(+val < compareMoney) {
                $('#discountMoney').html('0');
                $('#actualAmount').html(val);
            } else {
                $('#discountMoney').html('-' + discountMoney);
                $('#actualAmount').html(+val - discountMoney);
            }
        }
    })

    //点击核销按钮调取确认核销接口
    $('#okBtn').click(function() {
        if(flag) {
            var val = $('#accountInp').val();
            if(val != '') {
                hxData.shopAmount = val;
                $.ajax({
                    type: "POST",
                    url: "/hx/add",
                    dataType: "json",
                    data: hxData,
                    success: function(res) {
                        var data = res.data;
                        flag = false;
                        if(res.code == 0) {
                            $('#okBtn').removeClass('active').html('核销成功');
                            $('#accountInp').val('');
                            $('#discountMoney').html(0);
                            $('#actualAmount').html(0);
                            layer.open({
                                content: res.message,
                                skin: 'msg',
                                time: 5
                            });
                        } else {
                            $('#okBtn').removeClass('active').html('确认核销');
                            layer.open({
                                content: res.message,
                                skin: 'msg',
                                time: 7
                            });
                        }
                    },
                    error: function(err) {
                        console.log(err);
                    }
                });
            } else {
                console.log('error');
            }
        }
    })
})

//限制输入框只能输入金额
function bindKeyEvent(obj){
    obj.keyup(function () {
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    }).change(function () {
        $(this).keypress();
        var v = $(this).val();
        if (/\.$/.test(v))
        {
            $(this).val(v.substr(0, v.length - 1));
        }
    });
}

//获取链接后参数
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return r[2]
    return null;
}