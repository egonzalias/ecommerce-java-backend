package co.com.egonzalias.service.impl;

import co.com.egonzalias.dto.CreateOrderDTO;
import co.com.egonzalias.dto.OrderProductResponseDTO;
import co.com.egonzalias.dto.OrderResponseDTO;
import co.com.egonzalias.dto.UpdateOrderDTO;
import co.com.egonzalias.entity.OrderProduct;
import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.entity.Products;
import co.com.egonzalias.entity.Users;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.OrderProductRepository;
import co.com.egonzalias.repository.OrderRepository;
import co.com.egonzalias.repository.ProductRepository;
import co.com.egonzalias.repository.UserRepository;
import co.com.egonzalias.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public OrderResponseDTO createOrder(CreateOrderDTO dto) {
        Orders order = new Orders();

        // Obtener la entidad User desde el ID
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomError("User with ID "+dto.getUserId()+" not found" ));
        order.setUser(user);

        List<OrderProduct> orderProducts = dto.getProducts().stream().map(op -> {
            Products product = productRepository.findById(op.getProductId())
                    .orElseThrow(() -> new CustomError("Product with ID "+op.getProductId()+"not found" ));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(op.getQuantity());
            orderProduct.setOrder(order);
            return orderProduct;
        }).collect(Collectors.toList());

        order.setProducts(orderProducts);

        BigDecimal total = orderProducts.stream()
                .map(p -> p.getProduct().getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        Orders savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }


    @Transactional
    @Override
    public OrderResponseDTO updateOrder(Long orderId, UpdateOrderDTO dto) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomError("Order with ID "+orderId+"not found: "));

        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }

        if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
            order.getProducts().clear();

            for (var opDto : dto.getProducts()) {
                Products product = productRepository.findById(opDto.getProductId())
                        .orElseThrow(() -> new CustomError("Product with ID " + opDto.getProductId() + " not found"));

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(opDto.getQuantity());
                orderProduct.setOrder(order);

                order.getProducts().add(orderProduct);
            }

            BigDecimal total = order.getProducts().stream()
                    .map(p -> p.getProduct().getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            order.setTotal(total);
        }

        Orders updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new CustomError("Order with ID "+orderId+" not found");
        }
        orderRepository.deleteById(orderId);
    }

    private OrderResponseDTO mapToResponse(Orders order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());
        List<OrderProductResponseDTO> products = order.getProducts().stream().map(p -> {
            OrderProductResponseDTO pr = new OrderProductResponseDTO();
            pr.setProductId(p.getProduct().getId());
            pr.setQuantity(p.getQuantity());
            pr.setPrice(p.getProduct().getPrice());
            pr.setName(p.getProduct().getName());
            return pr;
        }).collect(Collectors.toList());
        response.setProducts(products);
        return response;
    }
}
