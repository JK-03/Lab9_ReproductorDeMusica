/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9_reproductor;

/**
 *
 * @author jenniferbueso
 */
public class Nodo {
    String cancion;
    String artista;
    String genero;
    long duracion;
    String imagePath;
    Nodo siguiente;

    Nodo(String imagePath, String genero) {
        this.duracion = duracion;
        this.imagePath = imagePath;
        this.siguiente = null;
    }
    
    @Override
    public String toString() {
        return "Canción: " + cancion + "\n" +
               "Artista: " + artista + "\n" +
               "Duración: " + duracion + "\n" +
               "Género: " + genero + "\n";
    }
}
