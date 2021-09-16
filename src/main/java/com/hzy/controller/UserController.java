package com.hzy.controller;

import com.hzy.config.WebConfiguration;
import com.hzy.model.User;
import com.hzy.param.PageParam;
import com.hzy.param.UserParam;
import com.hzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/30 16:06
 * Description: None
 */
@Controller
@EnableCaching
public class UserController {
    //refer to: https://blog.csdn.net/m0_37034934/article/details/82912665
    final static PageParam pageParam = new PageParam();//final static使缓存生效

    @Autowired
    private UserService userService;

    @ModelAttribute
    public String isLogin(HttpServletRequest request) throws Exception {//通过session判断是否登录的状态，防止鼠标回退保持登录的状态
        if (request.getSession().getAttribute(WebConfiguration.LOGIN_USER) == null) {
            return "login";
        } else {
            return "redirect:/list";
        }
    }

    /*@RequestMapping("/list")
    @Cacheable(value="user_mng_list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                       @RequestParam(value = "size", defaultValue = "6") Integer size) {

        pageParam.setCurrentPage(page);
        pageParam.setPageSize(size);
        pageParam.setBeginLine((pageParam.getCurrentPage())*6);//因为当前页从0页开始
        return userService.findAll(model, pageParam);
    }*/

    //根据条件查询用户列表
    //查询列表使用缓存，为了性能；增删改触发清除所有缓存，为了数据的一致性
    @RequestMapping("/list")
    @Cacheable(value="user_mng_list")
    public String search(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "6") Integer size,
                        String userName,String userType){
        pageParam.setCurrentPage(page);
        pageParam.setPageSize(size);
        pageParam.setBeginLine((pageParam.getCurrentPage())*6);
        return userService.findAllByCondition(model, pageParam,userName,userType);

    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "user/userAdd";
    }

    @RequestMapping("/add")
    @CacheEvict(value="user_mng_list",allEntries=true)
    public String add(@Valid UserParam userParam, BindingResult result, ModelMap model) {
        return userService.insertUser(model, userParam, result);
    }

    @RequestMapping("/toEdit")
    public String toEdit(Model model,Long id){
        User user=userService.findById(id).get();
        model.addAttribute("user",user);
        return "user/userEdit";
    }
    @RequestMapping("/edit")
    @CacheEvict(value="user_mng_list",allEntries=true)
    public String edit(@Valid UserParam userParam,BindingResult result,ModelMap model){
        return userService.updateUser(model,userParam,result);
    }

    @RequestMapping("/delete")
    @CacheEvict(value="user_mng_list",allEntries=true)
    public String delete(Long id){
        return userService.deleteById(id);
    }

}
