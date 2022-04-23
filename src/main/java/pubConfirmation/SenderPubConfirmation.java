package pubConfirmation;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Arrays;
import java.util.Vector;

public class SenderPubConfirmation {

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

            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            System.out.println(selectOk);
            System.out.println(channel);
            //declarar a fila que sera utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            //nome do exchange
            channel.exchangeDeclare(NAME_EXCHANGE,"fanout");

            String routingKey = "quick.orange.rabbit";

            //criar mensagem
            Vector<String> messages = new Vector<String>(3);
            messages.add("hello world");
            messages.add("this is the second message");
            messages.add("this is the final message");



            //enviar mensagem
            for (int i = 0; i < 3; i++) {
                String body = messages.get(i);
                channel.basicPublish(NAME_EXCHANGE,"", null, body.getBytes());
                System.out.println("sending message: " + body);

                //wait 5 seconds
                channel.waitForConfirmsOrDie(5_000);
                System.out.println(" message confirmed");

            }


            System.out.print("[x] Done");
        }
    }
}
