package dead;

import Utils.ForChannel;
import Utils.SleepUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer01 {
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String NORMAL_ROUTING_KEY = "normal_routing_key";
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();
        // 设置普通队列参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        //设置队列大小
        //arguments.put("x-max-length", 5);
        //arguments.put("x-message-ttl", 1000);//这个是设置消息过期时间，但是过期时间一般是在生产者中声明
        // 声明普通队列,并且把arguments参数设置到队列中
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, "direct", true);
        // 声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, "direct", true);
        // 绑定死信队列到死信交换机
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);
        // 绑定普通队列到普通交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            if (message.equals("Hello World! 5")){
                // 第二个参数设置为false，表示消息被拒绝并且不重新入队
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }else {
                System.out.println("消费端收到消息：" + message);
                // 第一个参数表示消息的tag，第二个参数为是否批量处理
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag+"消费端取消了订阅");
        };
        //channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,cancelCallback);
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }
}
