package com.tallerwebi.controlador.config.punta_a_punta;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.tallerwebi.controlador.config.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.controlador.config.punta_a_punta.vistas.VistaWeb;
import org.junit.jupiter.api.*;

import java.util.List;

public class VistaRestauranteE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaWeb vistaWeb;
    VistaLogin vistaLogin;

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
        vistaLogin = new VistaLogin(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirTuRestoEnElNavbar() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/restaurante/3");
        String texto = vistaWeb.obtenerTextoDelElemento("body > header > div > a");
        assertThat("TuResto", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaRealizarLaReservaCorrectamente() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/login");
        vistaLogin.escribirEMAIL("ivancarr03@gmail.com");
        vistaLogin.escribirClave("123");
        vistaLogin.darClickEnIniciarSesion();
        vistaWeb.darClickEnElElemento("body > main > div:nth-child(3) > div:nth-child(3) > div > div > a");
        vistaWeb.escribirEnElElemento("#nombre_form", "mateo");
        vistaWeb.escribirEnElElemento("#email_form", "mateo.fortu@gmail.com");
        vistaWeb.escribirEnElElemento("#num_form", "1234");
        vistaWeb.escribirEnElElemento("#dni_form", "4321");
        vistaWeb.escribirEnElElemento("#fecha_form", "12-07-2024");
        vistaWeb.seleccionarOpcionDelSelect("#cant_personas_form", "1");

        vistaWeb.page.locator("#form_reserva > div > div.col-md-12 > button").click();

        vistaWeb.page.waitForLoadState(LoadState.NETWORKIDLE);

        String urlActual = vistaWeb.obtenerURLActual();
        assertThat(urlActual, is("http://localhost:8080/turesto/reservar"));
    }

    @Test
    void deberiaContarLasTarjetasPorCategoria() {
        vistaWeb.page.navigate("http://localhost:8080/turesto/platos");

        List<ElementHandle> categorias = vistaWeb.page.querySelectorAll("div[th\\:each='entry : ${platosPorCategoria}']");

        for (ElementHandle categoria : categorias) {
            String categoriaNombre = categoria.querySelector("h3").innerText();
            List<ElementHandle> tarjetas = categoria.querySelectorAll("div[th\\:each='plato, iterStat : ${entry.value}']");
            assertThat("La categoría " + categoriaNombre + " debe tener al menos una tarjeta", tarjetas.size(), greaterThan(0));
        }
    }
}
