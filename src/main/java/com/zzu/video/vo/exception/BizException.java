package com.zzu.video.vo.exception;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
public class BizException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String code;

    public BizException(String code, String name){
        super(name);
        this.code = code;
    }

    public BizException(String name){
        super(name);
        code = "200";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}