package com.fansin.spring.cloud.swagger2.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * 这里有很多以@Api*开头的注释，详细查看源码
 *
 * @author fansin
 * @version 1.0
 * @date 18-3-20 上午10:58
 */
@Api(description = "使用swagger的controller示例类")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {


    static Map<String, User> users = new ConcurrentHashMap();

    @ApiOperation(value="获取Tomcat用户列表", notes="使用tomcat用户")
    @RequestMapping(value={""}, method=RequestMethod.GET)
    public List<User> getUserList() {
        List<User> r = new ArrayList<>(users.values());
        return r;
    }

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value="", method=RequestMethod.POST)
    public String postUser(@RequestBody User user) {
        users.put(user.getUsername(), user);
        return "success";
    }

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "username", value = "用户名称", required = true, dataType = "String")
    @RequestMapping(value="/{username}", method=RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        return users.get(username);
    }

    @ApiOperation(value="更新用户详细信息", notes="根据url的用户名来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @RequestMapping(value="/{username}", method=RequestMethod.PUT)
    public String putUser(@PathVariable String username, @RequestBody User user) {
        User u = users.get(username);
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setFullName(user.getFullName());
        users.put(username, u);
        return "success";
    }

    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
    @ApiImplicitParam(name = "username", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value="/{username}", method=RequestMethod.DELETE)
    public String deleteUser(@PathVariable String username) {
        users.remove(username);
        return "success";
    }

}
