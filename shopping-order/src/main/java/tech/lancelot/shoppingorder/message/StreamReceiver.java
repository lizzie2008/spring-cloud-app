package tech.lancelot.shoppingorder.message;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tech.lancelot.shoppingcommon.dto.OrderInput;

import java.io.IOException;


@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {

    @StreamListener(value = StreamClient.INPUT)
    public void process(OrderInput orderInput) {
        log.info("StreamReceiver: {}", orderInput);
    }

//    @StreamListener(value = StreamClient.INPUT)
//    public void process(OrderInput orderInput,
//                        @Header(AmqpHeaders.CHANNEL) Channel channel,
//                        @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
//        log.info("StreamReceiver: {}", orderInput);
//        try {
//            channel.basicAck(deliveryTag, false);//手动确认
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
