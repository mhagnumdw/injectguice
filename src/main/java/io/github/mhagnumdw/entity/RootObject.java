package io.github.mhagnumdw.entity;

import java.io.Serializable;

public abstract class RootObject implements Serializable {

    private transient boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
