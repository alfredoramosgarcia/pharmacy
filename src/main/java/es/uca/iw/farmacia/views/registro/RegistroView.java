package es.uca.iw.farmacia.views.registro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "registro", layout = MainLayout.class)
@PermitAll
public class RegistroView extends Div {
    public RegistroView()  {
            // Crear los campos de formulario
            TextField nombreField = new TextField("Nombre");
            TextField emailField = new TextField("Email");
            PasswordField passwordField = new PasswordField("Contraseña");

            // Crear el botón de registro
            Button registroButton = new Button("Registrarse");
            registroButton.addClickListener(e -> {
                // Lógica para procesar el registro del usuario
                String nombre = nombreField.getValue();
                String email = emailField.getValue();
                String password = passwordField.getValue();

                // Lógica de validación y almacenamiento de datos

                // Ejemplo de notificación de registro exitoso
                Notification.show("Registro exitoso");
            });

            // Crear el diseño del formulario
            FormLayout formLayout = new FormLayout();
            formLayout.add(nombreField, emailField, passwordField, registroButton);

            // Establecer estilos CSS para centrar y apilar los elementos
            Div container = new Div();
            container.getStyle().set("display", "flex");
            container.getStyle().set("flex-direction", "column");
            container.getStyle().set("align-items", "center");
            container.add(new H1("Registro de Usuario"), formLayout);

            // Establecer el contenedor como contenido de la vista
            add(container);
        }
}
