package tech.lancelot.apigateway.security;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流
 */
@Component
public class RateLimitFilter extends ZuulFilter {

    //每秒产生N个令牌
    private static final RateLimiter rateLimiter = RateLimiter.create(100);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }


    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        if (!rateLimiter.tryAcquire()) {
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(httpStatus.value());
        }

        return null;
    }
}
