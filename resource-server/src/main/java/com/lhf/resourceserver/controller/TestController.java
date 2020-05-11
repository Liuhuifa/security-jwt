package com.lhf.resourceserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author zy 刘会发
 * @version 1.0
 * @since 2020/5/11
 */
@RestController
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
