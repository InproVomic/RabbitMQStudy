package Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ForChannel {
    public static final String QUEUE_NAME = "ack_queue";
    public static final String IP = "47.96.80.253";
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "123";
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection();
        // 创建通道
        return connection.createChannel();
    }
}
