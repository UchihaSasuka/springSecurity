package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class OAuth2ServerConfig {

    private static final String DEMO_RESOURCE_ID = "order";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    /*.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()*/
                    /*.requestMatchers().anyRequest()
                    .and()
                    .anonymous()
                    .and()*/
                    .authorizeRequests()
                 //   .antMatchers("/product/**").access("#otuth2.hasScope('select') and hasRole('ROLE_USER')")
                    .antMatchers("/order/**").authenticated();

        }

        @Configuration
        @EnableAuthorizationServer
        protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{

            @Autowired
            AuthenticationManager authenticationManager;

            @Override
            public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
                security.allowFormAuthenticationForClients();
            }

            @Override
            public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
                clients.inMemory().withClient("client_1")
                        .resourceIds(DEMO_RESOURCE_ID)
                        .authorizedGrantTypes("client_credentials", "refresh_token")
                        .scopes("select")
                        .authorities("client")
                        .secret("123456")
                        .and()
                        .withClient("client_2")
                        .resourceIds(DEMO_RESOURCE_ID)
                        .authorizedGrantTypes("password", "refresh_token")
                        .scopes("select")
                        .authorities("client")
                        .secret("123456");
            }

            @Override
            public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
                endpoints
                        .authenticationManager(authenticationManager);
            }



        }

    }
}
