package com.nurasyl.restaraunt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nurasyl.restaraunt.factory.menu.MenuFactory;
import com.nurasyl.restaraunt.types.MenuItem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuFactory menuFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MenuService(MenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }

    public List<MenuItem> getFoods() throws IOException {
        File file = new File("src/main/resources/data/food.json");
        JsonNode root = objectMapper.readTree(file);
        ArrayNode menuArray = (ArrayNode) root.get("food");

        List<MenuItem> foods = new ArrayList<>();
        for (JsonNode node : menuArray) {
            String name = node.get("name").asText();
            long price = node.get("price").asLong();
            String category = node.get("category").asText();
            String description = node.get("description").asText();

            MenuItem food = MenuFactory.createMenu(category, name, (int) price, description);
            foods.add(food);
        }
        return foods;
    }

}
