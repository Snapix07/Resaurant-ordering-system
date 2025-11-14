package com.nurasyl.restaraunt.types;

import com.nurasyl.restaraunt.visitor.MenuItemVisitor;

public interface Visit {
    void accept(MenuItemVisitor visitor);
}
