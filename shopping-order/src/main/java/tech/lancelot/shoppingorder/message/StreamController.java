package tech.lancelot.shoppingorder.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingorder.service.OrderService;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/stream")
@Slf4j
public class StreamController {

    private final StreamClient streamClient;

    @Autowired
    public StreamController(StreamClient streamClient) {
        this.streamClient = streamClient;
    }

    @GetMapping("/sendMessage")
    public void sendMessage() {
        OrderInput orderInput=new OrderInput();
        orderInput.setBuyerName("小王");
        orderInput.setBuyerPhone("15011111111");
        orderInput.setBuyerAddress("姥姥家");
        orderInput.setBuyerOpenid("11111");
        streamClient.output().send(MessageBuilder.withPayload(orderInput).build());
    }
}
