package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private boolean sex;
    private java.util.Date birthday;
    private String head;
}
