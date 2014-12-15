package com.epam.cdp.router.service;

import com.epam.cdp.router.util.XstreamSerializer;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class XmlSerializer {

    final XstreamSerializer serializer = new XstreamSerializer();

    public <T> String serialize(final T objectToSerialize) {
        return serializer.serialize(objectToSerialize);
    }

}
