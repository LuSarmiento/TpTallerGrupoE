package huffman;

import java.io.File;

public class Main {
	public static void main(String[] args) throws Exception {
		// String loc = "C:/_uni/taed2/clase3/c.txt";
		File f = new File(".", "a.txt");
		// if (!f.exists())
		// f.createNewFile();

		Huffman h = new Huffman(f);
		// h.cargarFrecuencias();
		// h.crearArbol();
		// h.generarCodigos();
		// String s = h.mensajeCodificado();
		// System.out.printf("%s\n", s);
		// h.comprimir();
		// h.guardarCodigos();
		h.leerReferencia();
		h.descomprimir();

		// h.mostrarEnOrden();
		// h.mostrarTablaCompleta();

	}
}
