package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    
    @Bean
    CommandLineRunner initData(
            ClienteRepository clienteRepository,
            AdministradorRepository administradorRepository,
            ProductoRepository productoRepository,
            CarritoRepository carritoRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            if (administradorRepository.count() == 0) {
                Administrador admin = new Administrador(
                    "Admin Principal",
                    "admin@ecommerce.com",
                    passwordEncoder.encode("admin123"),
                    "Sistemas"
                );
                administradorRepository.save(admin);
                System.out.println("Admin creado: admin@ecommerce.com / admin123");
            }
            
            if (clienteRepository.count() == 0) {
                Cliente cliente1 = new Cliente(
                    "Juan Perez",
                    "juan@email.com",
                    passwordEncoder.encode("cliente123"),
                    "1234567890",
                    "Calle Principal #123"
                );
                clienteRepository.save(cliente1);
                
                Carrito carrito1 = new Carrito(cliente1);
                carritoRepository.save(carrito1);
                
                Cliente cliente2 = new Cliente(
                    "Maria Garcia",
                    "maria@email.com",
                    passwordEncoder.encode("cliente123"),
                    "0987654321",
                    "Av. Secundaria #456"
                );
                clienteRepository.save(cliente2);
                
                Carrito carrito2 = new Carrito(cliente2);
                carritoRepository.save(carrito2);
                
                System.out.println("Clientes creados: juan@email.com / cliente123");
            }
            
            if (productoRepository.count() == 0) {
                productoRepository.save(new Producto(
                    "Laptop HP Pavilion",
                    "Laptop HP Pavilion 15, Intel Core i5, 8GB RAM, 256GB SSD",
                    new BigDecimal("899.99"),
                    10,
                    "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=300&fit=crop",
                    "Electronica"
                ));
                
                productoRepository.save(new Producto(
                    "Mouse Inalambrico",
                    "Mouse inalambrico ergonomico con receptor USB",
                    new BigDecimal("29.99"),
                    50,
                    "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400&h=300&fit=crop",
                    "Accesorios"
                ));
                
                productoRepository.save(new Producto(
                    "Teclado Mecanico",
                    "Teclado mecanico RGB para gaming",
                    new BigDecimal("79.99"),
                    25,
                    "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&h=300&fit=crop",
                    "Accesorios"
                ));
                
                productoRepository.save(new Producto(
                    "Monitor 24 pulgadas",
                    "Monitor LED 24 Full HD 1080p",
                    new BigDecimal("199.99"),
                    15,
                    "https://images.unsplash.com/photo-1527443222154-ea8152d85dbb?w=400&h=300&fit=crop",
                    "Electronica"
                ));
                
                productoRepository.save(new Producto(
                    "Audifonos Bluetooth",
                    "Audifonos inalambricos con cancelacion de ruido",
                    new BigDecimal("149.99"),
                    30,
                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=300&fit=crop",
                    "Electronica"
                ));
                
                productoRepository.save(new Producto(
                    "USB 32GB",
                    "Memoria USB 3.0 de 32GB",
                    new BigDecimal("12.99"),
                    100,
                    "https://images.unsplash.com/photo-1605635151660-5e0c7b3d6a5b?w=400&h=300&fit=crop",
                    "Almacenamiento"
                ));
                
                productoRepository.save(new Producto(
                    " Disco Duro Externo 1TB",
                    "Disco duro portatil 1TB USB 3.0",
                    new BigDecimal("59.99"),
                    20,
                    "https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=400&h=300&fit=crop",
                    "Almacenamiento"
                ));
                
                productoRepository.save(new Producto(
                    "Webcam HD",
                    "Camara web 1080p con microfono integrado",
                    new BigDecimal("49.99"),
                    35,
                    "https://images.unsplash.com/photo-1587826080706-2543b8c0c9b1?w=400&h=300&fit=crop",
                    "Electronica"
                ));
                
                System.out.println("Productos de ejemplo creados: 8");
            }
        };
    }
}
