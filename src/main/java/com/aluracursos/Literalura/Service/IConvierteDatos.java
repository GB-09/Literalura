package com.aluracursos.Literalura.Service;

public interface IConvierteDatos
{
    <T> T obtenerDatos(String json, Class<T> tClass);
}

