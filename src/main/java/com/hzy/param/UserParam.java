package com.hzy.param;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time:  2019-12-17 15:30
 * Description: 用户信息校验类
 */
public class UserParam {
    private Long id;//primary key
    @NotEmpty(message = "用户名不能为空！")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6,message = "密码长度不能少于6位！")
    private String password;
    @Email
    private String email;
    @Min(value = 14,message = "必须年满14岁！")
    @Max(value = 100,message = "年龄不能超过100岁")
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserParam{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
