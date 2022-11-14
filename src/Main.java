import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final String archivo = "test.txt";

        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));

        bw.write("test\n");

        bw.close();
    }
}
