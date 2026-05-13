package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.exception.RecursoNoEncontradoException;
import com.ecommerce.exception.RegistroException;
import com.ecommerce.model.*;
import com.ecommerce.config.JwtUtil;
import com.ecommerce.repository.AdministradorRepository;
import com.ecommerce.repository.CarritoRepository;
import com.ecommerce.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(ClienteRepository clienteRepository, 
                       AdministradorRepository administradorRepository,
                       CarritoRepository carritoRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.clienteRepository = clienteRepository;
        this.administradorRepository = administradorRepository;
        this.carritoRepository = carritoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (clienteRepository.existsByEmail(request.getEmail()) || 
            administradorRepository.existsByEmail(request.getEmail())) {
            throw new RegistroException("El email ya esta registrado");
        }
        
        String rol = request.getRol() != null ? request.getRol().toUpperCase() : "CLIENTE";
        
        if ("ADMIN".equals(rol)) {
            Administrador admin = new Administrador(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "General"
            );
            admin = administradorRepository.save(admin);
            
            Carrito carrito = new Carrito(admin);
            carritoRepository.save(carrito);
            
            String token = jwtUtil.generateToken(admin.getEmail(), admin.getRol(), admin.getId());
            return new AuthResponse(token, admin.getEmail(), admin.getRol(), admin.getNombre(), admin.getId());
        } else {
            Cliente cliente = new Cliente(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getTelefono(),
                request.getDireccion()
            );
            cliente = clienteRepository.save(cliente);
            
            Carrito carrito = new Carrito(cliente);
            carritoRepository.save(carrito);
            
            String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRol(), cliente.getId());
            return new AuthResponse(token, cliente.getEmail(), cliente.getRol(), cliente.getNombre(), cliente.getId());
        }
    }
    
    public AuthResponse login(LoginRequest request) {
        Cliente cliente = clienteRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (cliente != null && passwordEncoder.matches(request.getPassword(), cliente.getPassword())) {
            String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRol(), cliente.getId());
            return new AuthResponse(token, cliente.getEmail(), cliente.getRol(), cliente.getNombre(), cliente.getId());
        }
        
        Administrador admin = administradorRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (admin != null && passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            String token = jwtUtil.generateToken(admin.getEmail(), admin.getRol(), admin.getId());
            return new AuthResponse(token, admin.getEmail(), admin.getRol(), admin.getNombre(), admin.getId());
        }
        
        throw new RegistroException("Email o contrasena incorrectos");
    }
    
    public Cliente getClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
            .orElseThrow(() -> new RegistroException("Cliente no encontrado"));
    }

    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }

    @Transactional
    public Cliente actualizarPerfil(Long usuarioId, UpdateProfileRequest request) {
        Cliente cliente = getClienteById(usuarioId);
        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            cliente.setNombre(request.getNombre());
        }
        if (request.getTelefono() != null) {
            cliente.setTelefono(request.getTelefono());
        }
        if (request.getDireccion() != null) {
            cliente.setDireccion(request.getDireccion());
        }
        return clienteRepository.save(cliente);
    }
}
