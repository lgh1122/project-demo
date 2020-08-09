package com.lgh.wechatpay.wechatpay.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lgh.wechatpay.wechatpay.utils.GsonUtil;
import com.lgh.wechatpay.wechatpay.utils.WeChatUtil;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class FeignLocalDecoder implements Decoder {

    private final static Logger logger = LoggerFactory.getLogger(FeignLocalDecoder.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);



    public FeignLocalDecoder() {
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        final int status = response.status();
        final Response.Body body = response.body();

        logger.info("wechat response status {} body {}", status,body);
        if (status == 404) {
            return Util.emptyValueOf(type);
        } else if (body == null) {
            return null;
        } else if (!(type instanceof Class)) {
            throw new UnsupportedOperationException("JAXB only supports decoding raw types. Found " + type);
        } else {
            Object var6 = null;
            try {
                final String readData = WeChatUtil.readData(body.asReader());
                logger.info("wechat response data {}"+readData);
                try {
                    final Map<String, String> stringStringMap = WeChatUtil.xmlToMap(readData);
                    final Map<String, String> map = WeChatUtil.generateReturnSignMap(stringStringMap, "xxxx");
//                            final String json = GsonUtil.fromMap2Json(map);
                    String json2 = objectMapper.writeValueAsString(map);
                    var6 = objectMapper.readValue(json2, (Class) type);
//                            var6 = GsonUtil.fromJsonUnderScoreStyle(json, (Class) type);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                if (response.body() != null) {
                    response.body().close();
                }

            }

            return var6;
        }
    }
}
