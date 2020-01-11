package tech.lancelot.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 验证token 过滤器
 */
//@Component
public class TokenFilter  extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //测试在url参数中获取token
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
