package net.coding.mart.json.developer;

import java.util.List;

/**
 * Created by liu on 16/6/21.
 */
public class ModuleVO {
    private String name;
    private List<String> function;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFunction() {
        return function;
    }

    public void setFunction(List<String> function) {
        this.function = function;
    }
}
