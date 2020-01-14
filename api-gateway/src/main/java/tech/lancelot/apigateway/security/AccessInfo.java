package tech.lancelot.apigateway.security;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class AccessInfo implements Serializable {

    /**
     * 访问码.
     */
    @Id
    private String accessKey;

    /**
     * 访问说明.
     */
    private String accessDesc;

    /**
     * 访问模块.
     */
    private String visitModule;

    /**
     * 访问状态, 0：不允许访问 1：允许访问
     */
    private AccessStatus accessStatus;


    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date updateTime;
}
