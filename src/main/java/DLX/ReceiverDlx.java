package DLX;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ReceiverDlx {
    private static final String CONSUMER_QUEUE="queueConsumer";
    public static void main(String [] args0) throws Exception{

        //primeiro criar conexão
        //setar informações para conexão
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.21.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        System.out.println(connection.hashCode());
        //criar um novo canal
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (ConsumerTag, delivery) ->{
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received Message: '"+ message + "'");
        };
        //enviar mensagem
        channel.basicConsume(CONSUMER_QUEUE,true,deliverCallback,ConsumerTag->{});

//        System.out.print("[x] Sent '" + message + "'");
    }
}
