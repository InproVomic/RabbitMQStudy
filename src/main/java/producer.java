import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class producer {
    public static String QUEUE_NAME = "hello";
    public static void main(String[] args) throws IOException, TimeoutException, IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接参数
        factory.setHost("47.96.80.253");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列，第一次参数是队列名称，第二个参数是是否持久化(保存到磁盘)，第三个参数是否排他(是否只有一个消费者可用)
        // 第四个参数是否自动删除(最后一个消费者断开连接的时候是否自动删除)，第五个参数是其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送消息
        String message = "Hello World!";
        //第一个参数是交换机名称，第二个参数是路由key(本次队列的名称)，第三个参数是消息的其他属性，第四个参数是消息体
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }
}
