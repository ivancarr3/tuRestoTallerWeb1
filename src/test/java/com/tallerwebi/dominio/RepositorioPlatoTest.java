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
    private SessionFactory sessionFactory;

    private List<Plato> platos = new ArrayList<>();

    @BeforeEach
    public void init() {
        this.platos.add(crearYGuardarPlato("Milanesa de pollo", 20000.0, "Rellena de jyq"));
        this.platos.add(crearYGuardarPlato("Milanesa de carne", 15000.0, "Con salsa"));
        this.platos.add(crearYGuardarPlato("Tarta de espinaca", 14000.0, "Con queso"));
    }

    private Plato crearYGuardarPlato(String nombre, Double precio, String descripcion) {
        Plato plato = new Plato(null, nombre, precio, descripcion, "ensaladas.jpg");
        repositorioPlato.guardarPlato(plato);
        return plato;
    }

    @Test
    public void queGuardePlatoCorrectamente() {
        crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq");
        List<Plato> platos = repositorioPlato.get();
        assertThat(platos.size(), equalTo(4));
    }

    @Test
    public void queDevuelvaPlatoPorId() {
        Plato plato = crearYGuardarPlato("La Quintana", 4.5, "Arieta 5000");
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
        Plato plato = crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq");

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
        Plato plato = crearYGuardarPlato("Milanesas", 20000.0, "Rellenas de jyq");

        Long id = plato.getId();
        repositorioPlato.eliminarPlato(plato);

        assertNull(repositorioPlato.buscar(id));
    }
}
