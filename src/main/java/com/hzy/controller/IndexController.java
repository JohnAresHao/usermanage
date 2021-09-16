package com.hzy.controller;

import com.hzy.config.WebConfiguration;
import com.hzy.param.LoginParam;
import com.hzy.param.UserParam;
import com.hzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/30 22:33
 * Description: None
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        Long id= (Long) request.getSession().getAttribute(WebConfiguration.LOGIN_KEY);
        if(id==null){
            return "login";
        }else{
            return "redirect:/list";
        }
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(@Valid LoginParam loginParam, BindingResult result, ModelMap model, HttpServletRequest request) {
        return userService.loginUser(model,loginParam,result,request);
    }

    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }
    @RequestMapping("/register")
    public String register(@Valid UserParam userParam, BindingResult result, ModelMap model){
        return userService.registerUser(model,userParam,result);
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest request){
       return userService.logoutUser(request);
    }

    @RequestMapping("/verified/{id}")
    public String verified(@PathVariable("id") Long id, ModelMap model){
        return userService.verifyUser(model,id);
    }

}
