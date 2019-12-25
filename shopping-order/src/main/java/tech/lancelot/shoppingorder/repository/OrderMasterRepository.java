package tech.lancelot.shoppingorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lancelot.shoppingorder.domain.OrderMaster;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
}
