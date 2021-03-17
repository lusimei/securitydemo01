package com.mieasy.securitydemo01.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);//自动创建数据库
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/unauth.html");//自定义拒绝访问页面
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/logout.html").permitAll();//自定义登出页面及接口

        http.formLogin()   //自定义自己编写的登录页面
            .loginPage("/login.html")   //登录页面设置
            .loginProcessingUrl("/user/login")  //登录访问路径
            .defaultSuccessUrl("/success.html").permitAll()   //登录成功后跳转的路径
            .and().authorizeRequests()  //表示可以定义哪些方法可以被保护，哪些不被保护
            .antMatchers("/","/web/hello","/user/login","/web/role").permitAll()  //直接访问不用登录验证
//            .antMatchers("/web/index").hasAuthority("admin,maneger")//如果当前的主体具有指定的权限
            .antMatchers("/web/index").hasAnyAuthority("admin,manager") //如果当前的主体有任何提供的角色
//            .antMatchers("/web/index").hasRole("sale")//ROLE_
                .antMatchers("/web/index").hasAnyRole("look,Set")
            .anyRequest().authenticated() //所有请求都可以访问
                .and().rememberMe().tokenRepository(persistentTokenRepository()) //记住我 及存放token的数据库
                .tokenValiditySeconds(60)  //设置token有效时间 单位秒
                .userDetailsService(userDetailsService) //用它底层操作数据库(登录时进行认证操作)
            .and().csrf().disable();  //关闭csrf防护
    }
}
