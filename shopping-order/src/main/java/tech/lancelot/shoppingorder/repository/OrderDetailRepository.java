package tech.lancelot.shoppingorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.lancelot.shoppingorder.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

}
