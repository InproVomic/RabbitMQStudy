package topic;

import Utils.ForChannel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String EXCHANGE_NAME = "topicExchange";
    public static final String ROUTING_KEY = "*.orange.*";
    public static final String QUEUE_NAME = "Q1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Routing_key是"+message.getEnvelope().getRoutingKey()+
                    "的消息内容是："+new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息被取消");
        };
        channel.basicConsume(QUEUE_NAME, true,deliverCallback,cancelCallback);
    }
}
