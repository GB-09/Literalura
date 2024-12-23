package com.aluracursos.Literalura.Principal;

import com.aluracursos.Literalura.Repository.AutorRepository;
import com.aluracursos.Literalura.Repository.LibroRepository;
import com.aluracursos.Literalura.Service.ConsumoApi;
import com.aluracursos.Literalura.Service.ConvierteDatos;
import com.aluracursos.Literalura.Service.IConvierteDatos;
import com.aluracursos.Literalura.model.Autor;
import com.aluracursos.Literalura.model.DatosLibros;
import com.aluracursos.Literalura.model.Libro;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private ConsumoApi consumoApi = new ConsumoApi();
    private Scanner scanner = new Scanner(System.in);
    private String urlBase ="https://gutendex.com/books/";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // Mostrar el menu en consola
    public void MuestraElMenu()
    {
        var opcion = -1;
        while (opcion != 0){
            var menu ="""
                    **************************************************
                        LiterAlura - Busqueda de Libros y Autores
                    **************************************************
                    
                    Selecciona una opcion acontinuacion: 
                    
                    1 - Buscar un libro
                    2 - Consultar libros buscados
                    3 - Consultar autores
                    4 - Consultar autores de un año especifico
                    5 - Consultar libros por lenguaje
                     
                    0 - Salir               
                    """;

            try {
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){

                System.out.println("Ingresa una opcion valida");
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    consultarLibros();
                    break;
                case 3:
                    consultarAutores();
                    break;
                case 4:
                    consultarAutoresPorAno();
                    break;
                case 5:
                    consultarLibrosLenguaje();
                    break;
                case 0:
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Ingresa una opcion valida");
            }
        }
    }

    // Extrae los datos de un libro
    private DatosLibros getDatosLibro() {
        System.out.println("Ingrese el nombre del libro");
        var busqueda = scanner.nextLine().toLowerCase().replace(" ","%20");
        var json = consumoApi.getData(urlBase +
                "?search=" +
                busqueda);

        DatosLibros datosLibro = convierteDatos.obtenerDatos(json, DatosLibros.class);
        return datosLibro;
    }

    // Busca un libro y guarda infromacion en la BD en sus tablas correspondientes
    private void buscarLibro()
    {
        DatosLibros datosLibros = getDatosLibro();

        try {
            Libro libro = new Libro(datosLibros.resultados().get(0));
            Autor autor = new Autor(datosLibros.resultados().get(0).autorList().get(0));

            System.out.println("""
                    libro[
                        titulo: %s
                        autor: %s
                        lenguaje: %s
                        descargas: %s
                    ]
                    """.formatted(libro.getTitulo(),
                    libro.getAutor(),
                    libro.getLenguaje(),
                    libro.getDescargas().toString()));

            libroRepository.save(libro);
            autorRepository.save(autor);

        }catch (Exception e){
            System.out.println("no se encontro ese libro");
        }

    }

    // Trae los libros guardados en la BD
    private void consultarLibros() {
        libros = libroRepository.findAll();
        libros.stream().forEach(l -> {
            System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getLenguaje(),
                    l.getDescargas().toString()));
        });
    }

    // Trae todos los autores de los libros consultados en la BD
    private void consultarAutores() {
        autores = autorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Año de nacimiento: %s
                        Año de defuncion: %s
                    """.formatted(a.getAutor(),
                    a.getNacimiento().toString(),
                    a.getDefuncion().toString()));
        });
    }

    // Trae a los autores apartir de cierto año
    public void consultarAutoresPorAno()
    {
        System.out.println("Ingresa el año a partir del cual buscar:");
        var anoBusqueda = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = autorRepository.autorPorFecha(anoBusqueda);
        autores.forEach( a -> {
            System.out.println("""
                    Nombre: %s
                    Fecha de nacimiento: %s
                    Fecha de defuncion: %s
                    """.formatted(a.getAutor(),a.getNacimiento().toString(),a.getDefuncion().toString()));
        });
    }


    private void consultarLibrosLenguaje()
    {
        System.out.println("""
                ****************************************************************    
                    Selcciona el lenguaje de los libros que deseas consultar
                ****************************************************************
                1 - En (Ingles)
                2 - Es (Español)
                """);

        try {

            var opcion2 = scanner.nextInt();
            scanner.nextLine();

            switch (opcion2) {
                case 1:
                    libros = libroRepository.findByLenguaje("en");
                    break;
                case 2:
                    libros = libroRepository.findByLenguaje("es");
                    break;

                default:
                    System.out.println("Ingresa una opcion valida");
            }

            libros.stream().forEach(l -> {
                System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                        l.getAutor(),
                        l.getLenguaje(),
                        l.getDescargas().toString()));
            });

        } catch (Exception e){
            System.out.println("Ingresa un valor valido");
        }
    }
}
