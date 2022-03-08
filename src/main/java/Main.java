import java.time.Duration;
import java.time.Instant;

public class Main {
    private static final String FILE_PATH = "src/main/resources/RU-NVS.osm.bz2";

    public static void main(String[] args) {
        Parser xmlParser = new Parser();

        Instant start = Instant.now();

        xmlParser.run(FILE_PATH);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        System.out.println("Time Elapsed: " + timeElapsed + " seconds");
    }
}
