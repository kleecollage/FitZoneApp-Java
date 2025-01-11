package gm.fit_zone;

import com.formdev.flatlaf.FlatDarkLaf;
import gm.fit_zone.gui.FitZoneForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

// @SpringBootApplication
public class FitZoneSwing {
    public static void main(String[] args) {
        // Dark mode
        FlatDarkLaf.setup();
        // Spring Fabric
        ConfigurableApplicationContext contextSpring =
                new SpringApplicationBuilder(FitZoneSwing.class)
                        .headless(false)
                        .web(WebApplicationType.NONE)
                        .run(args);
        // Create Swing object
        SwingUtilities.invokeLater(() -> {
            FitZoneForm fitZoneForm = contextSpring.getBean(FitZoneForm.class);
            fitZoneForm.setVisible(true);
        });
    }
}
