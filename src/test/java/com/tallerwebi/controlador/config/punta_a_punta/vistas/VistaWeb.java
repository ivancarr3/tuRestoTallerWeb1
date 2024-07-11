package com.tallerwebi.controlador.config.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class VistaWeb {
    public Page page;

    public VistaWeb(Page page) {
        this.page = page;
    }

    public String obtenerURLActual(){
        return page.url();
    }

    public String obtenerTextoDelElemento(String selectorCSS){
        return this.obtenerElemento(selectorCSS).textContent();
    }

    public void darClickEnElElemento(String selectorCSS){
        this.obtenerElemento(selectorCSS).click();
    }

    public void escribirEnElElemento(String selectorCSS, String texto){
        this.obtenerElemento(selectorCSS).type(texto);
    }

    public Locator obtenerElemento(String selectorCSS){
        return page.locator(selectorCSS);
    }

    public void seleccionarOpcionDelSelect(String selectorCSS, String valor) {
        this.obtenerElemento(selectorCSS).selectOption(valor);
    }

    public List<Locator> obtenerElementos(String selectorCSS) {
        return page.locator(selectorCSS).all();
    }
}