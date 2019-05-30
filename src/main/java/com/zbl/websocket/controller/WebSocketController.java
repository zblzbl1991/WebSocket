package com.zbl.websocket.controller;

import com.zbl.websocket.component.WebSocketServer;
import com.zbl.websocket.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

/**
 * @author zhaobaolong
 * @Title: WebSocketController
 * @ProjectName websocket
 * @Description: TODO
 * @date 2019/5/2916:54
 */
@Controller
@RequestMapping("/")
public class WebSocketController {
	@RequestMapping("/")
	public String index(){
		return "index.html";
	}

	@RequestMapping("/login")
	@ResponseBody
	public String login(String name){
		String replace = UUID.randomUUID().toString().replace("-", "");
		return replace;
	}

	@RequestMapping("/send")
	@ResponseBody
	public String sendMessage(String userId,String sendId,String message) throws IOException {


		String sendName = "";
		String userName = "";
		//发送给私人
		userName = WebSocketServer.nameMap.get(userId);
		//获取所有用户
		Collection<WebSocketServer> values = WebSocketServer.socketMap.keySet();
		if (sendId != null && sendId != "") {
			sendName = WebSocketServer.nameMap.get(sendId);


		} else {
			//发送给群体
			for (WebSocketServer item : values) {
				item.sendMessage(userName + "说:" + message);
			}
		}
			return "success";


	}
	@RequestMapping("/userList")
	@ResponseBody
	public List<User> getLoginUser(){
		Set<Map.Entry<String, String>> entries = WebSocketServer.nameMap.entrySet();
		List<User> userList =new ArrayList<>();
		for (Map.Entry<String, String> entry : entries) {
			String id = entry.getKey();
			String name = entry.getValue();
			User user = new User().setId(id).setName(name);
			userList.add(user);
		}
		return userList;
	}
}
