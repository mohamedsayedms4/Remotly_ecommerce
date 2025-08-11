    package org.example.remotly_ecommerce.Filter;

    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.web.csrf.CsrfToken;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    public class CsrfCookieFilter extends OncePerRequestFilter {
        /**
         * Same contract as for {@code doFilter}, but guaranteed to be
         * just invoked once per request within a single request thread.
         * See {@link #shouldNotFilterAsyncDispatch()} for details.
         * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
         * default ServletRequest and ServletResponse ones.
         *
         * @param request
         * @param response
         * @param filterChain
         */
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            token.getToken();

            filterChain.doFilter(request, response);
        }
    }
