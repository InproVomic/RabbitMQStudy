package direct;

import Utils.ForChannel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String QUEUE_NAME = "queue1";
    public static final String ROUTING_KEY = "info";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        // 消费消息
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("收到消息：" + new String(message.getBody()));
        }, (consumerTag) -> {
            System.out.println(consumerTag + "消费失败");
        });
    }
}
