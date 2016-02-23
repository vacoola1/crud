package com.vacoola;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vacoola.backend.entity.User;
import org.springframework.cache.Cache;


public class UserForm extends FormLayout {

    Button newUser = new Button("New user");

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    Button delete = new Button("Delete", this::delete);

    //TextField id = new TextField("ID");
    TextField name = new TextField("Name");
    TextField age = new TextField("Age");
    CheckBox admin = new CheckBox("Admin");
    DateField createDate = new DateField("Create date");

    User user;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<User> formFieldBindings;

    public UserForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        newUser.addClickListener(e -> edit(new User()));

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        newUser.setStyleName(ValoTheme.BUTTON_PRIMARY);
        setVisible(true);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions1 = new HorizontalLayout(newUser, cancel);
        actions1.setSpacing(true);

        HorizontalLayout actions2 = new HorizontalLayout(save, delete);
        actions2.setSpacing(true);

        createDate.setReadOnly(true);

        addComponents(actions1, name, age, admin, createDate, actions2);
    }


    public void save(Button.ClickEvent event) {

        if (user == null) {
            return;
        }
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
            getUI().userService.save(user);

            String msg = String.format("Saved '%s'.", user.toString());

            Notification.show(msg, Type.TRAY_NOTIFICATION);
            user = null;
            getUI().refreshContacts();

        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void delete(Button.ClickEvent event) {

        if (user == null) {
            return;
        }

        getUI().userService.delete(user);

        String msg = String.format("Deleted '%s'.", user.toString());

        Notification.show(msg, Type.TRAY_NOTIFICATION);
        user = null;
        getUI().refreshContacts();
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        user = null;
        getUI().userGrid.select(null);
    }

    void edit(User user) {
        this.user = user;
        if (user != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(user, this);
            name.focus();
        }
        //setVisible(user != null && user.getId() != 0);
    }

    @Override
    public MyUI getUI() {
        return (MyUI) super.getUI();
    }

}