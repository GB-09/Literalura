package com.aluracursos.Literalura.Repository;

import com.aluracursos.Literalura.model.Autor;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository  extends JpaRepository<Autor,Long>
{
    @Query("SELECT a FROM Autor a WHERE a.nacimiento >= :anoBusqueda ORDER BY a.nacimiento ASC ")
    List<Autor> autorPorFecha(int anoBusqueda);

}

