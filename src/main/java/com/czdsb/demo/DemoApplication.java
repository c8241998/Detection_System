package com.czdsb.demo;

import com.czdsb.demo.websocket.MessageSocket;
import com.czdsb.demo.websocket.PictureSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context =
				SpringApplication.run(DemoApplication.class, args);
		MessageSocket.setApplicationContext(context);
		PictureSocket.setApplicationContext(context);
	}
}
