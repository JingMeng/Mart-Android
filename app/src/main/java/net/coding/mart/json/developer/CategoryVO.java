package net.coding.mart.json.developer;

import java.util.List;

/**
 * Created by liu on 16/6/21.
 */
public class CategoryVO {
    private String name;
    private List<ModuleVO> module;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModuleVO> getModule() {
        return module;
    }

    public void setModule(List<ModuleVO> module) {
        this.module = module;
    }
}
