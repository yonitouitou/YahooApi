package com.tradair.mock.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	public static <T> T fromJson(String json, Class<T> clzz) {
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			T obj = mapper.readValue(json, clzz);
			return obj;
		}
		catch (Exception e) {
			return null;
		}
	}
}
