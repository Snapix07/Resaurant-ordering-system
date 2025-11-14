package com.nurasyl.restaraunt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nurasyl.restaraunt.decorator.concrete.ExtraTopping;
import com.nurasyl.restaraunt.factory.menu.MenuFactory;
import com.nurasyl.restaraunt.model.menu.food.Food;
import com.nurasyl.restaraunt.model.menu.food.Topping;
import com.nurasyl.restaraunt.types.MenuItem;
import com.nurasyl.restaraunt.types.Visit;
import com.nurasyl.restaraunt.visitor.concrete.DiscountVisitor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    private final MenuFactory menuFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer, Food> foodDataMap = new HashMap<>();
    private Map<Integer, Topping> toppingDataMap = new HashMap<>();

    public MenuService(MenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }

    public List<Map<String, Object>> getAllMenuItems() throws IOException {
        loadFoodData();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Food data : foodDataMap.values()) {
            list.add(toFoodMap(data));
        }
        return list;
    }

    public List<Map<String, Object>> getMenuItemsByCategory(String category) throws IOException {
        loadFoodData();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Food data : foodDataMap.values()) {
            if (data.getCategory().equals(category)) {
                list.add(toFoodMap(data));
            }
        }
        return list;
    }

    public List<Map<String, Object>> getToppingsByCategory(String category) throws IOException {
        loadToppingData();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Topping data : toppingDataMap.values()) {
            if (data.getCategory().equals(category)) {
                list.add(toToppingMap(data));
            }
        }
        return list;
    }

    public List<Map<String, Object>> getCustomizedMenuItems(int id, List<Integer> toppingIds) throws IOException {
        loadFoodData();
        loadToppingData();

        Food food = foodDataMap.get(id);
        List<Map<String, Object>> list = new ArrayList<>();

        if (toppingIds == null || toppingIds.isEmpty()) {
            list.add(toFoodMap(food));
            return list;
        }

        for (Integer toppingId : toppingIds) {
            Topping topping = toppingDataMap.get(toppingId);
            if (topping == null || !topping.getCategory().equals(food.getCategory())) {
                continue;
            }
            list.add(toCustomizedFood(food, topping));
        }

        return list;
    }





    private int applyDiscount(MenuItem menuItem) {
        if(!(menuItem instanceof Visit)) {
            return menuItem.getPrice();
        }
        Visit visit = (Visit) menuItem;
        DiscountVisitor visitor = new DiscountVisitor();
        visit.accept(visitor);
        return visitor.getDiscountedPrice();
    }

    private Map<String, Object> toFoodMap(Food data) {
        MenuItem menuItem = MenuFactory.createMenu(data.getCategory(),
                data.getName(),
                data.getPrice(),
                data.getDescription());

        int discountedPrice = applyDiscount(menuItem);

        Map<String, Object> map = new HashMap<>();
        map.put("id", data.getId());
        map.put("name", data.getName());
        map.put("price", data.getPrice());
        map.put("discountedPrice", discountedPrice);
        map.put("category", data.getCategory());
        map.put("description", data.getDescription());
        map.put("image", data.getImage());

        return map;
    }

    private Map<String, Object> toToppingMap(Topping data) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", data.getId());
        map.put("name", data.getName());
        map.put("category", data.getCategory());
        map.put("price", data.getPrice());
        return map;
    }

    private Map<String,Object> toCustomizedFood(Food food, Topping topping){
        MenuItem menuItem = MenuFactory.createMenu(food.getCategory(),
                food.getName(),
                food.getPrice(),
                food.getDescription());

        menuItem = new ExtraTopping(menuItem, topping.getName(), topping.getPrice());

        int discountedPrice = applyDiscount(menuItem);

        Map<String, Object> map = new HashMap<>();
        map.put("id", food.getId());
        map.put("name", menuItem.getName());
        map.put("price", menuItem.getPrice());
        map.put("discountedPrice", discountedPrice);
        map.put("category", menuItem.getCategory());
        map.put("description", menuItem.getDescription());
        map.put("image", food.getImage());
        map.put("topping", Map.of(
                "id", topping.getId(),
                "name", topping.getName(),
                "price", topping.getPrice()

        ));

        return map;
    }

    private void loadFoodData() throws IOException {
        if (!foodDataMap.isEmpty()) {
            return;
        }
        File file = new File("docs/data/food.json");
        JsonNode root = objectMapper.readTree(file);
        ArrayNode foodArray = (ArrayNode) root.get("food");

        for (JsonNode node : foodArray) {
            Food data = new Food();
            data.setId(node.get("id").asInt());
            data.setName(node.get("name").asText());
            data.setPrice(node.get("price").asInt());
            data.setCategory(node.get("category").asText());
            data.setDescription(node.get("description").asText());
            data.setImage(node.get("image").asText());

            foodDataMap.put(data.getId(), data);

        }
    }

    private void loadToppingData() throws IOException {
        if (!toppingDataMap.isEmpty()) {
            return;
        }

        File file = new File("docs/data/toppings.json");
        JsonNode root = objectMapper.readTree(file);
        ArrayNode toppingArray = (ArrayNode) root.get("toppings");

        for (JsonNode node : toppingArray) {
            Topping data = new Topping();
            data.setId(node.get("id").asInt());
            data.setName(node.get("name").asText());
            data.setCategory(node.get("category").asText());
            data.setPrice(node.get("price").asInt());

            toppingDataMap.put(data.getId(), data);
        }
    }


}
