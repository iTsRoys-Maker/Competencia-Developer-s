package com.ecommerce.repository;

import com.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // ── Consultas con JOIN FETCH para cargar usuario y detalles en una sola query ──

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.usuario " +
           "LEFT JOIN FETCH p.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE p.usuario.id = :usuarioId " +
           "ORDER BY p.fechaPedido DESC")
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(@Param("usuarioId") Long usuarioId);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.usuario " +
           "LEFT JOIN FETCH p.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE p.estado = :estado " +
           "ORDER BY p.fechaPedido DESC")
    List<Pedido> findByEstadoOrderByFechaPedidoDesc(@Param("estado") String estado);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.usuario " +
           "LEFT JOIN FETCH p.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllByOrderByFechaPedidoDesc();

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.usuario " +
           "LEFT JOIN FETCH p.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetails(@Param("id") Long id);

    // ── Conteos y sumas ──
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado IN :estados")
    long countByEstadoIn(@Param("estados") List<String> estados);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estado IN :estados")
    BigDecimal sumTotalByEstadoIn(@Param("estados") List<String> estados);
}
