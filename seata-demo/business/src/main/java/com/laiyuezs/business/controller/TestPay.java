package com.laiyuezs.business.controller;


import java.util.*;
import java.util.stream.Collectors;

public class TestPay {





    public static void main(String[] args) throws Exception {





        //final int i = kmpMethod("BBC ABCDAB ABCDABCDABDE", "ABCDABD");
//        final int i = kmpMethod("ABCDABCDADB", "BCDAD");
        final int i = kmpMethod("ABABCBACD", "ABABCD");
        //final int i = kmpMethod("ABCDABCDADB", "ABCDABCDADB");
    }


    public static int kmpMethod(String parentStr,String subStr) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        //计算出部分匹配表
        for (int i = 1; i <= subStr.length(); i++) {
            String substring = subStr.substring(0, i);
            //计算出各自的前缀和后缀，然后得出
            List<String> prefixStrList = new ArrayList<>();
            List<String> suffixStrList = new ArrayList<>();

            final int length = substring.length();
            for (int j = 0; j < length; j++) {
                final String prefix = substring.substring(0, j);
                final String suffix = substring.substring(j + 1, length);
                if (prefix != null && prefix.length() > 0) {
                    prefixStrList.add(prefix);
                }
                if (suffix != null && suffix.length() > 0) {
                    suffixStrList.add(suffix);
                }
            }
            prefixStrList.retainAll(suffixStrList);
            if (prefixStrList.size() == 0) {
                map.put(i, 0);

            } else {
                String result = prefixStrList.stream().sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList()).get(0);
                map.put(i, result.length());
            }

        }
        final char[] parentChars = parentStr.toCharArray();
        final char[] subChars = subStr.toCharArray();
        final int parentLenghth = parentChars.length;
        final int subLength = subChars.length;

        if(parentLenghth < subLength){
            return -1;
        }

        for (int i = 0, j = 0; i < parentLenghth; ) {
            final char parentChar = parentChars[i];
            final char subChar = subChars[j];
            if (!Objects.equals(parentChar, subChar)) {
                Integer integer = map.get(j);
                if (integer == null) {
                    i++;
                    integer = 0;
                }
                j = integer;
            } else {

                if (j == subLength - 1) {
                    return i;
                }
                i++;
                j++;
            }


        }


        return -1;
    }



}
