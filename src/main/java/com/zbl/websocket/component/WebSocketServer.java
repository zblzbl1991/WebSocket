package com.zbl.websocket.component;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhaobaolong
 * @Title: WebSocketServer
 * @ProjectName websocket
 * @Description: TODO
 * @date 2019/5/2916:50
 */
@Component
@ServerEndpoint("/websocket/{name}/{id}")
public class WebSocketServer {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	public static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
	public static ConcurrentHashMap<WebSocketServer,String> socketMap =new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,String> nameMap =new ConcurrentHashMap<>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	/**
	 * 连接建立成功调用的方法*/
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "id")String id,@PathParam("name")String name) {
		this.session = session;
		System.out.println(name+":"+id);
		nameMap.put(id,name);
		socketMap.put(this,id);
		webSocketSet.add(this);     //加入set中
		addOnlineCount();           //在线数加1
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());


	}


	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this);  //从set中删除
		String id = socketMap.get(this);
		nameMap.remove(id);

		subOnlineCount();           //在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息*/
	@OnMessage
	public void onMessage(String message) {
		System.out.println("来自客户端的消息:" + message);

	}

	/**
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}


	public void sendMessage(String message)  {
		try {
			if(this.session.isOpen()){
				this.session.getBasicRemote().sendText(message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 群发自定义消息
	 * */
	public static void sendInfo(String message) throws IOException {
		System.out.println(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}

}
