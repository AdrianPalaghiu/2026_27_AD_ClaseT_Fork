package app;

import dao.DAOException;
import dao.ProductoDAO;
import dao.implementaciones.ProductoDAOImplFicheros;
import modelo.Producto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * ACT13: fichero de acceso aleatorio con 25 productos (referencia int + precio double)
 * y ajuste de precios.
 * <p>
 * Cada registro ocupa 12 bytes: 4 (int) + 8 (double).
 * El registro número n (el primero es el 1) empieza en el byte (n - 1) * TAM_REGISTRO.
 */
public class DemoDAORAF {

    private static final int NUM_PRODUCTOS = 25;

    public static void main(String[] args) {
        Path ruta = Path.of("productos.dat");
        Random azar = new Random();

        if (!prepararDemo(ruta)) return;

        try {
            ProductoDAO dao = new ProductoDAOImplFicheros(ruta);
            // 1. Rellenar con 25 productos (igual que en ACT13, pero a través del DAO).
            for (int i = 0; i < NUM_PRODUCTOS; i++) {
                double precio = 1 + azar.nextDouble() * 199; // Obtiene un precio entre 1 y 200
                precio = Math.round(precio * 100) / 100.0;  // Redondea a 2 decimales
                dao.insertar(new Producto(1001 + i, precio));
            }

            // 2. Mostrar los productos.
            System.out.println("=== Precios iniciales ===");
            mostrar(dao.listar());
        } catch (DAOException e) {
            System.err.println("Error al acceder al fichero de productos: " + e.getMessage());
        }
    }

    /** Deja la demo en un estado conocido borrando el fichero de la ejecución anterior. */
    private static boolean prepararDemo(Path ruta) {
        try {
            Files.deleteIfExists(ruta);
            return true;
        } catch (IOException e) {
            System.out.println("No se puede preparar la demo: " + e.getMessage());
            return false;
        }
    }

    private static void mostrar(List<Producto> productos) {
        System.out.printf("%-10s %10s%n", "Referencia", "Precio");
        for (Producto p : productos) {
            System.out.printf("%-10d %10.2f%n", p.getReferencia(), p.getPrecio());
        }
    }


} // Fin de la clase app.DemoDAORAF

