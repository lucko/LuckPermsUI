package me.lucko.luckperms.standalone.util;

import java.util.Optional;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class OptionalPropertyValueFactory<S, T> extends PropertyValueFactory<S, T> {

    public OptionalPropertyValueFactory(String property) {
        super(property);
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        ObservableValue<T> ret = super.call(param);
        if (ret instanceof ReadOnlyObjectWrapper && ((ReadOnlyObjectWrapper) ret).get() instanceof Optional) {
            Optional opt = (Optional) ((ReadOnlyObjectWrapper) ret).get();
            if (!opt.isPresent()) {
                return new SimpleObjectProperty<T>();
            }
        }
        return ret;
    }

}
