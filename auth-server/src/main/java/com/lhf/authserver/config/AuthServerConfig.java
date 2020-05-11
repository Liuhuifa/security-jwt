package com.lhf.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * <p></p>
 *
 * @author zy 刘会发
 * @version 1.0
 * @since 2020/5/11
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private ClientDetailsService clientDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 授权服务器安全配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
    }

    /**
     * 授权服务客户端配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("novel")
                .secret(passwordEncoder.encode("123"))
                .autoApprove(true)
                .resourceIds("lhf")
                .scopes("all")
                .redirectUris("http://localhost:8082/index.html")
                .authorizedGrantTypes("authorization_code");
    }

    /**
     * 授权服务器节点配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(defaultTokenServices());
    }

    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices bean = new DefaultTokenServices();
        bean.setTokenStore(jwtTokenStore());
        bean.setSupportRefreshToken(true);
        bean.setAccessTokenValiditySeconds(60 * 60);
        bean.setRefreshTokenValiditySeconds(30 * 60);
        bean.setClientDetailsService(clientDetailsService);
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter()));//将token的增强方式注入进去
        bean.setTokenEnhancer(chain);
        return bean;
    }

    /**
     * token保存方式使用jwt
     * @return
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * jwt配置
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter bean = new JwtAccessTokenConverter();
        bean.setSigningKey("zjs");
        return bean;
    }
}
