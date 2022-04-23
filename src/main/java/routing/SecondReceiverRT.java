package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class SecondReceiverRT {

    private static String BINDKEY_NAME="routingKeyTest1";
    private static String NAME_EXCHANGE = "directExchange";
//    private static String NAME_QUEUE = "Work";


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
        System.out.println(channel);
        //o servidor ira definir um nome aleatorio para fila
        //a fila temporaria
        String nameQueue = channel.queueDeclare().getQueue();

        //declarar a fila que sera utilizada
        //nome da fila, exclusiva, autodelete, durable, map(args)
//        channel.queueDeclare(nameQueue, false, false, false, null);

        //definir exchange
        channel.exchangeDeclare(NAME_EXCHANGE,"direct");

        //definir o bind
        channel.queueBind(nameQueue,NAME_EXCHANGE,BINDKEY_NAME);

        DeliverCallback deliverCallback = (ConsumerTag, delivery) ->{
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received Message: '"+ message + "'");
        };
        //enviar mensagem
        channel.basicConsume(nameQueue,true,deliverCallback,ConsumerTag->{});

//        System.out.print("[x] Sent '" + message + "'");
    }
}
