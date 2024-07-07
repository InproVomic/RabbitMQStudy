package direct;

import Utils.ForChannel;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String ROUTING_KEY = "info";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ForChannel.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes());
        }
    }
}
