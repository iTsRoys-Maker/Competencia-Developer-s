package com.ecommerce.service;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CarritoResponse;
import com.ecommerce.exception.CarritoVacioException;
import com.ecommerce.exception.RecursoNoEncontradoException;
import com.ecommerce.exception.StockInsuficienteException;
import com.ecommerce.model.*;
import com.ecommerce.repository.CarritoRepository;
import com.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    
    public CarritoService(CarritoRepository carritoRepository, ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }
    
    public Carrito getCarritoPorClienteId(Long clienteId) {
        return carritoRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado para el cliente"));
    }
    
    public CarritoResponse getCarritoResponse(Long clienteId) {
        Carrito carrito = getCarritoPorClienteId(clienteId);
        return convertirACarritoResponse(carrito);
    }
    
    @Transactional
    public CarritoResponse agregarAlCarrito(Long clienteId, AddToCartRequest request) {
        Carrito carrito = getCarritoPorClienteId(clienteId);
        Producto producto = productoRepository.findById(request.getProductoId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", request.getProductoId()));
        
        if (!producto.tieneStockSuficiente(request.getCantidad())) {
            throw new StockInsuficienteException(
                producto.getNombre(), 
                producto.getStock(), 
                request.getCantidad()
            );
        }
        
        carrito.agregarItem(producto, request.getCantidad());
        carrito = carritoRepository.save(carrito);
        
        return convertirACarritoResponse(carrito);
    }
    
    @Transactional
    public CarritoResponse removerDelCarrito(Long clienteId, Long productoId) {
        Carrito carrito = getCarritoPorClienteId(clienteId);
        carrito.removerItem(productoId);
        carrito = carritoRepository.save(carrito);
        
        return convertirACarritoResponse(carrito);
    }
    
    @Transactional
    public CarritoResponse limpiarCarrito(Long clienteId) {
        Carrito carrito = getCarritoPorClienteId(clienteId);
        carrito.limpiar();
        carrito = carritoRepository.save(carrito);
        
        return convertirACarritoResponse(carrito);
    }
    
    @Transactional
    public void validarCarrito(Long clienteId) {
        Carrito carrito = getCarritoPorClienteId(clienteId);
        
        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }
        
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (!producto.tieneStockSuficiente(item.getCantidad())) {
                throw new StockInsuficienteException(
                    producto.getNombre(),
                    producto.getStock(),
                    item.getCantidad()
                );
            }
        }
    }
    
    private CarritoResponse convertirACarritoResponse(Carrito carrito) {
        CarritoResponse response = new CarritoResponse();
        response.setId(carrito.getId());
        response.setClienteNombre(carrito.getCliente().getNombre());
        response.setTotal(carrito.getTotal());
        response.setCantidadTotal(carrito.getCantidadTotal());
        
        List<CarritoResponse.ItemCarritoResponse> items = carrito.getItems().stream()
            .map(item -> {
                CarritoResponse.ItemCarritoResponse itemResponse = new CarritoResponse.ItemCarritoResponse();
                itemResponse.setProductoId(item.getProducto().getId());
                itemResponse.setProductoNombre(item.getProducto().getNombre());
                itemResponse.setPrecioUnitario(item.getProducto().getPrecio());
                itemResponse.setCantidad(item.getCantidad());
                itemResponse.setSubtotal(item.getSubtotal());
                return itemResponse;
            })
            .collect(Collectors.toList());
        
        response.setItems(items);
        return response;
    }
}
