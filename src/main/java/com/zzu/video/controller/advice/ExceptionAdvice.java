package com.zzu.video.controller.advice;

import com.zzu.video.vo.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public JsonResponse<Exception> handleException(Exception e) {
        return new JsonResponse<>(e);
    }
}
