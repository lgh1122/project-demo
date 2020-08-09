package com.lgh.wechatpay.wechatpay.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class WeChatUtil {

    private final static Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

    private static final String SIGN_FIELD = "sign";
    private static final String SIGN_CHECK_FIELD = "check_sign";
    /**
     * 微信app支付、扫码支付 根据请求对象生成签名，具体步骤：
     * 1.将对象转换成按字典排序的map
     * 2.将map拼装成排序字符串，并拼接key
     * 3.md5编码，并转成大写
     * @param request
     * @return
     */
    public static String generateSign(Object request,String key){
        Map<String, String> sortedMap = convertToSortedMap(request);
        sortedMap = new TreeMap<>(sortedMap);
        String sortRequest = convertToStr(sortedMap) + "key=" + key;
        return DigestUtils.md5Hex(sortRequest).toUpperCase();
    }

    /**
     * 根据请求数据 生成签名  并将签名数据放入map
     * @param dataMap
     * @param key
     * @return
     */
    public static Map<String, String> generateSignMap(Map<String, String> dataMap,String key){
        final String sign = generateSign(dataMap, key);
        dataMap.put(SIGN_FIELD,sign);
        return dataMap;
    }

    /**
     * 微信app支付、扫码支付 根据请求对象生成签名，具体步骤：
     * 1.将对象转换成按字典排序的map
     * 2.将map拼装成排序字符串，并拼接key
     * 3.md5编码，并转成大写
     * @return
     */
    public static String generateSign(Map<String, String> dataMap,String key){
        Map  sortedMap = new TreeMap<>(dataMap);
        String sortRequest = convertToStr(sortedMap) + "key=" + key;
        final String sign = DigestUtils.md5Hex(sortRequest).toUpperCase();
        return sign;
    }

    /**
     * 根据返回数据 生成签名  并将签名数据放入map
     * @return
     */
    public static Map<String, String> generateReturnSignMap(Map<String, String> dataMap,String key){
        final String sign = generateSign(dataMap, key);
        dataMap.put(SIGN_CHECK_FIELD,sign);
        return dataMap;
    }


    public static String generateOpenAppSign(Object request,String openKey){
        Map<String, String> sortedMap = convertToSortedMap(request);
        String sortRequest = convertToStr(sortedMap) + "key=" + openKey;
        return DigestUtils.md5Hex(sortRequest).toUpperCase();
    }

    /**
     * 将对象转换成排序的map
     * @param object
     * @return
     */
    private static Map<String, String> convertToSortedMap(Object object){

        final String json = GsonUtil.toJson(object);
        final Map<String, String> stringStringMap = GsonUtil.fromJson2Map(json);
        Map<String,String> sortedMap = new TreeMap<>(stringStringMap);
        sortedMap.remove(SIGN_FIELD);
        return sortedMap;
    }

    /**
     * 将排序的map转成有序字符串
     * @param sortedMap
     * @return
     */
    private static String convertToStr(Map<String, String> sortedMap){
        final String[] temp = {""};
        //去除签名字段
        sortedMap.remove(SIGN_FIELD);
        sortedMap.forEach((key, value) -> {
            if(StringUtils.isNotBlank(value)){
                temp[0] += key + "=" + value + "&";
            }
        });
        return temp[0];
    }


    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            logger.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        org.w3c.dom.Document document = WXPayXmlUtil.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

    public static String readData( Reader reader) {
        try (BufferedReader br = new BufferedReader(reader);){

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e){

        }
        return null;
    }


}
