package tech.lancelot.shoppingcommon.dto;

import lombok.Data;

@Data
/*公共结果返回对象*/
public class ResultOutput<T> {

    private Integer code;

    private String msg;

    private T data;

    public static ResultOutput success(Object object )
    {
        ResultOutput resultOutput=new ResultOutput();
        resultOutput.setCode(0);
        resultOutput.setMsg("OK");
        resultOutput.setData(object);

        return resultOutput;
    }

}
