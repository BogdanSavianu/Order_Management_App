package gui.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class RecordPropertyValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, javafx.beans.value.ObservableValue<T>> {
    private final String fieldName;

    public RecordPropertyValueFactory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public javafx.beans.value.ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        S value = param.getValue();
        try {
            java.lang.reflect.Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            T fieldValue = (T) field.get(value);
            return new ReadOnlyObjectWrapper<>(fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
