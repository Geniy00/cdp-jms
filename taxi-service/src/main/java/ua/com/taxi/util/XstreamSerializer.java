package ua.com.taxi.util;

import com.epam.cdp.core.xml.BookingRequestMessage;
import com.thoughtworks.xstream.XStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author Geniy00
 */
public class XstreamSerializer {

    //TODO: add XML validator. example here http://eminmamedov.org/?p=269
    private XStream xStream;

    public XstreamSerializer() {
        this.xStream = new XStream();
        xStream.aliasType("BookingRequestMessage", BookingRequestMessage.class);
        xStream.registerConverter(new JodaTimeConverter());
    }

    public <T> String serialize(T objectToSerialize) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xStream.toXML(objectToSerialize, writer);
            return outputStream.toString("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public <T> T deserialize(String fromXML, Class<T> clazzType) {
        try {
            return clazzType.cast(xStream.fromXML(fromXML));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
