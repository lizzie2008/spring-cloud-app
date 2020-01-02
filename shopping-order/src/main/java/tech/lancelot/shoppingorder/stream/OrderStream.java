package tech.lancelot.shoppingorder.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OrderStream {

    String STOCK_APPLY_OUTPUT = "stock_apply_output";
    @Output(OrderStream.STOCK_APPLY_OUTPUT)
    MessageChannel stockApplyOutput();

    String STOCK_RESULT_INPUT = "stock_result_input";
    @Input(OrderStream.STOCK_RESULT_INPUT)
    SubscribableChannel stockResultInput();
}
