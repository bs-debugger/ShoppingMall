package com.xq.live.web.security;


import com.xq.live.web.exception.JwtAuthenticationEntryPoint;
import com.xq.live.web.exception.RestAccessDeniedHandler;
import com.xq.live.web.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全模块配置
 *
 * @author zhangpeng32
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private PasswordEncoder passwordEncoder;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Value("${jwt.except.path}")
    private String[] exceptPath;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                             JwtAuthenticationEntryPoint unauthorizedHandler,RestAccessDeniedHandler restAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //系统默认身份验证组件
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder);
        // 使用自定义身份验证组件，可使用MD5加密方式
        //        authenticationManagerBuilder.authenticationProvider(new CustomAuthenticationProvider(userDetailsService));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()   // 由于使用的是JWT，我们这里不需要csrf
                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler).and()//权限异常处理
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()//未授权处理
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // 基于token，所以不需要session
                .authorizeRequests()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers(exceptPath).permitAll()
                .anyRequest().authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        httpSecurity.headers().frameOptions().disable();//允许浏览器页面加载frame页面
        // 添加JWT filter(添加过滤器，是在UsernamePasswordAuthenticationFilter过滤器前面加入JwtAuthenticationTokenFilter过滤器)
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
