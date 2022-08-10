package dislinkt.authservice.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import dislinkt.authservice.services.impl.CustomUserDetailsService;
import dislinkt.authservice.services.impl.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;
	private JwtTokenProvider tokenProvider;
	private CustomUserDetailsService userDetailsService;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig, JwtTokenProvider tokenProvider,
			CustomUserDetailsService userDetailsService) {

		this.jwtConfig = jwtConfig;
		this.tokenProvider = tokenProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String header = request.getHeader(jwtConfig.getHeader());

		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(jwtConfig.getPrefix(), "");

		if (tokenProvider.validateToken(token)) {
			Claims claims = tokenProvider.getClaimsFromJWT(token);
			String username = claims.getSubject();

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			SecurityContextHolder.clearContext();
		}

		chain.doFilter(request, response);
	}

}