package com.xq.live.task;

import org.springframework.stereotype.Service;

@Service("testTaskJob")
public class TestTaskJob {
	public void test(){
		System.err.println("任务执行中…………");
	}
	
	public void test1(){
		System.err.println("任务AAAA执行中…………");
	}
}
