package com.zzu.video.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zzu.video.vo.exception.BizException;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Component
public class TokenUtil {
    private static final String ISSUER = "签发者";

    public static String generateToken(int userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static String generateRefreshToken(int userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static int verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Integer.parseInt(userId);
        }catch (TokenExpiredException e){
            throw new BizException(403,"token过期！");
        }catch (Exception e){
            throw new BizException(401,"非法用户token！");
        }
    }
}
