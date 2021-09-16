package com.hzy.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SessionFilter implements Filter {
    protected Logger log = LoggerFactory.getLogger(SessionFilter.class);
    // 不登陆也可以访问的资源
    private static Set<String> GreenUrlSet = new HashSet<String>();

    public void doFilter(ServletRequest srequest, ServletResponse sresponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) srequest;
        String uri = request.getRequestURI();
        sresponse.setCharacterEncoding("UTF-8");//设置响应编码格式
        sresponse.setContentType("text/html;charset=UTF-8");//设置响应编码格式
        if (uri.endsWith(".js")
                || uri.endsWith(".css")
                || uri.endsWith(".jpg")
                || uri.endsWith(".gif")
                || uri.endsWith(".png")
                || uri.endsWith(".ico")) {//静态资源放行
            log.debug("security filter, pass, " + request.getRequestURI());
            filterChain.doFilter(srequest, sresponse);
            return;
        }

        System.out.println("request uri is : "+uri);//for tests
        //不处理指定的action, jsp
        if (GreenUrlSet.contains(uri) || uri.contains("/verified/")) {//判断如果在⽩名单内，直接跳过
            log.debug("security filter, pass, " + request.getRequestURI());
            filterChain.doFilter(srequest, sresponse);
            return;
        }
//        Long id=(Long) request.getSession().getAttribute(WebConfiguration.LOGIN_KEY);
//        if(id==null){//判断 Session 中是否存在⽤户 ID，如果存在表明⽤户已经登录，如果不存在跳转到⽤户登录⻚⾯
//        	String html = "<script type=\"text/javascript\">window.location.href=\"/\"</script>";//这里注掉，因为一直循环请求页面，直接放行后面有判断
//            sresponse.getWriter().write(html);
//        }else {
//            filterChain.doFilter(srequest, sresponse);
//        }
        filterChain.doFilter(srequest, sresponse);
    }

    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {//不需要 Session 验证的 URL
        GreenUrlSet.add("/toRegister");
        GreenUrlSet.add("/toLogin");
        GreenUrlSet.add("/login");
        GreenUrlSet.add("/loginOut");
        GreenUrlSet.add("/register");
        GreenUrlSet.add("/verified");
    }
}
