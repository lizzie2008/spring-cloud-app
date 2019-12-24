package tech.lancelot.shoppingprovider.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lancelot.shoppingprovider.domain.order.OrderMaster;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
}
