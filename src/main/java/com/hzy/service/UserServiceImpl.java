package com.hzy.service;
/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/30 18:09
 * Description: None
 */

import com.hzy.config.WebConfiguration;
import com.hzy.mapper.UserMapper;
import com.hzy.model.Page;
import com.hzy.model.User;
import com.hzy.param.LoginParam;
import com.hzy.param.PageParam;
import com.hzy.param.UserParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("userService")
public class UserServiceImpl implements UserService {
    Page usersPageInfo=null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    //发送验证邮件相关
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /*@Override
    public String findAll(Model model, PageParam pageParam) {
        List<User> userList=userMapper.findAll(pageParam);
        int count=userMapper.getTotalCount();
        usersPageInfo=new Page(pageParam,count,userList);
        //查询所有用户类型
        List<String> userTypes=userMapper.findUserTypes();
        model.addAttribute("userTypes",userTypes);

        model.addAttribute("users",usersPageInfo);
        logger.info("user list: "+ userList.toString());
        return "user/list";
    }*/

    @Override
    public String findAllByCondition(Model model, PageParam pageParam, String userName, String userType) {
        //分页查询
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginLine",pageParam.getBeginLine());
        map.put("pageSize",pageParam.getPageSize());
        if(userName!=null&&!userName.equals("")){
            map.put("userName",userName);
        }
        if(userType!=null&&!userType.equals("")){
            map.put("userType",userType);
        }
        List<User> userList=userMapper.findByLikeNameAndUserType(map);
        int count=userMapper.getTotalCountCondition(map);
        usersPageInfo=new Page(pageParam,count,userList);
        //查询所有用户类型
        List<String> userTypes=userMapper.findUserTypes();
        model.addAttribute("userTypes",userTypes);
        model.addAttribute("selectType",userType);
        model.addAttribute("searchUserName",userName);

        model.addAttribute("users",usersPageInfo);
        logger.info("user list: "+ userList.toString());
        return "user/list";
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public String loginUser(ModelMap model, LoginParam loginParam, BindingResult result, HttpServletRequest request) {
        String errorMsg = "";
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = errorMsg + error.getCode() + "***" + error.getDefaultMessage() + ";";
            }
            model.addAttribute("errorMsg", errorMsg);
            return "login";
        }
        User user = userMapper.findByUserName(loginParam.getLoginName());
        if (user == null) {
            model.addAttribute("errorMsg", "用户名不存在!");
            return "login";
        } else if (!user.getPassword().equals(loginParam.getPassword())) {
            model.addAttribute("errorMsg", "密码错误！");
            return "login";
        }
        request.getSession().setAttribute(WebConfiguration.LOGIN_KEY, user.getId());
        request.getSession().setAttribute(WebConfiguration.LOGIN_USER, user);
        logger.info("login user: "+user.toString());
        return "redirect:/list";
    }

    @Override
    public String registerUser(ModelMap model, UserParam userParam, BindingResult result) {
        logger.info("register param"+ userParam.toString());
        String errorMsg="";
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for (ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"***"+error.getDefaultMessage()+"；";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "register";
        }
        User u=userMapper.findByUserNameOrEmail(userParam.getUserName(),userParam.getEmail());
        if(u!=null){
            model.addAttribute("errorMsg","用户已存在！");
            return "register";
        }
        User user=new User();
        BeanUtils.copyProperties(userParam,user);
        user.setRegTime(new Date());
        user.setState("unverified");
        user.setUserType("user");
        userMapper.insertUser(user);
        User regUser=userMapper.findByEmail(user.getEmail());//set email unique
        sendRegisterMail(regUser.getId());
        return "login";
    }

    @Override
    public String logoutUser(HttpServletRequest request) {
        Long id= (Long) request.getSession().getAttribute(WebConfiguration.LOGIN_KEY);
        if(id!=null){//若未清除过session, 先打印用户退出的日志，再执行清除session操作,必须这样判断
            logger.info("logout user: "+request.getSession().getAttribute(WebConfiguration.LOGIN_USER).toString());
            request.getSession().removeAttribute(WebConfiguration.LOGIN_KEY);
            request.getSession().removeAttribute(WebConfiguration.LOGIN_USER);
            return "login";
        }else{
            return "redirect:/toLogin";
        }

    }

    @Override
    public String verifyUser(ModelMap model, Long id) {
        User user=userMapper.findById(id).get();
        if(user!=null&&user.getState().equals("unverified")){
            user.setState("verified");
            userMapper.updateUser(user);
            model.put("userName",user.getUserName());
        }
        logger.info("user state verified ok!");
        return "verified";
    }

    @Override
    public String insertUser(ModelMap model, UserParam userParam, BindingResult result) {
        String errorMsg="";
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for (ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"***"+error.getDefaultMessage()+"；";
            }
            model.addAttribute("errorMsg",errorMsg);
            return "user/userAdd";
        }
        User u=userMapper.findByUserNameOrEmail(userParam.getUserName(),userParam.getEmail());
        if(u!=null){
            model.addAttribute("errorMsg","用户已存在！");
            return "user/userAdd";
        }
        User user=new User();
        BeanUtils.copyProperties(userParam,user);//s->t
        user.setRegTime(new Date());
        user.setState("unverified");
        user.setUserType("user");
        userMapper.insertUser(user);
        logger.info("add user: "+user.toString());
        return "redirect:/list";
    }

    @Override
    public String updateUser(ModelMap model, UserParam userParam, BindingResult result) {
        String errorMsg="";
        if(result.hasErrors()){
            List<ObjectError> list=result.getAllErrors();
            for (ObjectError error:list){
                errorMsg=errorMsg+error.getCode()+"***"+error.getDefaultMessage()+"；";
            }
            model.addAttribute("errorMsg",errorMsg);
            model.addAttribute("user", userParam);
            return "user/userEdit";
        }
        User user=new User();
        BeanUtils.copyProperties(userParam,user);
        User originalUser=userMapper.findById(user.getId()).get();
        user.setRegTime(originalUser.getRegTime());
        user.setState(originalUser.getState());
        user.setUserType(originalUser.getUserType());
        userMapper.updateUser(user);
        logger.info("update user: "+user.toString());
        return "redirect:/list";
    }

    @Override
    public String deleteById(Long id) {
        userMapper.deleteById(id);
        logger.info("delete user where id: "+id);
        return "redirect:/list";
    }

    public String sendRegisterMail(Long uid){//封装了邮件发送的内容，注册成功后调⽤即可
        Context context=new Context();
        context.setVariable("id",uid);
        String emailContent=templateEngine.process("emailTemplate",context);
        MimeMessage message=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            //helper.setTo(user.getEmail());//real register email
            helper.setTo("3207861763@qq.com");//for test
            helper.setSubject("注册验证邮件");
            helper.setText(emailContent,true);
            mailSender.send(message);
        }catch (Exception e){
            logger.error("发送注册邮件时异常！", e);
        }finally {
            return "login";
        }

    }
}
