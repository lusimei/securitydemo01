package com.mieasy.securitydemo01.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mieasy.securitydemo01.entity.Users;
import com.mieasy.securitydemo01.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.List;

//@Service
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Users>  wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        Users users = usersMapper.selectOne(wrapper);
        if(users == null){
            throw new UsernameNotFoundException("用户名不存在！");
        }

        List<GrantedAuthority> list =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,manager,ROLE_sale,ROLE_look");
        return new User(users.getUsername(),new BCryptPasswordEncoder().encode(users.getPassword()),list);
    }
}
