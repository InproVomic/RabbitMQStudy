package dead;

import Utils.ForChannel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println(consumerTag + "是从死信队列中接收到" + new String(delivery.getBody()));
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}
