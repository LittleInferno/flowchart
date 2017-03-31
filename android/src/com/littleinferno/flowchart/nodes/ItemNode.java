package com.littleinferno.flowchart.nodes;

public class ItemNode {
    private String name;
    private String category;

    public ItemNode(String name) {
        this.name = name;
        this.category = "cat";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
