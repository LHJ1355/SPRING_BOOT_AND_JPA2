package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j  //log를 위한 어노테이션
public class HomeController {

    //Logger log = LoggerFactory.getLogger(getClass());  //@Slf4j로 대체 가능
    @GetMapping("/")
    public String home(){
        log.info("home controller");
        return "home";
    }
}
