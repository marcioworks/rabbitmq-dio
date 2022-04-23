package pubConfirmation;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Arrays;
import java.util.Vector;

public class SecondSenderPubConfirmation {

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
            String message = "this is the message ";
            int setOfMessages = 10;
            int outMessages = 0;


            //enviar mensagem
            for (int i = 0; i < setOfMessages; i++) {
                String bodyMessage = message + i;
                channel.basicPublish(NAME_EXCHANGE,"", null, bodyMessage.getBytes());
                System.out.println("sending message: " + bodyMessage);
                outMessages++;

                if(outMessages == setOfMessages) {
                    //wait 5 seconds
                    channel.waitForConfirmsOrDie(5_000);
                    System.out.println(" message confirmed");
                    outMessages = 0;
                }
            }

            if(outMessages != 0){
                System.out.println();
                channel.waitForConfirmsOrDie(5_000);
                System.out.println("[v] message confirmed");
            }


            System.out.print("[x] Done");
        }
    }
}
