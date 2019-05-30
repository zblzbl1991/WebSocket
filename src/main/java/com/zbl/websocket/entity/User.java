package com.zbl.websocket.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhaobaolong
 * @Title: User
 * @ProjectName websocket
 * @Description: TODO
 * @date 2019/5/3010:05
 */
@Data
@Accessors(chain = true)
public class User {
	private String name;
	private String id;
}
