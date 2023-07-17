package org.vaadin.example;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;

public class TableMenu extends MenuBar {

    public TableMenu(){
        super();
        createSubMenu();
    }

    private void createSubMenu() {
        MenuItem tables = addItem("Tables");
        SubMenu subItems = tables.getSubMenu();

        MenuItem jobItem = subItems.addItem("Job");
        MenuItem departmentItem = subItems.addItem("Department");
        MenuItem employeeItem = subItems.addItem("Employee");
        MenuItem empDep = subItems.addItem("EmpDepJob");


        ComponentEventListener<ClickEvent<MenuItem>> listenerJob = event -> {
            UI.getCurrent().navigate("/job");
        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerDep = event -> {
            UI.getCurrent().navigate("/department");
        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerEmp = event -> {
            UI.getCurrent().navigate("/employee");
        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerEmpDep = event -> {
            UI.getCurrent().navigate("/emp-dep-job");
        };

        jobItem.addClickListener(listenerJob);
        departmentItem.addClickListener(listenerDep);
        employeeItem.addClickListener(listenerEmp);
        empDep.addClickListener(listenerEmpDep);
    }
}
