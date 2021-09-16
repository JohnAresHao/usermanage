package com.hzy.service;
/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/30 16:08
 * Description: None
 */

import com.hzy.model.User;
import com.hzy.param.LoginParam;
import com.hzy.param.PageParam;
import com.hzy.param.UserParam;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {
    //public String findAll(Model model,PageParam pageParam);
    public String findAllByCondition(Model model, PageParam pageParam, String userName, String userType);
    public Optional<User> findById(Long id);
    public String loginUser(ModelMap model, LoginParam loginParam, BindingResult result, HttpServletRequest request);
    public String registerUser(ModelMap model, UserParam userParam, BindingResult result);
    public String logoutUser(HttpServletRequest request);
    public String verifyUser(ModelMap model, Long id);
    public String insertUser(ModelMap model, UserParam userParam, BindingResult result);
    public String updateUser(ModelMap model, UserParam userParam, BindingResult result);
    public String deleteById(Long id);


}
