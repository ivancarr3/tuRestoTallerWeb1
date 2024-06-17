package com.tallerwebi.integracion;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorLoginTest {

//	private Usuario usuarioMock;
//
//	@Autowired
//	private WebApplicationContext wac;
//	private MockMvc mockMvc;
//
//	@BeforeEach
//	public void init() {
//		usuarioMock = mock(Usuario.class);
//		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//	}
//
//	@Test
//	public void debeRetornarLaPaginaLoginCuandoSeNavegaALaRaiz() throws Exception {
//
//		MvcResult result = this.mockMvc.perform(get("/")).andDo(null).andExpect(status().is3xxRedirection())
//				.andReturn();
//
//		ModelAndView modelAndView = result.getModelAndView();
//		assert modelAndView != null;
//		assertThat("redirect:/login", equalToIgnoringCase(Objects.requireNonNull(modelAndView.getViewName())));
//		assertThat(true, is(modelAndView.getModel().isEmpty()));
//	}
//
//	@Test
//	public void debeRetornarLaPaginaLoginCuandoSeNavegaALLogin() throws Exception {
//
//		MvcResult result = this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andReturn();
//
//		ModelAndView modelAndView = result.getModelAndView();
//		assert modelAndView != null;
//		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
//		assertThat(modelAndView.getModel().get("datosLogin").toString(),
//				containsString("com.tallerwebi.presentacion.DatosLogin"));
//
//	}
}
