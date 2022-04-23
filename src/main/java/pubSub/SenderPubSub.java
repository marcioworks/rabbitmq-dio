package pubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderPubSub {

    private static String NAME_EXCHANGE = "fanoutExchange";

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
            channel.exchangeDeclare(NAME_EXCHANGE,"fanout");


            //criar mensagem
            String message = "hello this is a pub sub system";

            //enviar mensagem
            channel.basicPublish(NAME_EXCHANGE,"", null, message.getBytes());

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}
