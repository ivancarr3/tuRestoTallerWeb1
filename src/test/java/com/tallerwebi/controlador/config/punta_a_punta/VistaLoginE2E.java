package com.tallerwebi.controlador.config.punta_a_punta;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.microsoft.playwright.*;
import com.tallerwebi.controlador.config.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.*;

public class VistaLoginE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
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
        vistaLogin = new VistaLogin(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirTuRestoEnElNavbar() {
        String texto = vistaLogin.obtenerTextoDeLaBarraDeNavegacion();
        assertThat("TuResto", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaDarUnErrorAlNoCompletarElLoginYTocarElBoton() {
        vistaLogin.escribirEMAIL("admisn@admin.com");
        vistaLogin.escribirClave("admin");
        vistaLogin.darClickEnIniciarSesion();
        String texto = vistaLogin.obtenerMensajeDeError();
        assertThat("Error Usuario o clave incorrecta", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaNavegarAlHomeSiElUsuarioExiste() {
        vistaLogin.escribirEMAIL("admin@admin.com");
        vistaLogin.escribirClave("admin");
        vistaLogin.darClickEnIniciarSesion();
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/turesto/home"));
    }
}
