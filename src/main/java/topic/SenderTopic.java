package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderTopic {

    private static String NAME_EXCHANGE = "TopicExchange";

    public static void main(String [] args0) throws Exception{

        //primeiro criar conexão
        //setar informações para conexão
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.21.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try(Connection connection = factory.newConnection()) {
            System.out.println(connection.hashCode());
            //criar um novo canal
            Channel channel = connection.createChannel();
            System.out.println(channel);
            //declarar a fila que sera utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            channel.exchangeDeclare(NAME_EXCHANGE,"topic");

            String routingKey = "quick.orange.rabbit";
            String routingKey1 = "orange.rabbit";
            String routingKey2 = "rabbit";

            //criar mensagem
            String message = "hello this is a topic based message system";
            String message1 = "hello this is a topic based message system, key: "+ routingKey1;
            String message2 = "hello this is a topic based message system, key: " + routingKey2;

            //enviar mensagem
            channel.basicPublish(NAME_EXCHANGE,routingKey, null, message.getBytes());
            channel.basicPublish(NAME_EXCHANGE,routingKey1, null, message1.getBytes());
            channel.basicPublish(NAME_EXCHANGE,routingKey2, null, message2.getBytes());

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}
