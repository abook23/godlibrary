package com.god.util;

import java.util.UUID;

/**
 * Id生成策略
 * @author Administrator
 *
 */
public class IdUtils {

	/**
	 * 获取32位的UUID
	 * @return
	 */
	public static String getUUId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
