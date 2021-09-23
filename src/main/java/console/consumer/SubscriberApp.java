package console.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class SubscriberApp {
    private static final String EXCHANGE_NAME = "directExchanger";

    public static void main(String[] args) throws Exception {
        runSubscriberApp();
    }

    public static void runSubscriberApp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        Scanner in = new Scanner(System.in);
        System.out.print("Введите команду set_topic с интересующей темой.");
        String commandMessage = in.nextLine();

        if (commandMessage.startsWith("set_topic")) {
            String[] parts = commandMessage.split(" ", 2);
            String theme = parts[1];

            channel.queueBind(queueName, EXCHANGE_NAME, theme);

            System.out.println(" [*] Ожидание сообщений");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Получено сообщение '" + message + "'");
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        }
    }
}
