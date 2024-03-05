package com.wny.wnyflix.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    private String name;
    private String userId;
    private String password;
    private String email;
    private String phone;
    //국가
    private String country_name;

    private String country_iso_code;
    //나이
    private int age;
    //성별
    private String gender;
}
