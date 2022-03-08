import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class StreamProcessor implements AutoCloseable {
    private final XMLStreamReader reader;

    public StreamProcessor(InputStream is) throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        var in = new BZip2CompressorInputStream(is);
        reader = factory.createXMLStreamReader(in);
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException ignored) {
            }
        }
    }

    public boolean readerHasNext() throws XMLStreamException {
        return reader.hasNext();
    }

    public int getEventNext() throws XMLStreamException {
        return reader.next();
    }

    public void findAllElementsWithAttribute(LinkedHashMap<String, Integer> items, int event,
                                             String element, String attributeName) {
        if (event == XMLEvent.START_ELEMENT && element.equals(reader.getLocalName())) {
            int attributesAmount = reader.getAttributeCount();
            for (int i = 0; i < attributesAmount; ++i) {
                if (attributeName.equals(reader.getAttributeLocalName(i))) {
                    String item = reader.getAttributeValue(i);
                    if (items.containsKey(item)) {
                        items.put(item, items.get(item) + 1);
                    } else {
                        items.put(item, 1);
                    }
                }
            }
        }
    }
}
