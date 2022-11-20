import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*final String archivo = "test.txt";

        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));

        bw.write("test\n");

        bw.close();*/
        Scanner entrada = new Scanner(System.in);
        Random random = new Random();

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
    }
}
