package com.laiyuezs.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用于Feign传递请求头的拦截器
 * 将全局事务id传递到分支模块
 * Created by macro on 2019/10/18.
 */
//@Configuration
@Deprecated
public class FeignXidRequestInterceptor implements RequestInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(FeignXidRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes != null) {
            final String xid = RootContext.getXID();
            if(xid !=null && xid.length()>0){
                requestTemplate.header("TX_XID",xid);
            }
            logger.debug("全局事务id ：{}" , xid);

        }
    }
}
