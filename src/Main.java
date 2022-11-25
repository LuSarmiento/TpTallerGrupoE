import java.util.LinkedList;
import java.util.List;

import huffman.Huffman;
import sorteo.Jugada;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Jugada> jugadas = new LinkedList<Jugada>();
        jugadas.add(new Jugada(3, "maniana"));
        jugadas.add(new Jugada(3, "noche"));

        for (Jugada jugada : jugadas) {
            jugada.guardar();
            Huffman huffman = new Huffman(jugada.getArchivo());
            huffman.comprimirArchivo();
        }
    }
}
