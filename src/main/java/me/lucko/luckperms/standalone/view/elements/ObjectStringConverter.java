package me.lucko.luckperms.standalone.view.elements;

import java.util.Collection;

import javafx.util.StringConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ObjectStringConverter<T> extends StringConverter<T> {

    private Collection<T> list;

    @Override
    public T fromString(String string) {
        for (T obj : list) {
            if (toString(obj).equals(string)) {
                return obj;
            }
        }
        return null;
    }

}
