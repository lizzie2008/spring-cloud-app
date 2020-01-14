package tech.lancelot.apigateway.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tech.lancelot.apigateway.repository.AccessInfoRepository;
import tech.lancelot.apigateway.security.AccessInfo;

import java.util.List;

@Service
public class AccessInfoService {

    private final AccessInfoRepository accessInfoRepository;

    public AccessInfoService(AccessInfoRepository accessInfoRepository) {
        this.accessInfoRepository = accessInfoRepository;
    }

    /**
     * 获取所有访问权限信息
     *
     * @return
     */
    @Cacheable(value = "api-gateway:accessInfo")
    public List<AccessInfo> findAll() {
        return accessInfoRepository.findAll();
    }
}
