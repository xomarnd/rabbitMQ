package console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class BlogApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] args) throws Exception {
        runTtBlogApp();
    }

    public static void runTtBlogApp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            Scanner in = new Scanner(System.in);
            System.out.print("Введите тему и сообщение.");
            while (true) {
                String commandMessage = in.nextLine();
                String[] parts = commandMessage.split(" ",2);
                String theme = parts[0];
                String message = parts[1];
                System.out.println("Тема публикации: " + theme);
                channel.basicPublish(EXCHANGE_NAME, theme, null, message.getBytes("UTF-8"));
                System.out.println("Отправлено сообщение '" + message + "'");
            }
        }
    }
}
