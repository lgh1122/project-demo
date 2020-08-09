package com.lgh.wechatpay.wechatpay.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class SignUtil {

    private static final String SIGN_FIELD = "sign";
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";


    public static String sign(Map<String, String> param, String key) {
        String prestr = SignUtil.createLinkString(param);
        String sign = "";
        try {
            sign = byte2hex(Md5Util.encryptMD5(prestr + key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sign;
    }


    public static String getMD5(Object obj) {
        return getMD5(toString(obj), true);
    }

    /**
     * 获取MD5值
     *
     * @param obj
     * @param is32
     * @return
     */
    public static String getMD5(Object obj, boolean is32) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toString(obj).getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        if (is32) {
            return result;
        } else {
            return result.substring(8, 24);
        }
    }

    /**
     * 将任意对象转换成字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null || "".equals(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public static String sign(Object o, String key) {
        String sign = null;
        Map<String, String> params = new HashMap<>(16);
        try {
            Field[] ss = o.getClass().getDeclaredFields();
            for (Field s : ss) {
                s.setAccessible(true);
                if (s.get(o) == null || s.get(o).equals("")) {
                    continue;
                }
                if (SIGN_FIELD.equals(s.getName())) {
                    continue;
                }
                params.put(s.getName(), s.get(o).toString());
            }
            String prestr = SignUtil.createLinkString(params) + "&key=" + key;
            System.out.println("The url is : " + prestr);
            sign = getMD5(prestr).toUpperCase();
            Field signField = o.getClass().getDeclaredField(SIGN_FIELD);
            signField.setAccessible(true);
            signField.set(o, sign);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sign;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        return createLinkString(params, true);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean sort) {

        List<String> keys = new ArrayList<>(params.keySet());

        if (sort) {
            Collections.sort(keys);
        }

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }


    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sign.append(String.format("%02x", bytes[i]));
        }
        return sign.toString();
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
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
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
            throw ex;
        }

    }

    /**
     * 判断签名是否正确
     *
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        if (!data.containsKey("sign")) {
            return false;
        }
        String sign = data.get("sign");
        data.remove("sign");
        return sign(data, key).equals(sign);
    }

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;

        try {
            StringBuilder e = new StringBuilder();
            br = request.getReader();
            String line = null;

            while ((line = br.readLine()) != null) {
                e.append(line).append("\n");
            }

            line = e.toString();
            return line;
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var11) {
                }
            }

        }
    }

    private static String readData(InputStream input) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input));){

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



    public static String getReqInfo(String reqInfo, String key) {
        String result = null;
        try {
            byte[] reqInfoBytes = Base64Utils.decode(reqInfo);
            String keyString = Md5Util.MD5(key).toLowerCase();
            byte[] keyBytes = keyString.getBytes();
            result = AES.decryptData(reqInfoBytes, keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}


