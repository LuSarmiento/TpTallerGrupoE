package huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    private final String SUFIJO_DESTINO = ".compress";
    private final String SUFIJO_REFERENCIA = ".codigos";

    public Huffman(File origen) {
        this.codigos = new LinkedHashMap<Integer, String>();
        this.frecuencias = new LinkedHashMap<Integer, Integer>();
        this.origen = origen;
        this.destino = new File(origen + SUFIJO_DESTINO);
        this.referencia = new File(origen + SUFIJO_REFERENCIA);
    }

    public void comprimirArchivo() throws Exception {
        cargarFrecuencias();
        crearArbol();
        generarCodigos();
        // comprimir();
        generarArchivoComprimido(this.origen.getName(), this.destino.getName());
        guardarCodigos();
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

        d.write("Caracter,Codigo\n");
        for (Map.Entry<Integer, String> entry : codigos.entrySet()) {
            d.write(entry.getKey() + "," + entry.getValue());
            d.newLine();
        }

        d.close();
    }

    public void leerReferencia() throws Exception {
        BufferedReader d = new BufferedReader(new FileReader(referencia));
        Map<Integer, String> m = new LinkedHashMap<Integer, String>();

        String s = d.readLine();
        while ((s = d.readLine()) != null) {
            String[] ss = s.split(",");
            m.put(Integer.parseInt(ss[0]), ss[1]);
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

    public void generarArchivoComprimido(String nomArchivo, String nomArchivoDestino) {
        String strBuffer = "";
        File arch = new File(nomArchivoDestino);
        arch.delete();
        try {
            RandomAccessFile archivoOrigen = new RandomAccessFile(nomArchivo, "r");
            RandomAccessFile archivoDestino = new RandomAccessFile(nomArchivoDestino, "rw");
            char dato;
            long cont = 0;
            long tamano = archivoOrigen.length();
            while (cont < tamano) {
                archivoOrigen.seek(cont);
                dato = (char) archivoOrigen.read();
                strBuffer = strBuffer + codigos.get((int) dato);
                strBuffer = procesarbuffer(strBuffer, archivoDestino);
                cont++;
            }
            archivoOrigen.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String procesarbuffer(String strBuff, RandomAccessFile archivo) throws IOException {
        String Auxstr = strBuff, strIingByte = "";
        while (Auxstr.length() >= 8) {
            strIingByte = Auxstr.substring(0, 8);
            Auxstr = Auxstr.substring(8, Auxstr.length());
            archivo.writeByte(stringByteToByte(strIingByte));
        }
        return Auxstr;
    }

    private byte stringByteToByte(String string) {
        if (string.length() != 8)
            throw new NumberFormatException();

        int result = 0;
        int bitValue = 128;
        for (char c : string.toCharArray()) {
            if (Integer.parseInt(String.valueOf(c)) > 0)
                result += bitValue;
            bitValue /= 2;
        }

        return (byte) result;
    }

    public void descomprimirArchivo(String nomArchivo, String nomArchivoDestino) {
        File arch = new File(nomArchivoDestino);
        String codigo = "";
        arch.delete();
        Map<String, Integer> m = codigosInvertidos();
        try {
            RandomAccessFile archivoOrigen = new RandomAccessFile(nomArchivo, "r");
            RandomAccessFile archivoDestino = new RandomAccessFile(nomArchivoDestino, "rw");
            String strCharacter = "";
            long cont = 0;
            long tamano = archivoOrigen.length();
            while (cont < tamano) {
                archivoOrigen.seek(cont);
                strCharacter = strCharacter + completarByte(
                        Integer.toBinaryString((char) archivoOrigen.readByte() & 0xff));
                cont++;
            }
            for (char c : strCharacter.toCharArray()) {
                if (!m.containsKey(codigo))
                    codigo = codigo + String.valueOf(c);
                else {
                    archivoDestino.writeByte(m.get(codigo));
                    codigo = String.valueOf(c);
                }
            }
            archivoOrigen.close();
            archivoDestino.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String completarByte(String strByte) {
        String strIni = "";
        for (int i = strByte.length(); i < 8; i++) {
            strIni = strIni + "0";
        }
        return (strIni != null && strIni.length() > 0) ? strIni + strByte : strByte;
    }
}
