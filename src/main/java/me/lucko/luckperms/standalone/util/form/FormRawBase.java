package me.lucko.luckperms.standalone.util.form;

import java.util.List;

import me.lucko.luckperms.standalone.view.scene.Manager;

public class FormRawBase extends FormBase {

    public FormRawBase(Manager parent, String name, List<FormItem> items, List<FormResultType> buttons) {
        super(parent, name, items, buttons);
    }

    @Override
    protected Object[] getValues() {
        Object[] ret = new Object[controls.length];
        for (int i = 0; i < controls.length; i++) {
            ret[i] = controls[i];
        }
        return ret;
    }
}
