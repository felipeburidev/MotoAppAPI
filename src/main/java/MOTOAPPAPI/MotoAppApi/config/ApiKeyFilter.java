package MOTOAPPAPI.MotoAppApi.config;

import MOTOAPPAPI.MotoAppApi.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Rotas públicas que não precisam de API Key
        if (path.startsWith("/usuarios/login") ||
                path.startsWith("/usuarios/post")||
                path.equals("/usuarios/TODOS") ||
                path.equals("dashboard/corridas-por-dia/{usuarioId}"))
        {
            filterChain.doFilter(request, response);
            return;
        }

        // Para rotas protegidas, verificar a API Key
        String apiKey = getApiKeyFromRequest(request);

        if (apiKey != null && apiKeyService.isValidApiKey(apiKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("API Key inválida ou ausente");
        }
    }

    private String getApiKeyFromRequest(HttpServletRequest request) {
        // Pode vir do header
        String headerKey = request.getHeader("X-API-Key");
        return headerKey;
    }
}