package ack_queue;

import Utils.ForChannel;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();
        // 声明接收消息的方法
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(consumerTag + "消费者消费了消息：" + new String(message.getBody()));
            // 手动确认消息

            //第一个参数：表示消息的唯一标识(每个消息都有唯一标识) 第二个参数：是否批量确认(一般不要批量确认，因为批量可能存在消息丢失的情况)
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        // 取消消息的方法
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消了消息");
        };
        // 设置不公平分发消息
        channel.basicQos(2);
        // 消费消息
        channel.basicConsume(ForChannel.QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
