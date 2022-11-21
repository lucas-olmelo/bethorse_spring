package bethorse.site_spring.Services.Autenticacao;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class LoginInterceptorAppConfig implements WebMvcConfigurer{
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
        .excludePathPatterns(
            "/css/**",
            "/midia/**",
            "/login",
            "/logar",
            "/cadastro",
            "/",
            "/servicos",
            "/contato"
        );
    }
}
