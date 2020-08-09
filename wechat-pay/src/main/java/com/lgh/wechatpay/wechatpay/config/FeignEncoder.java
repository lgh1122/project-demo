package com.lgh.wechatpay.wechatpay.config;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class FeignEncoder implements Encoder {
    private final JAXBContextFactory jaxbContextFactory;

    public FeignEncoder(JAXBContextFactory jaxbContextFactory) {
        this.jaxbContextFactory = jaxbContextFactory;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {
        if (!(bodyType instanceof Class)) {
            throw new UnsupportedOperationException("JAXB only supports encoding raw types. Found " + bodyType);
        } else {
            try {
                Marshaller marshaller = this.jaxbContextFactory.createMarshaller((Class)bodyType);
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(object, stringWriter);
                final String s = stringWriter.toString();
                template.body(s);
            } catch (JAXBException var6) {
                throw new EncodeException(var6.toString(), var6);
            }
        }
    }
}
