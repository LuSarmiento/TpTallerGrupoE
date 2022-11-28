package sorteo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Jugada {
    private List<Apuesta> apuestas = new LinkedList<Apuesta>();
    private String turno;
    private String fecha;
    private int dni;

    public Jugada(int cantidadApuestas, String turno) {
        this.fecha = java.time.LocalDate.now().toString();
        this.dni = generarDni();
        this.turno = turno;

        for (int i = 0; i < cantidadApuestas; i++)
            apuestas.add(new Apuesta());
    }

    public File getArchivo() {
        final String ARCHIVO = String.format("jugada_%s_%d_%s.txt",
                this.fecha, this.dni, this.turno);

        return new File(ARCHIVO);
    }

    public void guardar() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getArchivo()));

        bw.write("Apuesta Numero Posicion\n");
        for (Apuesta apuesta : this.apuestas)
            bw.write(String.format("$%s %d %d\n", apuesta.getValor(),
                    apuesta.getNumero(), apuesta.getPosicion()));

        bw.close();
    }

    private int generarDni() {
        final int max = 50000000;
        final int min = 5000000;
        return new Random().nextInt(max - min) + min;
    }

    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    public String getTurno() {
        return turno;
    }

    public String getFecha() {
        return fecha;
    }

    public int getDni() {
        return dni;
    }
}
