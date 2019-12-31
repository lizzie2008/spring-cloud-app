package tech.lancelot.shoppingcommon.dto;

import lombok.Data;

@Data
public class ResultVo<T> {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private T data;

    public static ResultVo success(Object object) {
        ResultVo ResultVo = success();
        ResultVo.setData(object);
        return ResultVo;
    }

    public static ResultVo success() {
        ResultVo ResultVo = new ResultVo();
        ResultVo.setCode(0);
        ResultVo.setMsg("成功");
        return ResultVo;
    }

    public static ResultVo error(Integer code,String msg) {
        ResultVo ResultVo = new ResultVo();
        ResultVo.setCode(code);
        ResultVo.setMsg(msg);
        return ResultVo;
    }
}
