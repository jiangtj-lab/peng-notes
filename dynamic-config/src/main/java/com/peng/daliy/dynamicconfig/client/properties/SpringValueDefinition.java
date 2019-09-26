package com.peng.daliy.dynamicconfig.client.properties;

/**
 * Spring Value definition info
 *
 */
public class SpringValueDefinition {

	/** 属性key */
	private final String key;
	/** 属性占位符 */
	private final String placeholder;
	/** 属性名字 */
	private final String propertyName;

	public SpringValueDefinition(String key, String placeholder, String propertyName) {
		this.key = key;
		this.placeholder = placeholder;
		this.propertyName = propertyName;
	}

	public String getKey() {
		return key;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public String getPropertyName() {
		return propertyName;
	}
}
