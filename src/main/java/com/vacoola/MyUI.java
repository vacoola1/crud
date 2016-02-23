package com.vacoola;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vacoola.backend.entity.User;
import com.vacoola.backend.service.Paging;
import com.vacoola.backend.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.vacoola.MyAppWidgetset")
public class MyUI extends UI {

    final ApplicationContext context = new ClassPathXmlApplicationContext("/com/vacoola/backend/application-context.xml");
    UserService userService = context.getBean(UserService.class);

    TextField filter = new TextField();
    Grid userGrid = new Grid();


    Button startPageButton = new Button("start", this::startCurrentPage);
    Button prevPageButton = new Button("<", this::prevCurrentPage);
    Button nextPageButton = new Button(">", this::nextCurrentPage);
    Button endPageButton = new Button("end", this::endCurrentPage);

    UserForm userForm = new UserForm();

    Paging paging = new Paging();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        filter.setInputPrompt("Filter users...");
        filter.addTextChangeListener(e -> refreshContactsEventFound(e.getText()));

        userGrid.setContainerDataSource(new BeanItemContainer<>(User.class));
        userGrid.setColumnOrder("id", "name", "age", "admin", "createdDate");

        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        userGrid.addSelectionListener(e
                -> userForm.edit((User) userGrid.getSelectedRow()));

        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        refreshContacts();
    }

    private void buildLayout() {

        HorizontalLayout actions = new HorizontalLayout(filter);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        HorizontalLayout paging = new HorizontalLayout(startPageButton, prevPageButton, nextPageButton, endPageButton);

        VerticalLayout left = new VerticalLayout(actions, userGrid, paging);
        left.setSizeFull();
        userGrid.setSizeFull();
        left.setExpandRatio(userGrid, 1);

        left.setComponentAlignment(paging, Alignment.MIDDLE_CENTER);

        HorizontalLayout mainLayout = new HorizontalLayout(left, userForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        // Split and allow resizing
        setContent(mainLayout);

    }

    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    void refreshContactsEventFound(String stringFilter) {
        paging.currentPage = 1;
        refreshContacts(stringFilter);
    }

    private void refreshContacts(String stringFilter) {
        userGrid.setContainerDataSource(new BeanItemContainer<>(User.class, userService.getPageUsers(stringFilter, paging)));
        pagingButtomAccess();

        //userGrid.setVisible(true);
    }

    public void nextCurrentPage(Button.ClickEvent event) {
        paging.currentPage++;
        refreshContacts();
    }

    public void prevCurrentPage(Button.ClickEvent event) {
        paging.currentPage--;
        if (paging.currentPage <= 0) {
            paging.currentPage = 1;
        }
        refreshContacts();
    }

    public void startCurrentPage(Button.ClickEvent event) {
        paging.currentPage = 1;
        refreshContacts();
    }
    public void endCurrentPage(Button.ClickEvent event){
        paging.currentPage = paging.maxPage;
        refreshContacts();
    }

    private void pagingButtomAccess() {
        startPageButton.setReadOnly(paging.currentPage == 1);
        prevPageButton.setReadOnly(paging.currentPage == 1);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
