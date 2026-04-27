package es.tickethub.tickethub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping("/new")
    public String spaRoot() {
        return "forward:/new/index.html";
    }

    @GetMapping(value = { "/new/{path:^(?!assets)[^\\.]*$}", "/new/{path:^(?!assets)[^\\.]*$}/**" })
    public String spaRoutes() {
        return "forward:/new/index.html";
    }
}
