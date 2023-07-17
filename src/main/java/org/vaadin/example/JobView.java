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
import com.vaadin.flow.server.PWA;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.models.Job;
import java.sql.SQLException;


@Route("/job")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
public class JobView extends VerticalLayout {

    public JobView(@Autowired JobService service) throws SQLException {
        setSizeFull();

        TableMenu tableMenu = new TableMenu();

        Button addButton = new Button("Добавить запись");
        addButton.addClickListener(event -> {
                Dialog dialog = new Dialog();
                TextField textFieldName = new TextField("Name");
                TextField textFieldSalary = new TextField("Salary");
                Button  addHorButton = new Button("Добавить");
                    addHorButton.addClickListener(event1 -> {
                        String fieldNameValue = textFieldName.getValue();
                        String fieldSalaryValue = textFieldSalary.getValue();

                        if (!fieldNameValue.isEmpty() && !fieldSalaryValue.isEmpty()
                                && NumberUtils.isNumber(fieldSalaryValue)) {

                            try {
                                service.addJob(fieldNameValue, Float.parseFloat(fieldSalaryValue));
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
            dialog.add(new VerticalLayout(textFieldName,textFieldSalary, new HorizontalLayout(addHorButton, cancelButton)));
            dialog.open();
                });

        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tableMenu.addItem(addButton);

        Grid<Job> grid = new Grid<>(Job.class);

        grid.getColumns().forEach(column -> {
            column.setFlexGrow(4);
        });

        grid.setItems(service.getJobs());

        Grid.Column<Job> updateColumn = grid.addComponentColumn(item -> {
            Button updateButton = new Button("Обновить");
            updateButton.setIcon(new Icon(VaadinIcon.EDIT));
            updateButton.addClickListener(event -> {
                Dialog dialog = new Dialog();

                TextField idField = new TextField("Id");
                idField.setValue(String.valueOf(item.getId()));
                idField.setReadOnly(true);

                TextField nameField = new TextField("Name");
                nameField.setValue(item.getName());

                TextField salaryField = new TextField("Salary");
                salaryField.setValue(String.valueOf(item.getSalary()));

                Button saveButton = new Button("Сохранить");
                saveButton.addClickListener(saveEvent -> {
                    String nameFieldValue = nameField.getValue();
                    String salaryFieldValue = salaryField.getValue();

                    if (!nameFieldValue.isEmpty() && !salaryFieldValue.isEmpty()
                            && NumberUtils.isNumber(salaryFieldValue)) {

                        try {
                            service.updateJob(item.getId(),nameFieldValue, Float.parseFloat(salaryFieldValue));
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

                dialog.add(new VerticalLayout(idField, nameField, salaryField, new HorizontalLayout(saveButton, cancelButton)));
                dialog.open();
            });

            return updateButton;
        }).setHeader("Обновление")
                .setFlexGrow(1);

        Grid.Column<Job> deleteColumn = grid.addComponentColumn(item -> {
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

                TextField salaryField = new TextField("Salary");
                salaryField.setValue(String.valueOf(item.getSalary()));
                salaryField.setReadOnly(true);

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

                dialog.add(new VerticalLayout(idField, nameField, salaryField, new HorizontalLayout(deleteBut, cancelButton)));
                dialog.open();
            });

            return deleteButton;
        }).setHeader("Удаление")
                .setFlexGrow(1);

        add(tableMenu, grid);
    }
}
