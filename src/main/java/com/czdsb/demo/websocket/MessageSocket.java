package com.czdsb.demo.websocket;

import com.czdsb.demo.interceptor.WebAppConfig;
import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.User;
import com.czdsb.demo.service.CameraService;
import com.czdsb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

import static java.lang.Thread.getAllStackTraces;
import static java.lang.Thread.sleep;

@ServerEndpoint(value = "/admin/message", configurator = WebSocketConfig.class)
@Component
public class MessageSocket {

    private static final int PERIOD = 5000;

    private Session session;
    private User user;
    private Timer timer;
    private Map<String, Boolean> camerasState = new HashMap<>();

    private static Map<Integer, MessageSocket> socketMap = Collections.synchronizedMap(new HashMap<>());

    public static void noticeUser(String cameraId) {
        MessageSocket socket = socketMap.get(userService.findByCamera(cameraId).getUserId());
        if (socket != null)
            socket.sendMessage("invade\n" + cameraId);
    }

    private static UserService userService;
    private static CameraService cameraService;

    public static void setApplicationContext(ApplicationContext context) {
        userService = context.getBean(UserService.class);
        cameraService = context.getBean(CameraService.class);
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        System.out.println(user + " closed");
        if (timer != null)
            timer.cancel();
        socketMap.remove(user);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (user != null)
            return;

        //用户信息
        user = new User();
        user.setUsername(message);
        user = userService.findByUsername(user.getUsername());
        socketMap.put(user.getUserId(), this);

        for (Camera camera : cameraService.findByUser(user)) {
            Boolean state = camera.getLastAccessedTime() != null && System.currentTimeMillis() - camera.getLastAccessedTime().getTime() <= WebAppConfig.OVERDUE;
            camerasState.put(camera.getCameraId(), state);
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Camera camera : cameraService.findByUser(user)) {
                    Boolean state = camera.getLastAccessedTime() != null && System.currentTimeMillis() - camera.getLastAccessedTime().getTime() <= WebAppConfig.OVERDUE;
                    if (state != camerasState.get(camera.getCameraId())) {
                        sendMessage("state\n" + camera.getCameraId() + "\n" + (state ? "on" : "off"));
                        camerasState.put(camera.getCameraId(), state);
                    }
                }
            }
        }, 0, PERIOD);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(user + " websocket发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            onClose();
        }
    }
}