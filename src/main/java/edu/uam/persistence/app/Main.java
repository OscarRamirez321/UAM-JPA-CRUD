package edu.uam.persistence.app;

import edu.uam.persistence.entities.Categoria;
import edu.uam.persistence.entities.Libro;
import edu.uam.persistence.services.MyDao;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final MyDao<Categoria, Long> categoriaDao = new MyDao<>(Categoria.class);
    private static final MyDao<Libro, Long> libroDao = new MyDao<>(Libro.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op;
        do {
            System.out.println("\n==== MENU ====");
            System.out.println("1. Crear categoría");
            System.out.println("2. Listar categorías");
            System.out.println("3. Actualizar categoría");
            System.out.println("4. Eliminar categoría");
            System.out.println("5. Crear libro");
            System.out.println("6. Listar libros");
            System.out.println("7. Actualizar libro");
            System.out.println("8. Eliminar libro");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1 -> crearCategoria(sc);
                case 2 -> listarCategorias();
                case 3 -> actualizarCategoria(sc);
                case 4 -> eliminarCategoria(sc);
                case 5 -> crearLibro(sc);
                case 6 -> listarLibros();
                case 7 -> actualizarLibro(sc);
                case 8 -> eliminarLibro(sc);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    private static void crearCategoria(Scanner sc) {
        System.out.print("Nombre categoría: ");
        String nombre = sc.nextLine();
        Categoria c = new Categoria(nombre);
        categoriaDao.create(c);
        System.out.println("Creada: " + c);
    }

    private static void listarCategorias() {
        List<Categoria> list = categoriaDao.findAll();
        list.forEach(System.out::println);
    }

    private static void actualizarCategoria(Scanner sc) {
        System.out.print("ID categoría a actualizar: ");
        Long id = Long.parseLong(sc.nextLine());
        Optional<Categoria> opt = categoriaDao.findById(id);
        if (opt.isEmpty()) { System.out.println("No encontrada"); return; }
        Categoria c = opt.get();
        System.out.print("Nuevo nombre: ");
        c.setNombre(sc.nextLine());
        categoriaDao.update(c);
        System.out.println("Actualizada: " + c);
    }

    private static void eliminarCategoria(Scanner sc) {
        System.out.print("ID categoría a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        categoriaDao.deleteById(id);
        System.out.println("Eliminada si existía");
    }

    private static void crearLibro(Scanner sc) {
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Autor: ");
        String autor = sc.nextLine();
        System.out.print("ID categoría: ");
        Long idCat = Long.parseLong(sc.nextLine());
        Optional<Categoria> cat = categoriaDao.findById(idCat);
        if (cat.isEmpty()) { System.out.println("Categoría no existe"); return; }
        Libro libro = new Libro(titulo, autor, cat.get());
        libroDao.create(libro);
        System.out.println("Creado: " + libro);
    }

    private static void listarLibros() {
        List<Libro> list = libroDao.findAll();
        list.forEach(l -> System.out.println(l));
    }

    private static void actualizarLibro(Scanner sc) {
        System.out.print("ID libro a actualizar: ");
        Long id = Long.parseLong(sc.nextLine());
        Optional<Libro> opt = libroDao.findById(id);
        if (opt.isEmpty()) { System.out.println("No encontrado"); return; }
        Libro l = opt.get();
        System.out.print("Nuevo título: ");
        l.setTitulo(sc.nextLine());
        System.out.print("Nuevo autor: ");
        l.setAutor(sc.nextLine());
        System.out.print("Nuevo ID de categoría: ");
        Long idCat = Long.parseLong(sc.nextLine());
        Optional<Categoria> cat = categoriaDao.findById(idCat);
        cat.ifPresent(l::setCategoria);
        libroDao.update(l);
        System.out.println("Actualizado: " + l);
    }

    private static void eliminarLibro(Scanner sc) {
        System.out.print("ID libro a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        libroDao.deleteById(id);
        System.out.println("Eliminado si existía");
    }
}
