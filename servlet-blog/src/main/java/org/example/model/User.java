package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String userName;
    private String passWord;
    private String nickName;
    private boolean sex;
    private java.util.Date birthday;
    private String head;
}
