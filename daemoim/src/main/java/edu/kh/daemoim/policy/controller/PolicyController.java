package edu.kh.daemoim.policy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/policy")
public class PolicyController {

    @GetMapping
    public String policyPage() {
        return "policy/policy";
    }
}
