package com.nurasyl.restaraunt.model.menu.food;

<<<<<<< HEAD
import com.nurasyl.restaraunt.types.MenuItem;
=======
import com.nurasyl.restaraunt.factory.menu.MenuItem;
>>>>>>> 6113df0cea5b0f39c0be89b33784e1e6115e7a54
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Drink implements MenuItem {
    private String name;
    private int price;
    private String description;

    @Override
    public String getCategory() {
        return "Drinks";
    }
}
