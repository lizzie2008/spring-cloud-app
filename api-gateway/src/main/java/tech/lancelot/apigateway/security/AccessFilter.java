package tech.lancelot.apigateway.security;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.lancelot.apigateway.service.AccessInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AccessFilter extends ZuulFilter {

    private final AccessInfoService accessInfoService;

    public AccessFilter(AccessInfoService accessInfoService) {
        this.accessInfoService = accessInfoService;
    }

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
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        if (!isAuthorized(request)) {
            HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(httpStatus.value());
        }
        return null;
    }

    /**
     * 判断请求是否有权限
     *
     * @param request
     * @return
     */
    private boolean isAuthorized(HttpServletRequest request) {
        // 检查请求参数是否包含 access_key
        String access_key = request.getParameter("access_key");
        if (!StringUtils.isEmpty(access_key)) {
            // 检查 access_key 是否匹配
            List<AccessInfo> accessInfos = accessInfoService.findAll();
            Optional<AccessInfo> accessInfo = accessInfos.stream()
                    .filter(s -> access_key.equals(s.getAccessKey())).findAny();
            if (accessInfo.isPresent()) {
                return true;
            }
            return false;
        }
        return false;
    }
}