package com.tallerwebi.dominio;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tallerwebi.controlador.config.HibernateTestConfig;
import com.tallerwebi.controlador.config.SpringWebTestConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
@Transactional
public class RepositorioAdministradorRestauranteTest {

    @Autowired
    RepositorioAdministradorRestaurante repositorioAdministradorRestaurante;

}
