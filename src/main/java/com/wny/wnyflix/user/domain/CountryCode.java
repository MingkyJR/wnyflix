package com.wny.wnyflix.user.domain;

import java.util.HashMap;
import java.util.Map;

public class CountryCode {


    public String getCountryCode(String country_name) {
    Map<String, String> countryCode = new HashMap<>();
        countryCode.put("네덜란드", "NL");
        countryCode.put("노르웨이", "NO");
        countryCode.put("대한민국", "KR");
        countryCode.put("독일", "DE");
        countryCode.put("미국", "US");
        countryCode.put("멕시코", "MX");
        countryCode.put("벨기에", "BE");
        countryCode.put("브라질", "BR");
        countryCode.put("베트남", "VN");
        countryCode.put("사우디아라비아", "SA");
        countryCode.put("싱가포르", "SG");
        countryCode.put("스페인", "ES");
        countryCode.put("스웨덴", "SE");
        countryCode.put("스위스", "CH");
        countryCode.put("아랍에미레이트", "AE");
        countryCode.put("인도", "IN");
        countryCode.put("영국", "GB");
        countryCode.put("일본", "JP");
        countryCode.put("이탈리아", "IT");
        countryCode.put("중국", "CN");
        countryCode.put("캐나다", "CA");
        countryCode.put("프랑스", "FR");
        countryCode.put("터키", "TR");
        countryCode.put("폴란드", "PL");
        countryCode.put("포르투갈", "PT");
        countryCode.put("호주", "AU");

        return countryCode.get(country_name);
    }

}
