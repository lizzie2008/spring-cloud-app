package tech.lancelot.shoppingproduct.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProductStream {

    String STOCK_APPLY_INPUT = "stock_apply_input";
    @Input(ProductStream.STOCK_APPLY_INPUT)
    SubscribableChannel stockApplyInput();

    String STOCK_RESULT_OUTPUT = "stock_result_output";
    @Output(ProductStream.STOCK_RESULT_OUTPUT)
    MessageChannel stockResultOutput();
}
