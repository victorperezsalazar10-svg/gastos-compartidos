package com.gastos.gastos_compartidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() { return "index"; }

    @GetMapping("/usuarios-page")
    public String usuarios() { return "usuarios"; }

    @GetMapping("/grupos-page")
    public String grupos() { return "grupos"; }

    @GetMapping("/gastos-page")
    public String gastos() { return "gastos"; }
}
