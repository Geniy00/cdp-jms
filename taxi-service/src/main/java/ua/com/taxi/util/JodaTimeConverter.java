package ua.com.taxi.util;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Geniy00
 */
public class JodaTimeConverter implements Converter {
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");

    @Override
    @SuppressWarnings("unchecked")
    public boolean canConvert(final Class type) {
        return DateTime.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        DateTime dateTime = (DateTime) source;
        writer.setValue(formatter.print(dateTime));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext context) {
        return formatter.parseDateTime(reader.getValue());
    }
}
