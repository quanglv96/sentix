package sansan.sentix.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import sansan.sentix.Utils.Constants;
import sansan.sentix.Utils.DateTimeUtils;
import sansan.sentix.Utils.RequestUtils;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private void verifyRequest() {
        String sessionId = RequestUtils.getHeader(Constants.SESSION_ID);
        String device = RequestUtils.getHeader(Constants.USER_AGENT);
        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(device)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public String generateAccessToken() {
        verifyRequest();
        String sessionId = RequestUtils.getHeader(Constants.SESSION_ID);
        String device = RequestUtils.getHeader(Constants.USER_AGENT);
        Date now = DateTimeUtils.nowDate();
        long expirationMillis = 2 * 60 * 1000;
        return Jwts.builder()
                .setSubject(Constants.USER_NAME)
                .claim(Constants.SESSION_ID, sessionId)          // lưu session FE
                .claim("rnd", UUID.randomUUID().toString())  // giá trị ngẫu nhiên tăng độ khó đoán
                .claim("ts", now.getTime())        // timestamp
                .claim("dv", device)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS512) // thuật toán ký mạnh
                .compact();
    }


    public boolean verifyAccessToken(String token) {
        Claims claims = parseAccessToken(token); // parse token
        String sessionInToken = claims.get("sessionId", String.class); // lấy sessionId từ claim
        String sessionHeader = RequestUtils.getHeader(Constants.SESSION_ID);
        String role = claims.get("sub", String.class);
        String dv = claims.get("dv", String.class);
        String device = RequestUtils.getHeader(Constants.USER_AGENT);
        return sessionHeader != null
                && sessionHeader.equals(sessionInToken)
                && StringUtils.isNotEmpty(device)
                && device.equals(dv);

    }

    private Claims parseAccessToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // key giống với lúc generate
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("parseAccessToken error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
