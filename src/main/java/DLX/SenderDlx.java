package DLX;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Vector;

public class SenderDlx {

    private static String NAME_EXCHANGE = "mainExchange";

    public static void main(String[] args0) throws Exception {

        //primeiro criar conexão
        //setar informações para conexão
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.21.0.2");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection()) {
//            System.out.println(connection.hashCode());
            //criar um novo canal
            Channel channel = connection.createChannel();

            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            System.out.println(selectOk);
            System.out.println(channel);
            //declarar a fila que sera utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            //nome do exchange
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //criar mensagem
            String message = "this is a message made for DLX.";

            //enviar mensagem
            String routingKey = "bkConsumer.test1";
            channel.basicPublish(NAME_EXCHANGE, routingKey, null, message.getBytes());

        }


        System.out.print("[x] Done");
    }
}

