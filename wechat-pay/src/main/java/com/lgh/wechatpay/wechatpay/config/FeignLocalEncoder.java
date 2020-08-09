package com.lgh.wechatpay.wechatpay.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lgh.wechatpay.wechatpay.utils.GsonUtil;
import com.lgh.wechatpay.wechatpay.utils.WeChatUtil;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.lang.reflect.Type;
import java.util.Map;

public class FeignLocalEncoder implements Encoder {
    private final static Logger logger = LoggerFactory.getLogger(FeignLocalEncoder.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);


    public FeignLocalEncoder() {
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {
        try {
            final String json = GsonUtil.toJson(object);
            logger.info("wechat request data {}"+json);
//                    Map<String, String> objectMap = GsonUtil.fromJson2Map(json);

            Map objectMap = objectMapper.convertValue(object, Map.class);
            objectMap = WeChatUtil.generateSignMap(objectMap, "xxxx");
            final String requestXml = WeChatUtil.mapToXml(objectMap);

            template.body(requestXml);
        } catch (JAXBException var6) {
            throw new EncodeException(var6.toString(), var6);
        } catch (Exception e) {

        }
    }
}
