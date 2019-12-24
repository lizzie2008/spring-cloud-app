package tech.lancelot.shoppingprovider.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lancelot.shoppingprovider.domain.order.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

}
