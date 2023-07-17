package org.vaadin.example;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import org.vaadin.example.models.Employee;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@Route("/employee")
public class EmployeeView extends VerticalLayout {

    public EmployeeView(@Autowired EmployeeService service, @Autowired JobService jobService,
                        @Autowired DepartmentService departmentService,
                        @Autowired EmpDepService empDepService) throws SQLException {
        setSizeFull();

        TableMenu tableMenu = new TableMenu();

        Button addButton = new Button("Добавить запись");
        addButton.addClickListener(event -> {
            Dialog dialog = new Dialog();
            TextField textFieldName = new TextField("Name");
            TextField textFieldPhone = new TextField("Phone");
            TextField textFieldEmail = new TextField("Email");

            ComboBox<String> comboBox = new ComboBox<>("Job");

            try {
                comboBox.setItems(jobService.getJobName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            VerticalLayout layout = new VerticalLayout();

            ArrayList<ComboBox<String>> comboBoxes = new ArrayList<>();

            Button addDep = new Button("Добавить Department");
            addDep.getElement().getStyle().set("margin-right", "0");
            addDep.addClickListener(eventCombo -> {
                ComboBox<String> depBox = new ComboBox<>("Department");
                comboBoxes.add(depBox);
                depBox.getElement().getStyle().set("margin-right", "0");

                try {
                    depBox.setItems(departmentService.getDepName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Button removeButton = new Button("Убрать Department");
                removeButton.addClickListener(removeEvent -> {
                    layout.remove(depBox);
                    comboBoxes.remove(depBox);
                    layout.remove(removeButton);
                });
                layout.add(depBox, removeButton);
            });

            layout.setPadding(false);
            layout.add(addDep);

            Button  addHorButton = new Button("Добавить");
            addHorButton.addClickListener(event1 -> {
                String fieldNameValue = textFieldName.getValue();
                String fieldPhoneValue = textFieldPhone.getValue();
                String fieldEmailValue = textFieldEmail.getValue();
                String comboBoxValue = comboBox.getValue();

                boolean b = comboBoxes.stream().noneMatch(ComboBox::isEmpty);

                if (!fieldNameValue.isEmpty() && !fieldPhoneValue.isEmpty()
                        && !fieldEmailValue.isEmpty() && !comboBox.isEmpty() && b) {

                    ArrayList<String> depName = new ArrayList<>();
                    comboBoxes.forEach(x->{
                        depName.add(x.getValue());
                    });
                    try {
                        int id = service.addEmployee(fieldNameValue, fieldPhoneValue, fieldEmailValue);
                        empDepService.addDepEmp(id,comboBoxValue,depName);
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

            dialog.setHeight("1000px");

            dialog.add(new VerticalLayout(new HorizontalLayout(textFieldName,textFieldPhone), new HorizontalLayout(textFieldEmail,
                    comboBox), layout, new HorizontalLayout(addHorButton, cancelButton)));
            dialog.open();
        });

        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tableMenu.addItem(addButton);

        Grid<Employee> grid = new Grid<>(Employee.class);
        ArrayList<Grid.Column<Employee>> cols = new ArrayList<>();
        grid.getColumns().forEach(col -> {
            if (Objects.equals(col.getKey(), "id"))
                cols.add(0, col);
            else
                cols.add(col);
        });

        grid.setColumnOrder(cols);

        grid.setItems(service.getEmployee());

        Grid.Column<Employee> updateColumn = grid.addComponentColumn(item -> {
            Button updateButton = new Button("Обновить");
            updateButton.setIcon(new Icon(VaadinIcon.EDIT));
            updateButton.addClickListener(event -> {
                Dialog dialog = new Dialog();

                TextField idField = new TextField("Id");
                idField.setValue(String.valueOf(item.getId()));
                idField.setReadOnly(true);

                TextField nameField = new TextField("Name");
                nameField.setValue(item.getName());

                TextField phoneField = new TextField("Phone");
                phoneField.setValue(item.getPhone());

                TextField emailField = new TextField("Email");
                emailField.setValue(item.getEmail());

                ComboBox<String> comboBox = new ComboBox<>("Job");

                try {
                    comboBox.setItems(jobService.getJobName());
                    comboBox.setValue(empDepService.findJobEmp(item.getId()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                VerticalLayout layout = new VerticalLayout();
                ArrayList<ComboBox<String>> comboBoxes = new ArrayList<>();

                Button addDep = new Button("Добавить Department");
                addDep.getElement().getStyle().set("margin-right", "0");
                addDep.addClickListener(eventCombo -> {
                    ComboBox<String> depBox = new ComboBox<>("Department");
                    comboBoxes.add(depBox);
                    depBox.getElement().getStyle().set("margin-right", "0");

                    try {
                        depBox.setItems(departmentService.getDepName());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Button removeButton = new Button("Убрать Department");
                    removeButton.addClickListener(removeEvent -> {
                        layout.remove(depBox);
                        comboBoxes.remove(depBox);
                        layout.remove(removeButton);
                    });
                    layout.add(depBox, removeButton);
                });

                layout.add(addDep);

                try {
                    ArrayList<String> depNames = empDepService.findDep(item.getId());
                    depNames.forEach(x ->{
                        ComboBox<String> depBox = new ComboBox<>("Department");
                        comboBoxes.add(depBox);
                        depBox.getElement().getStyle().set("margin-right", "0");

                        try {
                            depBox.setItems(departmentService.getDepName());
                            depBox.setValue(x);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        Button removeButton = new Button("Убрать Department");
                        removeButton.addClickListener(removeEvent -> {
                            layout.remove(depBox);
                            comboBoxes.remove(depBox);
                            layout.remove(removeButton);
                        });
                        layout.add(depBox, removeButton);
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                layout.setPadding(false);

                Button saveButton = new Button("Сохранить");
                saveButton.addClickListener(saveEvent -> {
                    String nameFieldValue = nameField.getValue();
                    String phoneFieldValue = phoneField.getValue();
                    String emailFieldValue = emailField.getValue();
                    String comboBoxValue = comboBox.getValue();

                    boolean b = comboBoxes.stream().noneMatch(ComboBox::isEmpty);


                    if (!nameFieldValue.isEmpty() && !phoneFieldValue.isEmpty()
                            && !emailFieldValue.isEmpty() && !comboBoxValue.isEmpty() && b) {

                        ArrayList<String> depName = new ArrayList<>();
                        comboBoxes.forEach(x->{
                            depName.add(x.getValue());
                        });

                        try {
                            service.updateEmployee(item.getId(),nameFieldValue, phoneFieldValue,emailFieldValue);
                            empDepService.deleteInter(item.getId());
                            empDepService.addDepEmp(item.getId(),comboBoxValue, depName);
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

                dialog.add(new VerticalLayout(new HorizontalLayout(idField, nameField, emailField),
                        new HorizontalLayout(phoneField, comboBox), layout , new HorizontalLayout(saveButton, cancelButton)));
                dialog.open();
            });

            return updateButton;
        }).setHeader("Обновление");

        Grid.Column<Employee> deleteColumn = grid.addComponentColumn(item -> {
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

                TextField phoneField = new TextField("Phone");
                phoneField.setValue(item.getPhone());
                phoneField.setReadOnly(true);

                TextField emailField = new TextField("Email");
                emailField.setValue(item.getEmail());
                emailField.setReadOnly(true);

                ComboBox<String> comboBox = new ComboBox<>("Job");

                try {
                    comboBox.setItems(jobService.getJobName());
                    comboBox.setValue(empDepService.findJobEmp(item.getId()));
                    comboBox.setReadOnly(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                VerticalLayout layout = new VerticalLayout();

                try {
                    ArrayList<String> depNames = empDepService.findDep(item.getId());
                    depNames.forEach(x ->{
                        ComboBox<String> depBox = new ComboBox<>("Department");
                        depBox.getElement().getStyle().set("margin-right", "0");

                        try {
                            depBox.setItems(departmentService.getDepName());
                            depBox.setValue(x);
                            depBox.setReadOnly(true);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        layout.add(depBox);
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                layout.setPadding(false);

                Button deleteBut = new Button("Удалить");
                deleteBut.addClickListener(saveEvent -> {
                    try {
                        empDepService.deleteInter(item.getId());
                        service.deleteEmployee(item.getId());
                        UI.getCurrent().getPage().reload();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Button cancelButton = new Button("Отмена");
                cancelButton.addClickListener(cancelEvent -> {
                    dialog.close();
                });

                dialog.add(new VerticalLayout(new HorizontalLayout(idField, nameField,emailField),
                        new HorizontalLayout(phoneField,comboBox), layout,
                        new HorizontalLayout(deleteBut, cancelButton)));
                dialog.open();
            });

            return deleteButton;
        }).setHeader("Удаление");

        add(tableMenu, grid);
    }
}

