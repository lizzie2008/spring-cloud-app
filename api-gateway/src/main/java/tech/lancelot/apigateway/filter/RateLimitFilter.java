package tech.lancelot.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流
 */
@Component
public class RateLimitFilter extends ZuulFilter {

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);


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
        if (!RATE_LIMITER.tryAcquire()) {
            throw new RuntimeException("未能获取到令牌.");
        }

        return null;
    }
}
