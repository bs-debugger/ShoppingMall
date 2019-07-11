package com.xq.live.task;

import org.springframework.stereotype.Service;

@Service("soTask")
public class SoTask {
/*	@Resource(name = "orderService")
	private OrderService orderService;
	
	@Resource(name = "orderReceiverService")
	private OrderReceiverService orderReceiverService;
	*//**
	 * <p> 处理订单取消定时任务
	 * <p> 约会时间小于当前时间 并且未匹配成功的订单 系统通过定时任务自动取消——超过约会时间并且未匹配的订单自动取消
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	public void doOrderCancelTask(){
		SoInVo inVo = new SoInVo();
		orderService.doOrderCancelTask(inVo);
	}
	
	*//**
	 * <p> 处理订单完成定时任务
	 * <p> 普通订单： 超过约会时间24小时 并且已支付完成的订单自动完成确认
	 * <p> AA制订单：超过约会时间24小时 完成订单确认
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	public void doOrderFinishTask(){
		SoInVo inVo = new SoInVo();
		orderService.doOrderFinishTask(inVo);
	}
	
	*//**
	 * <p> 处理取消抢单的定时任务
	 * <p> 24小时没有人接受，则更新order_receiver 表的状态为已取消
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	public void doOrderReceiverCancelTask(){
		OrderReceiverInVo inVo = new OrderReceiverInVo();
		orderReceiverService.doOrderReceiverCancelTask(inVo);
	}*/
}
