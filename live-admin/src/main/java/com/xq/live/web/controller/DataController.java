package com.xq.live.web.controller;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 历史数据处理
 * <p>
 * 
 * @author zhangpeng
 *         <p>
 * @data:2015年12月21日下午1:35:07
 * 							<p>
 * @version 1.0
 */
@Controller
public class DataController extends BaseController {
/*	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "orderService")
	private OrderService orderService;
	
	@Resource(name = "accountService")
	private AccountService accountService;
	
	@RequestMapping(value = "/data")
	public String data(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "/data/import";
	}
    @RequestMapping(value="/importUser")
	public String importUser(@RequestParam("uploadUser") CommonsMultipartFile uploadUser, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		InputStream in;
		try {
			// 获取前台exce的输入流
			in = uploadUser.getInputStream();
			Workbook rwb = Workbook.getWorkbook(in);
			Sheet sh = rwb.getSheet(0);// 取第一个sheet
			for (int j = 0; j < sh.getRows(); j++) {
				if(j == 0){
					continue;
				}
				System.err.println("-----------------现在处理 第 ： "+j+" 行数据");
				Cell[] cells = sh.getRow(j);
//				User user = new User();
//				if(cells[1] != null && StringUtils.isNotEmpty(cells[1].getContents())){	//发布订单数
//					user.setOrderNum(Integer.parseInt(cells[1].getContents()));
//				}else{
//					user.setOrderNum(0);
//				}
//				if(cells[2] != null && StringUtils.isNotEmpty(cells[2].getContents())){	//接受订单数
//					user.setReceiverOrderNum(Integer.parseInt(cells[2].getContents()));
//				}else{
//					user.setReceiverOrderNum(0);
//				}
//				if(cells[4] != null && StringUtils.isNotEmpty(cells[4].getContents())){
//					user.setDescription(cells[4].getContents());
//				}
//				if(cells[6] != null && StringUtils.isNotEmpty(cells[6].getContents())){
//					user.setClientVersion(cells[6].getContents());
//				}
//				if(cells[8] != null && StringUtils.isNotEmpty(cells[8].getContents())){
//					user.setIconUrl(cells[8].getContents());
//				}
//				if(cells[9] != null && StringUtils.isNotEmpty(cells[9].getContents())){
//					user.setRobNum(Integer.parseInt(cells[9].getContents()));
//				}
//				if(cells[12] != null && StringUtils.isNotEmpty(cells[12].getContents())){
//					user.setHeight(BigDecimal.valueOf(Double.parseDouble(cells[12].getContents())));
//				}
//				if(cells[15] != null && StringUtils.isNotEmpty(cells[15].getContents())){
//					user.setCity(cells[15].getContents());
//				}
//				if(cells[16] != null && StringUtils.isNotEmpty(cells[16].getContents())){
//					user.setBirthday(DateUtil.fromStringToDate("yyyy-MM-dd", cells[16].getContents()));
//				}
//				if(cells[17] != null && StringUtils.isNotEmpty(cells[17].getContents())){
//					user.setLocationX(cells[17].getContents());
//				}
//				if(cells[18] != null && StringUtils.isNotEmpty(cells[18].getContents())){
//					user.setDeviceId(cells[18].getContents());
//				}
//				if(cells[21] != null && StringUtils.isNotEmpty(cells[21].getContents())){
//					if("0".equals(cells[21].getContents())){
//						user.setSex(2);
//					}else if("1".equals(cells[21].getContents())){
//						user.setSex(1);
//					}else{
//						user.setSex(0);
//					}
//				}
//				if(cells[22] != null && StringUtils.isNotEmpty(cells[22].getContents())){
//					user.setUserName(cells[22].getContents());
//				}
//				if(cells[23] != null && StringUtils.isNotEmpty(cells[23].getContents())){
//					user.setNickName(cells[23].getContents());
//				}
//				if(cells[26] != null && StringUtils.isNotEmpty(cells[26].getContents())){
//					user.setDeviceType(cells[26].getContents());
//				}
//				if(cells[27] != null && StringUtils.isNotEmpty(cells[27].getContents())){
//					user.setInstallId(cells[27].getContents());
//				}
//				if(cells[28] != null && StringUtils.isNotEmpty(cells[28].getContents())){
//					user.setValidMobile(cells[28].getContents());
//				}
//				if(cells[29] != null && StringUtils.isNotEmpty(cells[29].getContents())){
//					user.setLocationY(cells[29].getContents());
//				}
//				if(cells[31] != null && StringUtils.isNotEmpty(cells[31].getContents())){
//					user.setLastAddress(cells[31].getContents());
//				}
//				if(cells[32] != null && StringUtils.isNotEmpty(cells[32].getContents())){
//					user.setLevel(Integer.parseInt(cells[32].getContents()));
//				}
//				userService.add(user);
				Account account = new Account();
				if(cells[5] != null && StringUtils.isNotEmpty(cells[5].getContents())){
					account.setCashAmount(BigDecimal.valueOf(Double.parseDouble(cells[5].getContents())));
				}else{
					account.setCashAmount(BigDecimal.ZERO);
				}
				account.setQsbAmount(BigDecimal.ZERO);
				if(cells[22] != null && StringUtils.isNotEmpty(cells[22].getContents())){
					account.setUserName(cells[22].getContents());
				}
				account.setCreateOperatorName(account.getUserName());
//				accountService.create(account);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "/data/import";
	}
    
    @RequestMapping(value="/uploadOrder")
    public String uploadOrder(@RequestParam("uploadOrder") CommonsMultipartFile uploadOrder, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
    	InputStream in;
		try {
			// 获取前台exce的输入流
			in = uploadOrder.getInputStream();
			Workbook rwb = Workbook.getWorkbook(in);
			Sheet sh = rwb.getSheet(0);// 取第一个sheet
			for (int j = 0; j < sh.getRows(); j++) {
				if(j == 0){
					continue;
				}
				System.err.println("-----------------现在处理 第 ： "+j+" 行数据");
				Cell[] cells = sh.getRow(j);
				So so = new So();
				if(cells[0] != null && StringUtils.isNotEmpty(cells[0].getContents())){	//用户名
					so.setUserName(cells[0].getContents());
					UserInVo inVo = new UserInVo();
					inVo.setUserName(so.getUserName());
					ServiceResult<List<User>> users = userService.queryUsersByInVo(inVo);
					so.setUserId(users.getData().get(0).getId());
					so.setCreateOperatorId(so.getUserId());
					so.setCreateOperatorName(so.getUserName());
				}
				if(cells[1] != null && StringUtils.isNotEmpty(cells[1].getContents())){	//奖励金额
					so.setOrderRewardAmount(BigDecimal.valueOf(Double.parseDouble(cells[1].getContents())));
				}else{
					so.setOrderRewardAmount(BigDecimal.ZERO);
				}
				if(cells[3] != null && StringUtils.isNotEmpty(cells[3].getContents())){
					if("true".equalsIgnoreCase(cells[3].getContents().trim())){
						so.setIsTake(1);
					}else{
						so.setIsTake(0);
					}
				}else{
					so.setIsTake(0);
				}
				if(cells[4] != null && StringUtils.isNotEmpty(cells[4].getContents())){
					so.setOrderRemark(cells[4].getContents());
				}
				if(cells[6] != null && StringUtils.isNotEmpty(cells[6].getContents())){
					so.setLocationX(cells[6].getContents());
				}
				if(cells[7] != null && StringUtils.isNotEmpty(cells[7].getContents())){
					so.setLocationY(cells[7].getContents());
				}
				if(cells[8] != null && StringUtils.isNotEmpty(cells[8].getContents())){
					so.setOrderTitle(cells[8].getContents());
				}
				
				if(cells[12] != null && StringUtils.isNotEmpty(cells[12].getContents())){
					String orderState = cells[12].getContents();
					if("4".equals(orderState)){	//已完成的订单
						so.setOrderStatus(100);
						so.setOrderPaidStatus(1);
					}else if("5".equals(orderState) || "6".equals(orderState)){	//取消的订单
						so.setOrderStatus(30);
						so.setOrderPaidStatus(0);
					}else if("2".equals(orderState)){	//订单已接受
						so.setOrderStatus(3);
						so.setOrderPaidStatus(0);
					}else if("1".equals(orderState)){
						so.setOrderStatus(2);
						so.setOrderPaidStatus(0);
					}else{
						so.setOrderStatus(30);
						so.setOrderPaidStatus(0);
					}
				}
				if(cells[13] != null && StringUtils.isNotEmpty(cells[13].getContents())){
					so.setReceiveUserName(cells[13].getContents());
					UserInVo inVo = new UserInVo();
					inVo.setUserName(so.getReceiveUserName());
					ServiceResult<List<User>> receivers = userService.queryUsersByInVo(inVo);
					so.setReceiveUserId(receivers.getData().get(0).getId());
				}
				if(cells[14] != null && StringUtils.isNotEmpty(cells[14].getContents())){
					so.setOrderTitle(cells[14].getContents());
				}
				so.setOrderType(1);
				if(cells[23] != null && StringUtils.isNotEmpty(cells[23].getContents())){
					String pay = cells[23].getContents().trim();
					if("true".equalsIgnoreCase(pay)){
						so.setOrderPaidStatus(1);
					}else{
						so.setOrderPaidStatus(0);
					}
				}
				if(cells[25] != null && StringUtils.isNotEmpty(cells[25].getContents())){
					so.setOrderPrincipalAmount(BigDecimal.valueOf(Double.valueOf(cells[25].getContents())));
				}
				if(cells[26] != null && StringUtils.isNotEmpty(cells[26].getContents())){
					so.setAddress(cells[26].getContents());
				}
				if(cells[28] != null && StringUtils.isNotEmpty(cells[28].getContents())){	//约会时间
					so.setMeetingTime(DateUtil.fromStringToDate("yyyy-MM-dd hh:mm:ss", cells[28].getContents()));
				}
				if(cells[2] != null && StringUtils.isNotEmpty(cells[2].getContents())){
					so.setCreateTime(DateUtil.fromStringToDate("yyyy-MM-dd hh:mm:ss", cells[2].getContents()));
				}
				if(cells[29] != null && StringUtils.isNotEmpty(cells[29].getContents())){
					so.setOrderTipAmount(BigDecimal.valueOf(Double.parseDouble(cells[29].getContents())));
				}else{
					so.setOrderTipAmount(BigDecimal.ZERO);
				}
				if(cells[31] != null && StringUtils.isNotEmpty(cells[31].getContents())){
					if("1".equals(cells[31].getContents())){
						so.setDateType(1);
					}else if("3".equals(cells[31].getContents())){
						so.setDateType(2);
					}else{
						so.setDateType(3);
					}
				}
				so.setPaymentType(1);
				orderService.add(so);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}    	
    	return "/data/import";
    }*/
}
