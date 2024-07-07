package ack_queue;

import Utils.ForChannel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = ForChannel.getChannel();
        // 声明队列
        channel.queueDeclare(ForChannel.QUEUE_NAME, false, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 成功回调
        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
           if (multiple) {
               // 批量确认
               // 先建立视图，映射map，然后清除(就是说getOuter也会影响map)
               ConcurrentNavigableMap<Long, String> getOuter = map.headMap(deliveryTag, true);
               getOuter.clear();
           }else {
               // 移除单个确认
               map.remove(deliveryTag);
           }
           System.out.println("确认消息"+deliveryTag);
        };
        // 失败回调
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认消息失败"+deliveryTag);
        };
        // 开启监控
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0;i < 1000 ;++i){
            String message = "hello world"+i;
            channel.basicPublish("", ForChannel.QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 将消息和序号绑定
            map.put(channel.getNextPublishSeqNo(), message);
        }
        System.out.println("批量发送完毕！");
    }
}
