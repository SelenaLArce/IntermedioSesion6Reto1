package com.sesion6ejemplo1.demo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class ProductoDataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public ProductoDataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Guardando productos ---");
        guardarProducto(new Producto("Laptop Lenovo", "Laptop de alta gama", 12500.00));
        guardarProducto(new Producto("Mouse Logitech", "Mouse ergonómico", 350.00));
        guardarProducto(new Producto("Teclado Mecánico", "Teclado RGB", 950.00));
        guardarProducto(new Producto("Monitor Dell", "Monitor 27 pulgadas", 3200.00));
        guardarProducto(new Producto("Auriculares Sony", "Auriculares con cancelación de ruido", 600.00));

        System.out.println("Productos con precio mayor a 500:");
        List<Producto> productosMayor500 = productoRepository.findByPrecioGreaterThan(500.00);
        productosMayor500.forEach(System.out::println);

        System.out.println("Productos que contienen 'lap':");
        List<Producto> productosConLap = productoRepository.findByNombreContainingIgnoreCase("lap");
        productosConLap.forEach(System.out::println);

        System.out.println("Productos con precio entre 400 y 1000:");
        List<Producto> productosEntre400y1000 = productoRepository.findByPrecioBetween(400.00, 1000.00);
        productosEntre400y1000.forEach(System.out::println);

        System.out.println("Productos cuyo nombre empieza con 'm':");
        List<Producto> productosEmpiezanConM = productoRepository.findByNombreStartingWithIgnoreCase("m");
        productosEmpiezanConM.forEach(System.out::println);
    }

    private void guardarProducto(Producto producto) {
        try {
            productoRepository.save(producto);
            System.out.println("Guardado: " + producto.getNombre());
        } catch (ConstraintViolationException e) {
            System.err.println("Error de validación al guardar producto: " + producto.getNombre());
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.err.println(" - " + violation.getPropertyPath() + ": " + violation.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error inesperado al guardar producto: " + producto.getNombre() + " - " + e.getMessage());
        }
    }
}