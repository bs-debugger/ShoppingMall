package com.xq.live.task;

import org.springframework.stereotype.Service;

@Service("rewardTask")
public class RewardTask {
/*	@Resource(name = "rewardLogService")
	private RewardLogService rewardLogService;
	
	*//**
	 * <p> 处理评论奖励的任务
	 * <p> 
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	public void doCommentsRewardTask() {
		*//**
		 * 获取当前时间
		 *//*
		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		RewardLogInVo inVo = new RewardLogInVo();
		inVo.setRewardDate(dBefore);
		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.err.println("----评论奖励定时任务开始执行----startTime: " + startTime);
		rewardLogService.doCommentsRewardTask(inVo);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.err.println("----评论奖励定时任务开始执行----endTime: " + endTime);
		System.err.println("评论奖励定时任务运行耗时： " + (endTime - startTime) + "ms");
	}
	
	*//**
	 * <p> 处理点赞奖励的定时任务
	 * <p> 
	 * <p> User: Zhang Peng
	 * <p> Date: 2015年11月16日
	 *//*
	public void doZanRewardTask() {
		*//**
		 * 获取当前时间
		 *//*
		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		RewardLogInVo inVo = new RewardLogInVo();
		inVo.setRewardDate(dBefore);
		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.err.println("----点赞奖励定时任务开始执行----startTime: " + startTime);
		rewardLogService.doZanRewardTask(inVo);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.err.println("----点赞奖励定时任务开始执行----endTime: " + endTime);
		System.err.println("点赞奖励定时任务运行耗时： " + (endTime - startTime) + "ms");
	}	*/
}
