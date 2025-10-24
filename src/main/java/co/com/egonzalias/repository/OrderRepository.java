package co.com.egonzalias.repository;

import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.util.OrderStatus;
import co.com.egonzalias.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser(Users user);
    List<Orders> findByUserAndStatus(Users user, OrderStatus status);
}