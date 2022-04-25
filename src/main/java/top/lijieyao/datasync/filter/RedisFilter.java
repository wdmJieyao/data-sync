package top.lijieyao.datasync.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import top.lijieyao.datasync.comment.RedisTemplateThreadLocal;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Description: Redis拦截器
 * @Author: LiJieYao
 * @Date: 2022/4/25 10:12
 */
@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@WebFilter(urlPatterns = "/*")
public class RedisFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("RedisFilter..init succeed!!!!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        RedisTemplateThreadLocal.clean();
        Filter.super.destroy();
    }
}
