package com.wny.wnyflix.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    //이름
    private String name;

    //아이디
    private String userId;

    //비번
    private String password;

    //이메일
    private String email;

    //전화번호
    private String phone;

    //국가
    private String country_name;

    //국가 코드
    private String country_iso_code;

    //나이
    private int age;

    //성별
    private String gender;


}
