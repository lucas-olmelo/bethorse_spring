package bethorse.site_spring.Services.Autenticacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import bethorse.site_spring.Services.CookieService;

@Component
public class LoginInterceptor implements HandlerInterceptor{
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        System.out.println("Chegou no preHandle");

        if(CookieService.getCookie(request, "usuarioId") != null) return true;

        response.sendRedirect("/login");
        return false;
    }
}
