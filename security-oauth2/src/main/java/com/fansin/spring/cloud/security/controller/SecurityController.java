package com.fansin.spring.cloud.security.controller;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by zhaofeng on 17-6-26.
 */
@RestController
public class SecurityController {

    @RequestMapping("/")
    public String index(){
        return "security-oauth2 Startup Successful";
    }

    @RequestMapping("/login")
    public ModelAndView login(String clientId, String[] scopes, Map<String, Object> model, HttpServletResponse response) throws IOException {
//        String template = "<html> <head></head> <body> <form id=\"loginForm\" name=\"loginForm\" action=\"/oauth/confirm_access\" method=\"post\"> <input name=\"authorizationRequest.clientId\" value=\"oauth2-client-id\" /> <label><input name=\"login\" value=\"login\" type=\"submit\" /></label> </form> </body> </html>";
//        response.setContentType("text/html;charset=utf-8");
//        PrintWriter out=response.getWriter();
//        out.println(template);
//        return new ModelAndView("/");
//        model.getModel();
//        ModelAndView modelAndView = new ModelAndView("forward:/oauth/confirm_access");
//        AuthorizationRequest authorizationRequest = new AuthorizationRequest("oauth2-client-id", Arrays.asList("read,write,trust"));
//        modelAndView.addObject("authorizationRequest",authorizationRequest);
//        modelAndView.getModelMap().addAttribute("authorizationRequest",authorizationRequest);

        AuthorizationRequest authorizationRequest = new AuthorizationRequest(clientId,Arrays.asList(scopes));
        model.put("authorizationRequest", authorizationRequest);


//        return modelAndView;
        return null;
    }

//  正则匹配所有 ignore0-ignore9
    @RequestMapping("/{url:ignore[0-9]}")
    public String ignore(@PathVariable String url){
        return url;
    }

    @RequestMapping("/getUserToken")
    public String getUserToken(){
        return "99999999";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping(value = "/registered",method = RequestMethod.POST)
    public String registered(User user){
        return user.toString();
    }

    @RequestMapping("/user")
    public String user(){
        return "user信息......";
    }

}
