package edu.kh.daemoim.category.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.daemoim.category.dto.CategoryDTO;
import edu.kh.daemoim.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SessionAttributes({"loginMember"})
@RequiredArgsConstructor
@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;


    @GetMapping("/category")
    public String getCategoryPage(
            @RequestParam(value = "type", defaultValue = "allCategory") String category,
            Model model) {
        model.addAttribute("selectedCategory", category);
        return "category/category"; 
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<CategoryDTO> getGroupsByCategory(
            @RequestParam(value = "type") String category,
            @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        
    	log.info("category 이름 : {}, 검색어 : {}", category, query);
        return service.getGroupsByCategory(category, query);
    }
}


