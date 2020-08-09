package com.lgh.wechatpay.wechatpay.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lgh.wechatpay.wechatpay.utils.WeChatUtil;
import feign.*;
import feign.codec.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static feign.FeignException.errorStatus;

@Slf4j
public class WeChatFeignConfig {

    private final static Logger logger = LoggerFactory.getLogger(WeChatFeignConfig.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);


    @Value("${ly.wechat.pay-key:xxxxxxxxxxx}")
    private String payKey;




    @Bean
    public RequestInterceptor wechatHeaderInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate template) {
                template.header("Content-Type", "text/xml;charset=utf-8") ;
            }
        };

    }


    @Bean
    @Scope("prototype")
    public Feign.Builder wechatFeignBuilder(RequestInterceptor wechatHeaderInterceptor) {
        return Feign.builder()
                .requestInterceptor(wechatHeaderInterceptor)
                .decoder(feignDecoder())
                .encoder(feignEncoder())
                .errorDecoder(errorDecoder())
                .contract(new feign.Contract.Default());
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                FeignException exception = errorStatus(methodKey, response);
                log.error("{}", exception);
                return exception;
            }
        };
    }


    @Bean
    public Encoder feignEncoder() {
        return new Encoder() {
            @Override
            public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
                try {
//                    logger.info("wechat request data {}"+json);
                    Map objectMap = objectMapper.convertValue(object, Map.class);
                    objectMap = WeChatUtil.generateSignMap(objectMap, payKey);
                    final String requestXml = WeChatUtil.mapToXml(objectMap);

                    template.body(requestXml);
                } catch (JAXBException var6) {
                    throw new EncodeException(var6.toString(), var6);
                } catch (Exception e) {

                }
            }
        };
    }
    @Bean
    public Decoder feignDecoder() {
        return new Decoder() {
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
                            final Map<String, String> map = WeChatUtil.generateReturnSignMap(stringStringMap, payKey);
                            String json2 = objectMapper.writeValueAsString(map);
                            var6 = objectMapper.readValue(json2, (Class) type);
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
        };
    }



}
