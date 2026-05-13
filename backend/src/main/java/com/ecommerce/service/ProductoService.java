package com.ecommerce.service;

import com.ecommerce.exception.RecursoNoEncontradoException;
import com.ecommerce.model.Producto;
import com.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
    
    public List<Producto> getProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    public List<Producto> buscarProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCase(query);
    }
    
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
    @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = getProductoById(id);
        
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setImagenUrl(productoActualizado.getImagenUrl());
        producto.setCategoria(productoActualizado.getCategoria());
        
        return productoRepository.save(producto);
    }
    
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = getProductoById(id);
        productoRepository.delete(producto);
    }
    
    @Transactional
    public Producto actualizarStock(Long id, int cantidad) {
        Producto producto = getProductoById(id);
        int nuevoStock = producto.getStock() - cantidad;
        
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }
    
    public List<Producto> getProductosDisponibles() {
        return productoRepository.findByStockGreaterThan(0);
    }
}
