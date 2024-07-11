package com.tallerwebi.controlador.config.punta_a_punta;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.microsoft.playwright.*;
import com.tallerwebi.controlador.config.punta_a_punta.vistas.VistaWeb;
import org.junit.jupiter.api.*;

import java.util.List;

public class VistaHomeE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaWeb vistaWeb;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaWeb = new VistaWeb(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirTuRestoEnElNavbar() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/home");
        String texto = vistaWeb.obtenerTextoDelElemento("body > header > div > a");
        assertThat("TuResto", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaNavegarALaCategoriaDePizzas() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/home");
        vistaWeb.darClickEnElElemento("a[href*='/home/platos/pizzas']");
        String urlActual = vistaWeb.obtenerURLActual();
        assertThat(urlActual, containsString("/home/platos/pizzas"));
    }

    @Test
    void deberiaFiltrarPorEstrellas() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/home");

        vistaWeb.seleccionarOpcionDelSelect("select[name='filtrado']", "5.0");

        vistaWeb.darClickEnElElemento("body > main > div:nth-child(2) > div > form:nth-child(1) > div > button");

        List<Locator> estrellas = vistaWeb.obtenerElementos(".contRestaurant-estrellas");
        for (Locator estrella : estrellas) {
            double valorEstrella = Double.parseDouble(estrella.textContent());
            assertThat("El valor de las estrellas debería ser 5.0", valorEstrella, greaterThanOrEqualTo(5.0));
        }
    }

    @Test
    void deberiaOrdenarPorEstrellasDescendentes() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/home");

        vistaWeb.seleccionarOpcionDelSelect("select[name='filtro_orden']", "DESC");

        vistaWeb.darClickEnElElemento("body > main > div:nth-child(2) > div > form.d-flex.align-items-center.ms-2 > div > button");

        List<Locator> estrellas = vistaWeb.obtenerElementos(".contRestaurant-estrellas");
        double previousValue = Double.MAX_VALUE;
        for (Locator estrella : estrellas) {
            double valorEstrella = Double.parseDouble(estrella.textContent());
            assertThat("Los valores de las estrellas deberían estar en orden descendente", valorEstrella, lessThanOrEqualTo(previousValue));
            previousValue = valorEstrella;
        }
    }

    @Test
    void deberiaFiltrarPorCapacidad() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/home");

        vistaWeb.escribirEnElElemento("input[name='capacidadPersonas']", "4");

        vistaWeb.darClickEnElElemento("body > main > div:nth-child(2) > div > form:nth-child(3) > div > button");

        List<Locator> capacidad = vistaWeb.obtenerElementos(".contRestaurant-capacidad");
        for (Locator cap : capacidad) {
            int valorCapacidad = Integer.parseInt(cap.textContent());
            assertThat("El valor de la capacidad debería ser al menos 4", valorCapacidad, greaterThanOrEqualTo(4));
        }
    }
}
