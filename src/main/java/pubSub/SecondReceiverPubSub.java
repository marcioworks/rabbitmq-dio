package pubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class SecondReceiverPubSub {
    private static String NAME_QUEUE = "broadcast";
    private static String NAME_EXCHANGE = "fanoutExchange";

    private static void doWork(String task) throws InterruptedException {
        for(char ch:task.toCharArray()){
            if(ch=='.') Thread.sleep(5000);
        }
    }

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
        //declarar a fila que sera utilizada
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //definir exchange
        channel.exchangeDeclare(NAME_EXCHANGE,"fanout");

        //definir o bind
        channel.queueBind(NAME_QUEUE,NAME_EXCHANGE,"");

        DeliverCallback deliverCallback = (ConsumerTag, delivery) ->{
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received Message: '"+ message + "'");

        };
        //enviar mensagem
        boolean Autoack= false;// ack is on
        channel.basicConsume(NAME_QUEUE,true,deliverCallback,ConsumerTag->{});

//        System.out.print("[x] Sent '" + message + "'");
    }
}
