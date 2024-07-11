package com.tallerwebi.dominio;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
@Transactional
public class RepositorioPlatoTest {
    @Autowired
    private RepositorioPlato repositorioPlato;

    @Autowired
    private RepositorioRestaurante repositorioRestaurante;

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @Autowired
    private SessionFactory sessionFactory;

    private final List<Plato> platos = new ArrayList<>();
    private final Restaurante restauranteInit = new Restaurante(null, "Restaurante Mock", 4.5, "Direccion Mock", "imagenMock.jpg", 50, -34.598940, -58.415550);
    private final Categoria categoriaInit = new Categoria(null, "Categoria Mock", "Img Mock");

    @BeforeEach
    public void init() {
        repositorioRestaurante.guardar(restauranteInit);
        repositorioCategoria.guardar(categoriaInit);
        this.platos.add(crearYGuardarPlato("Milanesa de pollo", 20000.0, "Rellena de jyq", restauranteInit, categoriaInit));
        this.platos.add(crearYGuardarPlato("Milanesa de carne", 15000.0, "Con salsa", restauranteInit, categoriaInit));
        this.platos.add(crearYGuardarPlato("Tarta de espinaca", 14000.0, "Con queso", restauranteInit, categoriaInit));
    }

    private Plato crearYGuardarPlato(String nombre, Double precio, String descripcion, Restaurante restaurante, Categoria categoria) {
        Plato plato = new Plato(null, nombre, precio, descripcion, "ensalada.jpg", restaurante, categoria, true);
        repositorioPlato.guardarPlato(plato);
        return plato;
    }

    @Test
    public void queGuardePlatoCorrectamente() {
        crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq", this.restauranteInit, this.categoriaInit);
        List<Plato> platos = repositorioPlato.get();
        assertThat(platos.size(), equalTo(4));
    }

    @Test
    public void queDevuelvaPlatosDeUnRestaurante() {
        Plato plato = crearYGuardarPlato("Manteca rellena", 4500.0, "Bien sana", this.restauranteInit, this.categoriaInit);
        Long idRestaurante = plato.getRestaurante().getId();
        List<Plato> platos = repositorioPlato.getPlatosDeRestaurante(idRestaurante);
        assertNotNull(platos);
        assertEquals(4, platos.size());
    }

    @Test
    public void queDevuelvaPlatoPorId() {
        Plato plato = crearYGuardarPlato("La Quintana", 4.5, "Arieta 5000", this.restauranteInit, this.categoriaInit);
        Long id = plato.getId();
        Plato platoEncontrado = repositorioPlato.buscar(id);
        assertNotNull(platoEncontrado);
        assertEquals(plato.getNombre(), platoEncontrado.getNombre());
        assertEquals(plato.getDescripcion(), platoEncontrado.getDescripcion());
        assertEquals(plato.getPrecio(), platoEncontrado.getPrecio());
    }

    @Test
    public void queAlBuscarPorPrecioDevuelvaLosCorrespondientes() {
        Double precio = 14000.1;
        List<Plato> platos = repositorioPlato.buscarPlatoPorPrecio(precio);

        assertEquals(2, platos.size());
    }

    @Test
    public void queAlBuscarPorNombreDevuelvaLosCorrespondientes() {
        String nombre = "milanesa";
        List<Plato> platos = repositorioPlato.buscarPlatoPorNombre(nombre);

        assertEquals(2, platos.size());
    }

    @Test
    public void queOrdenePlatosPorPrecioAscendente() {
        List<Plato> platos = repositorioPlato.ordenarPorPrecio("ASC");

        assertEquals(3, platos.size());
        assertEquals(14000.0, platos.get(0).getPrecio());
        assertEquals(15000.0, platos.get(1).getPrecio());
        assertEquals(20000.0, platos.get(2).getPrecio());
    }

    @Test
    public void queOrdenePlatosPorPrecioDescendente() {
        List<Plato> platos = repositorioPlato.ordenarPorPrecio("DESC");

        assertEquals(3, platos.size());
        assertEquals(20000.0, platos.get(0).getPrecio());
        assertEquals(15000.0, platos.get(1).getPrecio());
        assertEquals(14000.0, platos.get(2).getPrecio());
    }

    @Test
    public void queActualizePlato() {
        Restaurante restauranteMock = repositorioRestaurante.buscar(1L);
        Plato plato = crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq", this.restauranteInit, this.categoriaInit);

        Long id = plato.getId();
        String nombre = "Test";
        String descripcion = "test desc";
        Double precio = 10000.0;
        plato.setNombre(nombre);
        plato.setDescripcion(descripcion);
        plato.setPrecio(precio);
        repositorioPlato.modificarPlato(plato);
        Plato platoActualizado = repositorioPlato.buscar(id);

        assertEquals(platoActualizado.getNombre(), nombre);
        assertEquals(platoActualizado.getDescripcion(), descripcion);
        assertEquals(platoActualizado.getPrecio(), precio);
    }

    @Test
    public void queEliminePlato() {
        Restaurante restauranteMock = repositorioRestaurante.buscar(1L);
        Plato plato = crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq", this.restauranteInit, this.categoriaInit);

        Long id = plato.getId();
        repositorioPlato.eliminarPlato(plato);

        assertNull(repositorioPlato.buscar(id));
    }

    @Test
    public void queDevuelvaPlatosAgrupadosPorCategoria() {
        Categoria otraCategoria = new Categoria(null, "Otra Categoria", "Otra img");
        repositorioCategoria.guardar(otraCategoria);

        crearYGuardarPlato("Pizza", 25000.0, "Pizza con mucho queso", this.restauranteInit, otraCategoria);
        crearYGuardarPlato("Hamburguesa", 18000.0, "Hamburguesa completa", this.restauranteInit, otraCategoria);

        List<Plato> platosAgrupados = repositorioPlato.getPlatosAgrupadosPorCategoria();

        assertNotNull(platosAgrupados);
        assertEquals(5, platosAgrupados.size());
    }

    @Test
    public void queDevuelvaPlatosPorCategoria() {
        List<Plato> platosPorCategoria = repositorioPlato.getPlatosPorCategoria("Categoria Mock");

        assertNotNull(platosPorCategoria);
        assertEquals(3, platosPorCategoria.size());
        assertEquals("Milanesa de pollo", platosPorCategoria.get(0).getNombre());
        assertEquals("Milanesa de carne", platosPorCategoria.get(1).getNombre());
        assertEquals("Tarta de espinaca", platosPorCategoria.get(2).getNombre());
    }

}
