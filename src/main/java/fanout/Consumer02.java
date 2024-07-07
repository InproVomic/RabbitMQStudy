package fanout;

import Utils.ForChannel;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息...");
        // 消费消息
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println("02收到消息：" + msg);
        }, (consumerTag) -> {
            System.out.println("消息消费失败");
        });
    }
}
