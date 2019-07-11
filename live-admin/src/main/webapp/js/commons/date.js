Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
function formatDatebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		dt = new Date(value);
	}
	return dt.format("yyyy-MM-dd"); // 扩展的Date的format方法(上述插件实现)
}

/**
 * 将数值四舍五入后格式化.
 * 
 * @param num
 *            数值(Number或者String)
 * @param cent
 *            要保留的小数位(Number)
 * @param isThousand
 *            是否需要千分位 0:不需要,1:需要(数值类型);
 * @return 格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatNumber(num, cent, isThousand) {
	num = num.toString().replace(/\$|\,/g, '');

	// 检查传入数值为数值类型
	if (isNaN(num))
		num = "0";

	// 获取符号(正/负数)
	sign = (num == (num = Math.abs(num)));

	num = Math.floor(num * Math.pow(10, cent) + 0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入
	cents = num % Math.pow(10, cent); // 求出小数位数值
	num = Math.floor(num / Math.pow(10, cent)).toString(); // 求出整数位数值
	cents = cents.toString(); // 把小数位转换成字符串,以便求小数位长度

	// 补足小数位到指定的位数
	while (cents.length < cent)
		cents = "0" + cents;

	if (isThousand) {
		// 对整数部分进行千分位格式化.
		for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
			num = num.substring(0, num.length - (4 * i + 3)) + ','
					+ num.substring(num.length - (4 * i + 3));
	}

	if (cent > 0)
		return (((sign) ? '' : '-') + num + '.' + cents);
	else
		return (((sign) ? '' : '-') + num);
}