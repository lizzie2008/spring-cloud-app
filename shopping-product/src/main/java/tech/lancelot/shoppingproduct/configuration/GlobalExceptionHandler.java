package tech.lancelot.shoppingproduct.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.lancelot.shoppingcommon.dto.ResultVo;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultVo globalException(HttpServletResponse response, Exception ex){

        log.info("错误代码："  + response.getStatus());
        return  ResultVo.error(new Integer(response.getStatus()),ex.getMessage());
    }
}