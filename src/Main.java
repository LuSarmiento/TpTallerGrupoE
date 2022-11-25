import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import huffman.Huffman;
import sorteo.Apuesta;

public class Main {
    public static void main(String[] args) throws Exception {
        String date = java.time.LocalDateTime.now().toString()
                .split("T")[0];
        String turno = "maniana";

        List<Apuesta> apuestasList = new ArrayList<Apuesta>();
        for (int i = 0; i < 4; i++) {
            Apuesta apuesta = new Apuesta();
            apuestasList.add(apuesta);
        }

        final String nombreArchivo = "jugada_" + date + "_dni_" + turno + ".txt";

        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo));

        bw.write("Apuesta Numero Posicion\n");
        for (Apuesta elemento : apuestasList) {
            bw.write("$" + elemento.getValor() + " " + elemento.getNumero() + " " + elemento.getPosicion() + "\n");
        }

        bw.close();

        File archivo = new File(nombreArchivo);
        Huffman huffman = new Huffman(archivo);
        huffman.cargarFrecuencias();
        huffman.crearArbol();
        huffman.generarCodigos();
        String s = huffman.mensajeCodificado();
        System.out.printf("%s\n", s);
        huffman.comprimir();
        huffman.guardarCodigos();
    }
}
