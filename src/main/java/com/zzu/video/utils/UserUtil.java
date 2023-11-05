package com.zzu.video.utils;

import com.zzu.video.vo.exception.BizException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Component
public class UserUtil {
    public int getCurrentUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("Authoritization");
        int userId = TokenUtil.verifyToken(token);
        if(userId < 0) {
            throw new BizException(401,"用户未登录");
        }
        return userId;
    }
}
