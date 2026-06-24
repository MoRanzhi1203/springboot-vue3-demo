package com.example.springbootdemobackend.interceptor;

import com.example.springbootdemobackend.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT 认证拦截器
 * 除了登录/注册/Knife4j/Druid 等白名单接口外，都需要 Authorization 请求头
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 白名单路径放行（由 WebConfig 配置，这里做兜底）
        String uri = request.getRequestURI();
        if (uri.startsWith("/doc.html") || uri.startsWith("/swagger-ui") ||
            uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-resources") ||
            uri.startsWith("/webjars") || uri.startsWith("/druid") ||
            uri.equals("/error") || uri.equals("/admin/login") || uri.equals("/admin/register")) {
            return true;
        }

        // 获取 Authorization 请求头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            sendError(response, "未登录或token已过期");
            return false;
        }

        // 校验 Token
        if (!jwtUtils.validateToken(authHeader)) {
            sendError(response, "未登录或token已过期");
            return false;
        }

        // 提取用户信息存入 request attribute，后续 Controller 可通过 request 获取
        request.setAttribute("userId", jwtUtils.getUserIdFromToken(authHeader));
        request.setAttribute("username", jwtUtils.getUsernameFromToken(authHeader));

        return true;
    }

    private void sendError(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 401);
        body.put("message", message);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
