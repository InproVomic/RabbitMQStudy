package dead;

import Utils.ForChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String NORMAL_ROUTING_KEY = "normal_routing_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();

        // 设置过期时间，放到channel.basicPublish的第三个参数中
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("1000").build();
        for (int i = 1; i < 10; i++) {
            String message = "Hello World! " + i;
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, properties, message.getBytes());
        }
    }
}
