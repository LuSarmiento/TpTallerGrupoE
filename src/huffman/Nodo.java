package huffman;

public class Nodo implements Comparable<Nodo> {
    public char dato;
    public int frec;
    public String codigo;
    public boolean esDato;
    public Nodo izq;
    public Nodo der;

    public Nodo(char dato, int frec) {
        this.dato = dato;
        this.frec = frec;
    }

    public Nodo(char dato, int frec, boolean esDato) {
        this.dato = dato;
        this.frec = frec;
        this.esDato = esDato;
    }

    public Nodo(char dato, int frec, boolean esDato, Nodo izq, Nodo der) {
        this.dato = dato;
        this.frec = frec;
        this.esDato = esDato;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public int compareTo(Nodo n) {
        return Integer.compare(this.frec, n.frec);
    }
}
