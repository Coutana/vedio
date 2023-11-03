package com.zzu.video.controller.advice;

import com.zzu.video.vo.JsonResponse;
import com.zzu.video.vo.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@ResponseBody
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BizException.class})
    public JsonResponse<String> handleException(Exception e) {
        logger.info(e.getMessage());
        return new JsonResponse<>(e.getMessage());
    }
}
