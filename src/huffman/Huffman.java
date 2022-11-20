package huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {
	public Nodo raiz;
	public Map<Integer, String> codigos;
	public Map<Integer, Integer> frecuencias;
	private long nchars;
	public File origen;
	public File destino;
	public File referencia;
	private final String SUFIJO_DESTINO = ".cmp";
	private final String SUFIJO_REFERENCIA = ".ref";

	public Huffman(File origen) {
		this.codigos = new LinkedHashMap<Integer, String>();
		this.frecuencias = new LinkedHashMap<Integer, Integer>();
		this.origen = origen;
		this.destino = new File(origen + SUFIJO_DESTINO);
		this.referencia = new File(origen + SUFIJO_REFERENCIA);
	}

	public void cargarFrecuencias() throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(origen));

		int n;
		while ((n = f.read()) != -1) {
			if (!frecuencias.containsKey(n)) {
				frecuencias.put(n, 1);
				continue;
			}

			frecuencias.put(n, frecuencias.get(n) + 1);
		}

		f.close();
	}

	public void comprimir() throws Exception {
		BufferedWriter d = new BufferedWriter(new FileWriter(destino));
		String cmp = mensajeCodificado();

		while (cmp.length() >= 8) {
			String b = cmp.substring(0, 8);
			d.write(Integer.parseInt(b, 2));
			cmp = cmp.substring(8, cmp.length());
		}

		d.close();
	}

	public String mensajeCodificado() throws Exception {
		BufferedReader o = new BufferedReader(new FileReader(origen));
		StringBuilder sb = new StringBuilder();

		int c;
		nchars = 0;
		while ((c = o.read()) != -1) {
			sb.append(codigos.get(c));
			nchars++;
		}

		while (sb.length() % 8 != 0)
			sb.append('0');

		o.close();
		return sb.toString();
	}

	public void guardarCodigos() throws Exception {
		BufferedWriter d = new BufferedWriter(new FileWriter(referencia));

		d.write("Caracter,Binario");
		d.newLine();
		for (Map.Entry<Integer, String> entry: codigos.entrySet()) {
			d.write(entry.getKey()+","+entry.getValue());
			d.newLine();
		}

		d.close();
	}

	public void leerReferencia() throws Exception {
		BufferedReader d = new BufferedReader(new FileReader(referencia));
		Map<Integer, String> m = new LinkedHashMap<Integer, String>();

		nchars = Integer.parseInt(d.readLine());
		for (String t : d.readLine().split(",")) {
			if (t.substring(0, 1).equals("{"))
				t = t.substring(1, t.length());
			if (t.substring(t.length() - 1, t.length()).equals("}"))
				t = t.substring(0, t.length() - 1);

			String[] ss = t.split("=");
			m.put(Integer.parseInt(ss[0].trim()), ss[1].trim());
		}

		codigos = m;
		d.close();
	}

	private Map<String, Integer> codigosInvertidos() {
		Map<String, Integer> m = new LinkedHashMap<String, Integer>();

		for (Integer i : codigos.keySet())
			m.put(codigos.get(i), i);

		return m;
	}

	public void descomprimir() throws Exception {
		BufferedReader d = new BufferedReader(new FileReader(destino));
		Map<String, Integer> m = codigosInvertidos();

		StringBuilder sb = new StringBuilder();

		int c;
		while ((c = d.read()) != -1)
			sb.append(String.format("%8s",
					Integer.toBinaryString(c))
					.replace(' ', '0'));

		// System.out.println(sb.toString());
		// TODO: improve code
		d.close();
		BufferedWriter o = new BufferedWriter(new FileWriter(origen));

		String curr;
		int mchars = 9, chars = 0, p = 0, f = 1;
		while (f <= sb.length() && chars < mchars) {
			curr = sb.substring(p, f);

			if (m.get(curr) == null) {
				f++;
				continue;
			}

			// System.out.printf("%c", m.get(curr));
			o.write(m.get(curr));
			chars++;
			p = f;
		}
		o.close();
	}

	public void crearArbol() {
		procesarLista(crearLista());
	}

	public List<Nodo> crearLista() {
		List<Nodo> listaArboles = new LinkedList<Nodo>();

		for (int i : frecuencias.keySet()) {
			if (i <= 0)
				continue;

			Nodo n = new Nodo((char) i, frecuencias.get(i), true);
			listaArboles.add(n);
		}

		return listaArboles;
	}

	public void procesarLista(List<Nodo> nodos) {
		Queue<Nodo> cola = new PriorityQueue<Nodo>(nodos);

		while (cola.size() != 1) {
			Nodo n = new Nodo('\0', 0, false);
			n.izq = cola.poll();
			n.der = cola.poll();
			n.frec = n.izq.frec + n.der.frec;
			cola.add(n);
		}

		this.raiz = cola.poll();
	}

	public void generarCodigos() {
		generarCodigosRec(raiz, "");
	}

	private void generarCodigosRec(Nodo n, String c) {
		if (n == null)
			return;

		generarCodigosRec(n.izq, c + '0');
		generarCodigosRec(n.der, c + '1');

		if (n.dato == '\0')
			return;
		codigos.put((int) n.dato, c);
	}

	public void mostrarTablaCompleta() {
		List<Nodo> l = crearLista();
		System.out.printf("char	hex	codigo	frecuencia\n");
		for (Nodo n : l)
			System.out.printf("%c	%02x	%s	%d\n",
					Character.isWhitespace(n.dato) ? ' ' : n.dato,
					(int) n.dato, codigos.get((int) n.dato),
					n.frec);
	}

	public void mostrarEnOrden() {
		printPreOrder(raiz, 1);
	}

	private void printPreOrder(Nodo n, int spaces) {
		if (n == null)
			return;

		printPreOrder(n.der, spaces + 1);

		String fmt;
		if (Character.isWhitespace(n.dato))
			fmt = "%" + Integer.toString(spaces) + "s(%s)\n";
		else
			fmt = "%" + Integer.toString(spaces) + "s(%s)\n";
		String str = String.format(fmt,
				Character.isWhitespace(n.dato) ? "~" : n.dato,
				codigos.get((int) n.dato)).replace(' ', '	');
		System.out.print(str);

		printPreOrder(n.izq, spaces + 1);
	}
}
