package com.baeldung.um.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

//@Configuration
//@PropertySource({ "classpath:persistence.properties" })
//@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfigInMemory extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {// @formatter:off
        clients.inMemory().withClient("sampleClientId").authorizedGrantTypes("implicit").scopes("read", "write", "foo", "bar").autoApprove(false).accessTokenValiditySeconds(3600).redirectUris("http://localhost:8083/","http://localhost:8086/")

                .and().withClient("fooClientIdPassword").secret(passwordEncoder().encode("secret"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials").scopes("foo", "read", "write", "user").accessTokenValiditySeconds(3600)
                .autoApprove("read", "write", "user")
                // 1 hour
                .refreshTokenValiditySeconds(2592000)
                // 30 days
                .redirectUris("xxx","http://localhost:8082/um-webapp-client/user","http://localhost:8080/login/oauth2/code/custom","http://localhost:8080/ui-thymeleaf/login/oauth2/code/custom", "http://localhost:8080/authorize/oauth2/code/bael", "http://localhost:8080/login/oauth2/code/bael")

                .and().withClient("barClientIdPassword").secret(passwordEncoder().encode("secret")).authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("bar", "read", "write").accessTokenValiditySeconds(3600)
                // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days

                .and().withClient("testImplicitClientId").authorizedGrantTypes("implicit").scopes("read", "write", "foo", "bar").autoApprove(true).redirectUris("xxx");

//          .withClient("sampleClientId")
//          .authorizedGrantTypes("implicit")
//          .scopes("read", "write", "foo", "bar")
//          .autoApprove(false).accessTokenValiditySeconds(3600)
//
//          .and()
//          .withClient("fooClientIdPassword")
//          .secret("secret")
//          .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
//          .scopes("foo", "read", "write")
//          .accessTokenValiditySeconds(3600) // 1 hour
//          .refreshTokenValiditySeconds(2592000) // 30 days
//
//          .and()
//          .withClient("barClientIdPassword")
//          .secret("secret")
//          .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//          .scopes("bar", "read", "write")
//          .accessTokenValiditySeconds(3600) // 1 hour
//          .refreshTokenValiditySeconds(2592000) // 30 days
	  ;
	} // @formatter:on

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		endpoints.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
		// @formatter:on
    }

    @Bean
    public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("123");
//    final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"), "mypass".toCharArray());
//    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
    return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
