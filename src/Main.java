import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import huffman.Huffman;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);
        Random random = new Random();

        LocalDateTime date = java.time.LocalDateTime.now();
        System.out.println("fecha "+date);

        System.out.println("Desea apostar para la mañana o para la noche\nIngrese 1 para 'DIA'\nIngrese 2 para 'NOCHE'");
        int turno = entrada.nextInt();

        List<Apuesta> apuestasList = new ArrayList<Apuesta>();
        for (int i = 0; i < 4; i++) {
            int valor = random.nextInt(1000-10)+10;
            System.out.println("valor generado: "+valor);
            int numero = random.nextInt(9999-1)+1;
            System.out.println("numero generado: "+numero);
            int posicion = random.nextInt(20-1)+1;
            System.out.println("posicion generado: "+posicion);

            Apuesta apuesta = new Apuesta(valor, numero, posicion);
            apuestasList.add(apuesta);
        }

        // Hacer while para hacer por consola

        final String nombreArchivo = "jugada_"+date+"_dni_"+turno+".txt";

        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo));

        bw.write("Nombre del archivo="+nombreArchivo);
        bw.newLine();
        bw.write("Jugada= "+turno);
        bw.newLine();
        bw.write("Fecha y Hora="+date);
        bw.newLine();
        bw.write("Dni=32541874");
        bw.newLine();
        bw.write("Apuesta Numero Posicion\n");
        for (Apuesta elemento: apuestasList) {
            bw.write("$"+elemento.getValorApuesta()+" "+elemento.getNumero()+" "+elemento.getPosicion()+"\n");
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
