package com.czdsb.demo.websocket;

import com.czdsb.demo.model.Record;
import com.czdsb.demo.service.CameraService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;

@ServerEndpoint(value = "/admin/picture", configurator = WebSocketConfig.class)
@Component
public class PictureSocket {

    private static CameraService cameraService;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    public static void setApplicationContext(ApplicationContext context) {
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
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        String[] messages = message.split("\n");

        try {
            // 客户端发来摄像头编号和
            // 指定离最近时间的第几张图片
            String cameraId = messages[0];
            Integer index = Integer.valueOf(messages[1]);

            //...
            Record record = cameraService.findRecords(cameraId, index);
            File file = new File("src\\main\\resources\\invadeReport\\" +
                    record.getRecordId() + ".jpg");
            FileInputStream in = new FileInputStream(file);
            //获取指定文件的长度并用它来创建一个可以存放内容的字节数组
            byte[] content = new byte[in.available()];
            //将图片内容读入到字节数组
            in.read(content);
            //使用刚才的字节数组创建ByteBuffer对象
            ByteBuffer byteBuffer = ByteBuffer.wrap(content);
            sendBinary(byteBuffer);
//            sendText(record.getInvadeType());
//            sendText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(record.getInvadeTime()));
            //关闭文件流对象
            in.close();
        } catch (IOException e) {
            onError(session, e);
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("picSocket发生错误");
        error.printStackTrace();
    }

    public void sendBinary(ByteBuffer byteBuffer) throws IOException {
        this.session.getBasicRemote().sendBinary(byteBuffer);
    }
//    public void sendText(String string) throws IOException {
////        this.session.getBasicRemote().sendText(string);
////    }
}