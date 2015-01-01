package ua.com.taxi.util;

import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.core.xml.BookingRequestMessage;
import com.thoughtworks.xstream.XStream;
import ua.com.taxi.entity.ClientDetails;

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
    private final XStream xStream = new XStream();

    public XstreamSerializer() {
        xStream.aliasType("BookingRequestMessage", BookingRequestMessage.class);
        xStream.aliasType("Customer", ClientDetails.class);
        xStream.registerConverter(new JodaTimeConverter());
    }

    public <T> String serialize(final T objectToSerialize) throws TsException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final Writer writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xStream.toXML(objectToSerialize, writer);
            return outputStream.toString("UTF-8");
        } catch (final IOException ex) {
            throw new TsException(ex, TsException.Reason.SERIALIZATION_FAILURE,
                    objectToSerialize.getClass().getSimpleName());
        }
    }

    public <T> T deserialize(final String fromXML, final Class<T> clazzType) throws TsException {
        try {
            return clazzType.cast(xStream.fromXML(fromXML));
        } catch (final Exception ex) {
            throw new TsException(ex, TsException.Reason.DESERIALIZATION_FAILURE, fromXML, clazzType.getSimpleName());
        }

    }
}
