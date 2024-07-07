package topic;

import Utils.ForChannel;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String EXCHANGE_NAME = "topicExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();

        Map<String,String> bindingKeyMap = new HashMap<String, String>();
        bindingKeyMap.put("quick.orange.rabbit","被队列Q1Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列Q1Q2接收到");
        bindingKeyMap.put("quick.orange.fox","被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配Q2");

        for (Map.Entry<String, String> entry : bindingKeyMap.entrySet()) {
            // 获取路由键和消息
            String routingKey = entry.getKey();
            String message = entry.getValue();
            // 发送消息到交换机，并指定路由键
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
        }
    }
}
