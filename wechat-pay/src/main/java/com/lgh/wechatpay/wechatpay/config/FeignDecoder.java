package com.lgh.wechatpay.wechatpay.config;

import com.lgh.wechatpay.wechatpay.utils.GsonUtil;
import com.lgh.wechatpay.wechatpay.utils.SignUtil;
import com.lgh.wechatpay.wechatpay.utils.WeChatUtil;
import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Map;

public class FeignDecoder implements Decoder {
    private final JAXBContextFactory jaxbContextFactory;

    public FeignDecoder(JAXBContextFactory jaxbContextFactory) {
        this.jaxbContextFactory = jaxbContextFactory;
    }



    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == 404) {
            return Util.emptyValueOf(type);
        } else if (response.body() == null) {
            return null;
        } else if (!(type instanceof Class)) {
            throw new UnsupportedOperationException("JAXB only supports decoding raw types. Found " + type);
        } else {
            Object var6;
            try {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
                saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                Source source = new SAXSource(saxParserFactory.newSAXParser().getXMLReader(), new InputSource(response.body().asInputStream()));
                Unmarshaller unmarshaller = this.jaxbContextFactory.createUnmarshaller((Class)type);
                var6 = unmarshaller.unmarshal(source);
            } catch (JAXBException var12) {
                throw new DecodeException(var12.toString(), var12);
            } catch (ParserConfigurationException var13) {
                throw new DecodeException(var13.toString(), var13);
            } catch (SAXException var14) {
                throw new DecodeException(var14.toString(), var14);
            } finally {
                if (response.body() != null) {
                    response.body().close();
                }

            }

            return var6;
        }
    }
}
