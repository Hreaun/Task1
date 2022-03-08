import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {
    public void run(String path) {
        try (StreamProcessor processor = new StreamProcessor(Files.newInputStream(Paths.get(path)))) {
            LinkedHashMap<String, Integer> userEdits = new LinkedHashMap<>();
            LinkedHashMap<String, Integer> keys = new LinkedHashMap<>();
            while (processor.readerHasNext()) {
                int event = processor.getEventNext();
                processor.findAllElementsWithAttribute(userEdits, event, "node", "user");
                processor.findAllElementsWithAttribute(keys, event, "tag", "k");
            }
            userEdits = sortMap(userEdits);
            keys = sortMap(keys);
            printResult(userEdits, keys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, Integer> sortMap(LinkedHashMap<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
    }

    private void printResult(LinkedHashMap<String, Integer> userEdits, LinkedHashMap<String, Integer> keyEntries) {
        userEdits.forEach((user, amount) -> System.out.println(user + " — " + amount));
        System.out.println("==================");
        keyEntries.forEach((key, amount) -> System.out.println(key + " — " + amount));
    }
}
