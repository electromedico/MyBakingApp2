package com.example.alex.mybakingapp2.model;

public class Label {

    public static final String LABEL_INGREDIENTS = "Ingredients";
    public static final String LABEL_STEPS = "Steps";
    private String label;

    public Label(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
