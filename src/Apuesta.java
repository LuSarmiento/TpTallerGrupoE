public class Apuesta {

    int valorApuesta;

    int numero;

    int posicion;

    public Apuesta(int valor, int num, int pos) {
        this.valorApuesta = valor;
        this.numero = num;
        this.posicion = pos;
    }

    public int getValorApuesta() {
        return valorApuesta;
    }

    public int getNumero() {
        return numero;
    }

    public int getPosicion() {
        return posicion;
    }
}
