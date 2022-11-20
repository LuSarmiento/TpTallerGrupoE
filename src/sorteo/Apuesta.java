package sorteo;

import java.util.Random;

public class Apuesta {
    private Random random = new Random();
    private int valor;
    private int numero;
    private int posicion;

    public Apuesta() {
        this.valor = generarNumeroEntre(10, 1000);
        this.numero = generarNumeroEntre(1, 9999);
        this.posicion = generarNumeroEntre(1, 20);
    }

    public Apuesta(int valor, int num, int pos) {
        this.valor = valor;
        this.numero = num;
        this.posicion = pos;
    }

    public int getValor() {
        return valor;
    }

    public int getNumero() {
        return numero;
    }

    public int getPosicion() {
        return posicion;
    }

    private int generarNumeroEntre(int min, int max) {
        return random.nextInt(max - min) + min;
    }
}
