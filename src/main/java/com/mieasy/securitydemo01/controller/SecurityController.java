package com.mieasy.securitydemo01.controller;

import com.mieasy.securitydemo01.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/web")
public class SecurityController {


    @RequestMapping("/hello")
    public String hello(){
        return "hello security";
    }

    @RequestMapping("/index")
    public String index(){
        return "index page";
    }

    @Secured({"ROLE_sale","ROLE_admin"})  //设置角色  securedEnabled = true
    @RequestMapping("/role")
    public String role(){
        return "test annotation of role";
    }

//  @PreAuthorize("hasRole('sale')")
//  @PreAuthorize("hasAuthority('admin')")
//  @PreAuthorize("hasAnyRole('sale,manager')")
    @RequestMapping("/test")
    @PreAuthorize("hasAnyAuthority('admin,manager')")  //进入方法之前 验证权限角色  prePostEnabled = true
    public String test(){
        return "test PreAuthorized hasAnyAuthority";
    }

    @RequestMapping("/post")
    @PostAuthorize("hasAnyAuthority('admin,manager')")  //进入方法之后 验证权限角色  prePostEnabled = true
    public String post(){
        System.out.println("进入了post方法");
        return "post PreAuthorized hasAnyAuthority";
    }

    @RequestMapping("/filter")
//    @PostAuthorize("hasAnyAuthority('admin,manager')")
    /**
     * ！！！ 此处需要特别注意 PostFilter注解的对象名必须为  filterObject,
     * 属性名必须为对象里面有的属性(不能随意更改,不然会报错),username  符号为 ==
     */
    @PostFilter("filterObject.username == 'admin'") //对方法返回的数据进行过滤
    public List<Users> filter(){
        List<Users> list = new ArrayList<>();
        list.add(new Users(1,"admin","123456"));
        list.add(new Users(2,"test","123456"));
        list.add(new Users(3,"opp","1243"));
        System.out.println(list);
        return list;
    }

    @RequestMapping("/param")
//    @PreFilter(filterTarget="ids", value="filterObject%2==0")
//    public void delete(List<Integer> ids, List<String> usernames) {
    @PreFilter("filterObject.id%2 == 0")  //对传入方法的参数进行过滤 prePostEnabled = true
    public void filter(@RequestBody List<Users> list){
        List<Users> list1 = list;
        for (Users users : list){
            System.out.println(users);
        }
    }

}
