package com.peng.daliy.dynamicconfig.client.listener;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 自定义环境变量变化事件
 *
 * @author kaiwei.yan@xiaobao100.com
 *
 * @since 2019/3/14
 *
 */
public class EnvironmentChangeEvent extends ApplicationEvent {

	private Map<String, Object> data;

	public EnvironmentChangeEvent(Map<String, Object> data) {
		this(data, data);
	}

	public EnvironmentChangeEvent(Object context, Map<String, Object> data) {
		super(context);
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}
}
