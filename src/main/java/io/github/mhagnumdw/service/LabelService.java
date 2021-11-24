package io.github.mhagnumdw.service;

import io.github.mhagnumdw.entity.Label;

public class LabelService extends Service<Label> {

    public Label get(String name) {
        return getBySimpleNaturalId(name);
    }

}
