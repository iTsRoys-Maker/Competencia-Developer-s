package com.ecommerce.repository;

import com.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);
    List<Pedido> findByEstadoOrderByFechaPedidoDesc(String estado);
    List<Pedido> findAllByOrderByFechaPedidoDesc();

    // Cuenta pedidos cuyo estado esté en la lista proporcionada
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado IN :estados")
    long countByEstadoIn(@Param("estados") List<String> estados);

    // Suma el total de pedidos cuyo estado esté en la lista (solo COMPLETADO/PAGADO aportan a ventas)
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estado IN :estados")
    BigDecimal sumTotalByEstadoIn(@Param("estados") List<String> estados);
}
