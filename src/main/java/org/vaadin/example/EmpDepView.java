package org.vaadin.example;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.models.EmpDep;

import java.sql.SQLException;
import java.util.stream.Stream;

@Route("/emp-dep-job")
public class EmpDepView extends VerticalLayout {


    public EmpDepView(@Autowired EmpDepService service) {
        setSizeFull();

        TableMenu tableMenu = new TableMenu();

        Grid<EmpDep> grid = new Grid<>(EmpDep.class, false);
        grid.addColumn(EmpDep::getEmpId);
        grid.addColumn(EmpDep::getEmpName);
        grid.addColumn(EmpDep::getDepId);
        grid.addColumn(EmpDep::getDepName);
        grid.addColumn(EmpDep::getJobId);
        grid.addColumn(EmpDep::getJobName);

        //grid.addColumn(createToggleDetailsRenderer(grid));

        grid.setItemDetailsRenderer(createEmpDepDetailsRenderer());

        try {
            grid.setItems(service.getEmpDep());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        add(tableMenu, grid);
    }

    private ComponentRenderer<EmpDepDetailsFormLayout, EmpDep> createEmpDepDetailsRenderer(){
        return new ComponentRenderer<>(
             EmpDepDetailsFormLayout::new,
                EmpDepDetailsFormLayout::setEmpDep
         );
    }

    private static class EmpDepDetailsFormLayout extends FormLayout {
        private final TextField EmpPhone = new TextField("Employee Phone");
        private final TextField EmpEmail = new TextField("Employee Email");
        private final TextField DepEmail = new TextField("Department Email");
        private final TextField depPhone = new TextField("Department Phone");
        private final TextField JobSalary = new TextField("Salary");


        public EmpDepDetailsFormLayout(){
            Stream.of(EmpPhone, EmpEmail, DepEmail,depPhone,JobSalary).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 1));
        }
        public void setEmpDep(EmpDep empDep) {
            EmpPhone.setValue(empDep.getEmpPhone());
            EmpEmail.setValue(empDep.getEmpEmail());
            DepEmail.setValue(empDep.getDepEmail());
            depPhone.setValue(empDep.getDepPhone());
            JobSalary.setValue(empDep.getJobSalary());
        }
    }

    /*private static ComponentRenderer<Button, EmpDep> createToggleDetailsRenderer(
            Grid<EmpDep> grid) {
        return new ComponentRenderer<>(
                item -> {
                    Button toggleButton = new Button("Toggle details");
                    toggleButton.addClickListener(e -> grid.setDetailsVisible(item, !grid.isDetailsVisible(item)));
                    return toggleButton;
                });*/
    }



