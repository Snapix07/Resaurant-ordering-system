package com.nurasyl.restaraunt.controllers;


import com.nurasyl.restaraunt.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Map<String, Object>> getAllMenuItems() throws IOException {
        return menuService.getAllMenuItems();
    }

    @GetMapping("/category/{category}")
    public List<Map<String, Object>> getMenuItemsByCategory(@PathVariable("category") String category) throws IOException {
        return menuService.getMenuItemsByCategory(category);
    }

    @GetMapping("/toppings/{category}")
    public List<Map<String, Object>> getToppingsByCategory(@PathVariable("category") String category) throws IOException {
        return menuService.getToppingsByCategory(category);
    }

    @PostMapping("/item/{id}/customize/{toppingId}")
    public List<Map<String, Object>> getCustomizedMenuItems(@PathVariable("id") int id,
                                                            @PathVariable("toppingId") int toppingId,
                                                            @RequestBody List<Integer> toppingIds) throws IOException {
        return menuService.getCustomizedMenuItems(id, toppingIds);
    }
}
