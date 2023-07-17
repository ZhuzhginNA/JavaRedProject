package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.models.Department;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@Route("/department")
public class DepartmentView extends VerticalLayout {

    public DepartmentView(@Autowired DepartmentService service) throws SQLException {
        setSizeFull();

        TableMenu tableMenu = new TableMenu();

        Button addButton = new Button("Добавить запись");
        addButton.addClickListener(event -> {
            Dialog dialog = new Dialog();
            TextField textFieldName = new TextField("Name");
            TextField textFieldPhone = new TextField("Phone");
            TextField textFieldEmail = new TextField("Email");
            Button  addHorButton = new Button("Добавить");
            addHorButton.addClickListener(event1 -> {
                String fieldNameValue = textFieldName.getValue();
                String fieldPhoneValue = textFieldPhone.getValue();
                String fieldEmailValue = textFieldEmail.getValue();

                if (!fieldNameValue.isEmpty() && !fieldPhoneValue.isEmpty()
                        && !fieldEmailValue.isEmpty()) {
                    try {
                        service.addDepartment(fieldNameValue,
                                fieldEmailValue,
                                fieldPhoneValue);
                        UI.getCurrent().getPage().reload();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Notification.show("Не заполнены поля");
                }
            });
            Button cancelButton = new Button("Отмена");
            cancelButton.addClickListener(cancelEvent -> {
                dialog.close();
            });
            dialog.add(new VerticalLayout(textFieldName,textFieldEmail,textFieldPhone,
                    new HorizontalLayout(addHorButton, cancelButton)));
            dialog.open();
        });


        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tableMenu.addItem(addButton);

        Grid<Department> grid = new Grid<>(Department.class);

        ArrayList<Grid.Column<Department>> cols = new ArrayList<>();
        grid.getColumns().forEach(col -> {
            if (Objects.equals(col.getKey(), "id"))
                cols.add(0, col);
            else
                cols.add(col);
        });

        grid.setColumnOrder(cols);

        grid.setItems(service.getDepartments());

        Grid.Column<Department> updateColumn = grid.addComponentColumn(item -> {
                    Button updateButton = new Button("Обновить");
                    updateButton.setIcon(new Icon(VaadinIcon.EDIT));
                    updateButton.addClickListener(event -> {
                        Dialog dialog = new Dialog();

                        TextField idField = new TextField("Id");
                        idField.setValue(String.valueOf(item.getId()));
                        idField.setReadOnly(true);

                        TextField nameField = new TextField("Name");
                        nameField.setValue(item.getName());

                        TextField emailField = new TextField("Email");
                        emailField.setValue(item.getEmail());

                        TextField phoneField = new TextField("Phone");
                        phoneField.setValue(item.getPhone());

                        Button saveButton = new Button("Сохранить");
                        saveButton.addClickListener(saveEvent -> {
                            String nameFieldValue = nameField.getValue();
                            String emailFieldValue = emailField.getValue();
                            String phoneFieldValue = phoneField.getValue();

                            if (!nameFieldValue.isEmpty() && !emailFieldValue.isEmpty()
                                    && !phoneFieldValue.isEmpty()) {
                                try {
                                    service.updateDepartment(item.getId(),nameFieldValue,
                                            emailFieldValue, phoneFieldValue);
                                    UI.getCurrent().getPage().reload();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                            } else {
                                Notification.show("Не заполнены поля");
                            }
                        });

                        Button cancelButton = new Button("Отмена");
                        cancelButton.addClickListener(cancelEvent -> {
                            dialog.close();
                        });

                        dialog.add(new VerticalLayout(idField, nameField, emailField,
                                phoneField, new HorizontalLayout(saveButton, cancelButton)));
                        dialog.open();
                    });

                    return updateButton;
                }).setHeader("Обновление")
                .setFlexGrow(1);

        Grid.Column<Department> deleteColumn = grid.addComponentColumn(item -> {
                    Button deleteButton = new Button("Удалить");
                    deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
                    deleteButton.addClickListener(event -> {
                        Dialog dialog = new Dialog();

                        TextField idField = new TextField("Id");
                        idField.setValue(String.valueOf(item.getId()));
                        idField.setReadOnly(true);

                        TextField nameField = new TextField("Name");
                        nameField.setValue(item.getName());
                        nameField.setReadOnly(true);

                        TextField emailField = new TextField("Email");
                        emailField.setValue(item.getEmail());
                        emailField.setReadOnly(true);

                        TextField phoneField = new TextField("Phone");
                        phoneField.setValue(item.getPhone());
                        phoneField.setReadOnly(true);

                        Button deleteBut = new Button("Удалить");
                        deleteBut.addClickListener(saveEvent -> {
                            try {
                                service.deleteJob(item.getId());
                                UI.getCurrent().getPage().reload();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        Button cancelButton = new Button("Отмена");
                        cancelButton.addClickListener(cancelEvent -> {
                            dialog.close();
                        });

                        dialog.add(new VerticalLayout(idField, nameField, emailField,
                                phoneField, new HorizontalLayout(deleteBut, cancelButton)));
                        dialog.open();
                    });

                    return deleteButton;
                }).setHeader("Удаление")
                .setFlexGrow(1);

        add(tableMenu, grid);
    }
}
