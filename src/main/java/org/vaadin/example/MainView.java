package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;



@Route()
public class MainView extends VerticalLayout {

    public MainView(){

        Button job = new Button("Job");
        job.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/job");
        });
        job.addThemeVariants(ButtonVariant.LUMO_LARGE);

        Button department = new Button("Department");
        department.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/department");
        });
        department.addThemeVariants(ButtonVariant.LUMO_LARGE);

        Button employee = new Button("Employee");
        employee.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/employee");
        });
        employee.addThemeVariants(ButtonVariant.LUMO_LARGE);

        Button empDep = new Button("Employee-Department-Job");
        empDep.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("/emp-dep-job");
        });
        empDep.addThemeVariants(ButtonVariant.LUMO_LARGE);

        add(new HorizontalLayout(job,department,employee,empDep));

    }

}
