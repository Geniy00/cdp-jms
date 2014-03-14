package com.epam.cdp.router.service;

import com.epam.cdp.router.util.XstreamSerializer;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class XmlSerializer {

    XstreamSerializer serializer = new XstreamSerializer();

    public <T> String serialize(T objectToSerialize) {
        return serializer.serialize(objectToSerialize);
    }

    public <T> T deserialize(String fromXML, Class<T> clazzType) {
        return deserialize(fromXML, clazzType);
    }

}
