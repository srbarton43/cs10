import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CompressionTester
{
    public static void main(String[] args)
    {
        Compression test = new Compression();
        test.compress("inputs/USConstitution.txt", "outputs/compressed");
        test.decompress("outputs/compressed", "outputs/USA.txt");
    }
}
