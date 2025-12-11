package pack.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtAuthenticationManager authManager;
    public JwtGatewayFilter(JwtAuthenticationManager authManager){ this.authManager = authManager; }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.substring(7);
            try{
                Authentication auth = new UsernamePasswordAuthenticationToken(null, token);
                auth = authManager.authenticate(auth);
                String userId = (String) auth.getPrincipal();
                String role = auth.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
                var mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch(Exception e){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() { return -1; }
}

