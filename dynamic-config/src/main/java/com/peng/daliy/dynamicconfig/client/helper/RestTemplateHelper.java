package com.peng.daliy.dynamicconfig.client.helper;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class RestTemplateHelper {

	public static RestTemplate builer() {
		return new RestTemplate(getClientRequestFactory());
	}

	private static ClientHttpRequestFactory getClientRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(1000);
		factory.setReadTimeout(60000);
		return factory;
	}
}
