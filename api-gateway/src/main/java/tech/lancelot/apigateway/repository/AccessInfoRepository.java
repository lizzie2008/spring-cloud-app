package tech.lancelot.apigateway.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.lancelot.apigateway.security.AccessInfo;

@Repository
public interface AccessInfoRepository extends JpaRepository<AccessInfo, String> {

}
