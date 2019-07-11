
package com.xq.live.service.impl;

import com.xq.live.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;

/**
 *
 * 目前没有调用，这个可以作为自定义验证规则就可以用
 * 在配置类里面注入此类
 * Created by zhangpeng32 on 2017/12/14.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 认证逻辑
        UserDetails userDetails= userDetailsService.loadUserByUsername(name);
        if(null != userDetails){
            String encodePassword = DigestUtils.md5DigestAsHex((password).getBytes());
            if(userDetails.getPassword().equals(encodePassword)){
                // 这里设置权限和角色
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add( new Authority("ROLE_ADMIN") );
                authorities.add( new Authority("AUTH_WRITE") );
                // 生成令牌
                Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
                return auth;
            }else {
                throw new BadCredentialsException("密码错误~");
            }
        }else {
            throw new UsernameNotFoundException("用户不存在~");
        }
    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
