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
 * The type Security controller.
 *
 * @author zhaofeng on 17-6-26.
 */
@RestController
public class SecurityController {

    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping("/")
    public String index(){
        return "security-oauth2 Startup Successful";
    }

    /**
     * Login model and view.
     *
     * @param clientId the client id
     * @param scopes   the scopes
     * @param model    the model
     * @return the model and view
     */
    @RequestMapping("/login")
    public ModelAndView login(String clientId, String[] scopes, Map<String, Object> model) {

        AuthorizationRequest authorizationRequest = new AuthorizationRequest(clientId,Arrays.asList(scopes));
        model.put("authorizationRequest", authorizationRequest);


//        return modelAndView;
        return null;
    }

    /**
     * Ignore string.
     *
     *  正则匹配所有 ignore0-ignore9
     *
     * @param url the url
     * @return the string
     */
    @RequestMapping("/{url:ignore[0-9]}")
    public String ignore(@PathVariable String url){
        return url;
    }

    /**
     * Get user token string.
     *
     * @return the string
     */
    @RequestMapping("/getUserToken")
    public String getUserToken(){
        return "99999999";
    }

    /**
     * Welcome string.
     *
     * @return the string
     */
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * Registered string.
     *
     * @param user the user
     * @return the string
     */
    @RequestMapping(value = "/registered",method = RequestMethod.POST)
    public String registered(User user){
        return user.toString();
    }

    /**
     * User string.
     *
     * @return the string
     */
    @RequestMapping("/user")
    public String user(){
        return "user信息......";
    }

}
