package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    
    @Bean
    CommandLineRunner initData(
            ClienteRepository clienteRepository,
            AdministradorRepository administradorRepository,
            ProductoRepository productoRepository,
            CarritoRepository carritoRepository,
            PedidoRepository pedidoRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // ===== ADMIN =====
            if (administradorRepository.count() == 0) {
                Administrador admin = new Administrador(
                    "Admin Principal",
                    "admin@ecommerce.com",
                    passwordEncoder.encode("admin123"),
                    "Sistemas"
                );
                administradorRepository.save(admin);
                
                Carrito adminCarrito = new Carrito(admin);
                carritoRepository.save(adminCarrito);
                
                System.out.println("Admin creado: admin@ecommerce.com / admin123");
            }
            
            // ===== CLIENTES =====
            Cliente cliente1 = null;
            Cliente cliente2 = null;
            if (clienteRepository.count() == 0) {
                cliente1 = new Cliente(
                    "Juan Perez",
                    "juan@email.com",
                    passwordEncoder.encode("cliente123"),
                    "3001234567",
                    "Cra 15 #45-67, Bogotá"
                );
                clienteRepository.save(cliente1);
                
                Carrito carrito1 = new Carrito(cliente1);
                carritoRepository.save(carrito1);
                
                cliente2 = new Cliente(
                    "Maria Garcia",
                    "maria@email.com",
                    passwordEncoder.encode("cliente123"),
                    "3109876543",
                    "Calle 80 #23-45, Medellín"
                );
                clienteRepository.save(cliente2);
                
                Carrito carrito2 = new Carrito(cliente2);
                carritoRepository.save(carrito2);
                
                System.out.println("Clientes creados: juan@email.com / cliente123");
            } else {
                cliente1 = clienteRepository.findAll().get(0);
                if (clienteRepository.count() > 1) {
                    cliente2 = clienteRepository.findAll().get(1);
                }
            }
            
            // ===== PRODUCTOS (Precios en COP) =====
            if (productoRepository.count() == 0) {
                productoRepository.save(new Producto(
                    "MacBook Air M2",
                    "Laptop Apple MacBook Air con chip M2, 8GB RAM, 256GB SSD, pantalla Liquid Retina 13.6\"",
                    new BigDecimal("4899000"),
                    8,
                    "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&h=450&fit=crop",
                    "Laptops"
                ));
                
                productoRepository.save(new Producto(
                    "Samsung Galaxy S24 Ultra",
                    "Smartphone Samsung Galaxy S24 Ultra, 256GB, cámara 200MP, S Pen integrado",
                    new BigDecimal("5299000"),
                    12,
                    "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&h=450&fit=crop",
                    "Smartphones"
                ));
                
                productoRepository.save(new Producto(
                    "Monitor LG UltraWide 34\"",
                    "Monitor curvo LG 34\" UltraWide QHD IPS, HDR10, 75Hz, USB-C",
                    new BigDecimal("1899000"),
                    15,
                    "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&h=450&fit=crop",
                    "Monitores"
                ));
                
                productoRepository.save(new Producto(
                    "Sony WH-1000XM5",
                    "Audífonos inalámbricos Sony con cancelación de ruido líder en la industria, 30h batería",
                    new BigDecimal("1499000"),
                    25,
                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&h=450&fit=crop",
                    "Audio"
                ));
                
                productoRepository.save(new Producto(
                    "Teclado Mecánico Logitech MX",
                    "Teclado mecánico inalámbrico Logitech MX Mechanical, retroiluminado, low-profile",
                    new BigDecimal("649000"),
                    30,
                    "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&h=450&fit=crop",
                    "Accesorios"
                ));
                
                productoRepository.save(new Producto(
                    "iPad Pro 12.9\" M2",
                    "Tablet Apple iPad Pro 12.9\", chip M2, 128GB, pantalla Liquid Retina XDR",
                    new BigDecimal("5199000"),
                    6,
                    "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600&h=450&fit=crop",
                    "Tablets"
                ));
                
                productoRepository.save(new Producto(
                    "Mouse Logitech MX Master 3S",
                    "Mouse ergonómico inalámbrico Logitech MX Master 3S, sensor 8000 DPI, carga USB-C",
                    new BigDecimal("449000"),
                    40,
                    "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600&h=450&fit=crop",
                    "Accesorios"
                ));
                
                productoRepository.save(new Producto(
                    "SSD Samsung 1TB",
                    "Disco de estado sólido Samsung 870 EVO 1TB SATA III, lectura 560MB/s",
                    new BigDecimal("379000"),
                    50,
                    "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=600&h=450&fit=crop",
                    "Almacenamiento"
                ));
                
                // ===== 12 PRODUCTOS ADICIONALES: Electrónica de Alta Gama =====

                // --- Gaming Laptops ---
                productoRepository.save(new Producto(
                    "ASUS ROG Strix G16",
                    "Laptop gaming ASUS ROG Strix G16, Intel Core i9-13980HX, RTX 4070, 16GB DDR5, 1TB SSD, pantalla 16\" QHD 240Hz",
                    new BigDecimal("8499000"),
                    5,
                    "https://images.unsplash.com/photo-1593642702821-c8da6771f0c6?w=600&h=450&fit=crop",
                    "Gaming"
                ));

                productoRepository.save(new Producto(
                    "MSI Raider GE78 HX",
                    "Laptop gaming MSI Raider GE78 HX, Intel Core i9, RTX 4080, 32GB DDR5, 2TB SSD, pantalla 17\" UHD+ 144Hz",
                    new BigDecimal("12990000"),
                    3,
                    "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=600&h=450&fit=crop",
                    "Gaming"
                ));

                productoRepository.save(new Producto(
                    "Lenovo Legion Pro 7i",
                    "Laptop gaming Lenovo Legion Pro 7i, Intel Core i9-13900HX, RTX 4090, 32GB DDR5, 1TB SSD, pantalla 16\" WQXGA 240Hz",
                    new BigDecimal("14500000"),
                    2,
                    "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600&h=450&fit=crop",
                    "Gaming"
                ));

                // --- Periféricos Premium ---
                productoRepository.save(new Producto(
                    "Razer BlackWidow V4 Pro",
                    "Teclado mecánico gaming Razer BlackWidow V4 Pro, switches Green, RGB Chroma, reposamuñecas magnético",
                    new BigDecimal("1199000"),
                    18,
                    "https://images.unsplash.com/photo-1595225476474-87563907a212?w=600&h=450&fit=crop",
                    "Periféricos"
                ));

                productoRepository.save(new Producto(
                    "Logitech G Pro X Superlight 2",
                    "Mouse gaming inalámbrico Logitech G Pro X Superlight 2, sensor HERO 2, 63g, 32K DPI, autonomía 95h",
                    new BigDecimal("749000"),
                    22,
                    "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&h=450&fit=crop",
                    "Periféricos"
                ));

                productoRepository.save(new Producto(
                    "HyperX Cloud III Wireless",
                    "Audífonos gaming inalámbricos HyperX Cloud III, DTS Spatial Audio, micrófono con cancelación de ruido, 120h batería",
                    new BigDecimal("599000"),
                    30,
                    "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=600&h=450&fit=crop",
                    "Periféricos"
                ));

                // --- Consolas ---
                productoRepository.save(new Producto(
                    "PlayStation 5 Slim",
                    "Consola Sony PlayStation 5 Slim Digital Edition, SSD 1TB, DualSense incluido, resolución 4K HDR",
                    new BigDecimal("2599000"),
                    10,
                    "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=600&h=450&fit=crop",
                    "Consolas"
                ));

                productoRepository.save(new Producto(
                    "Xbox Series X",
                    "Consola Microsoft Xbox Series X 1TB, 4K UHD, hasta 120fps, Game Pass Ultimate compatible, SSD NVMe",
                    new BigDecimal("2799000"),
                    7,
                    "https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=600&h=450&fit=crop",
                    "Consolas"
                ));

                productoRepository.save(new Producto(
                    "Nintendo Switch OLED",
                    "Consola Nintendo Switch OLED, pantalla 7\" OLED, 64GB almacenamiento, Joy-Con Neon, dock incluido",
                    new BigDecimal("1899000"),
                    14,
                    "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=600&h=450&fit=crop",
                    "Consolas"
                ));

                // --- Smart Home ---
                productoRepository.save(new Producto(
                    "Amazon Echo Show 15",
                    "Pantalla inteligente Amazon Echo Show 15, 15.6\" Full HD, Alexa, Fire TV integrado, widgets personalizables",
                    new BigDecimal("1349000"),
                    12,
                    "https://images.unsplash.com/photo-1558089687-f282ffcbc126?w=600&h=450&fit=crop",
                    "Smart Home"
                ));

                productoRepository.save(new Producto(
                    "Google Nest Hub Max",
                    "Pantalla inteligente Google Nest Hub Max 10\", cámara Nest integrada, Google Assistant, Chromecast built-in",
                    new BigDecimal("1099000"),
                    16,
                    "https://images.unsplash.com/photo-1512446816042-444d641267d4?w=600&h=450&fit=crop",
                    "Smart Home"
                ));

                productoRepository.save(new Producto(
                    "Ring Video Doorbell Pro 2",
                    "Timbre inteligente Ring Video Doorbell Pro 2, video 1536p HD+, detección de movimiento 3D, visión nocturna a color",
                    new BigDecimal("899000"),
                    20,
                    "https://images.unsplash.com/photo-1558002038-1055907df827?w=600&h=450&fit=crop",
                    "Smart Home"
                ));

                System.out.println("Productos creados: 20 (precios en COP)");
            }
            
            // ===== PEDIDOS SEMILLA (para que el dashboard NO muestre ceros) =====
            if (pedidoRepository.count() == 0 && cliente1 != null) {
                // Pedido 1: COMPLETADO
                Pedido pedido1 = new Pedido(cliente1, "Cra 15 #45-67, Bogotá");
                pedido1.setMetodoPago("TARJETA");
                pedido1.setFechaPedido(LocalDateTime.now().minusDays(5));
                Producto p1 = productoRepository.findAll().get(0); // MacBook
                pedido1.agregarDetalle(p1, 1, p1.getPrecio());
                pedido1.calcularTotal();
                pedido1.setEstado("COMPLETADO");
                pedido1.setNumeroOrden("ORD-SEED1COMP");
                pedidoRepository.save(pedido1);
                
                // Pedido 2: COMPLETADO
                Pedido pedido2 = new Pedido(cliente1, "Cra 15 #45-67, Bogotá");
                pedido2.setMetodoPago("CONTRA_ENTREGA");
                pedido2.setFechaPedido(LocalDateTime.now().minusDays(3));
                Producto p4 = productoRepository.findAll().get(3); // Sony
                pedido2.agregarDetalle(p4, 2, p4.getPrecio());
                pedido2.calcularTotal();
                pedido2.setEstado("COMPLETADO");
                pedido2.setNumeroOrden("ORD-SEED2COMP");
                pedidoRepository.save(pedido2);
                
                // Pedido 3: PENDIENTE
                if (cliente2 != null) {
                    Pedido pedido3 = new Pedido(cliente2, "Calle 80 #23-45, Medellín");
                    pedido3.setMetodoPago("TARJETA");
                    pedido3.setFechaPedido(LocalDateTime.now().minusDays(1));
                    Producto p2 = productoRepository.findAll().get(1); // Samsung
                    pedido3.agregarDetalle(p2, 1, p2.getPrecio());
                    Producto p5 = productoRepository.findAll().get(4); // Teclado
                    pedido3.agregarDetalle(p5, 1, p5.getPrecio());
                    pedido3.calcularTotal();
                    pedido3.setEstado("PENDIENTE");
                    pedido3.setNumeroOrden("ORD-SEED3PEND");
                    pedidoRepository.save(pedido3);
                    
                    // Pedido 4: CANCELADO
                    Pedido pedido4 = new Pedido(cliente2, "Calle 80 #23-45, Medellín");
                    pedido4.setMetodoPago("CONTRA_ENTREGA");
                    pedido4.setFechaPedido(LocalDateTime.now().minusDays(2));
                    Producto p7 = productoRepository.findAll().get(6); // Mouse
                    pedido4.agregarDetalle(p7, 3, p7.getPrecio());
                    pedido4.calcularTotal();
                    pedido4.setEstado("CANCELADO");
                    pedido4.setNumeroOrden("ORD-SEED4CANC");
                    pedidoRepository.save(pedido4);
                }
                
                System.out.println("Pedidos semilla creados: 4 (2 completados, 1 pendiente, 1 cancelado)");
            }
        };
    }
}
