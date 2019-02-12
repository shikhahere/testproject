package springapp;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import valueobjects.VersionInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VersionCheckerTest {

	@Autowired
	private MockMvc mvc;

	@Mock
	private RestTemplate restTemplate ;

	@InjectMocks
	private VersionChecker versionChecker;

	@Before
	public void setup() throws Exception {
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		MockitoAnnotations.initMocks(this);
		this.mvc = MockMvcBuilders.standaloneSetup(versionChecker).build();
	}

	@Test
	public void valid_pws_version() throws Exception {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setApiVersion("2.63.0");

		Mockito.when(restTemplate.getForEntity("https://api.run.pivotal.io/v2/info", VersionInfo.class))
		.thenReturn(ResponseEntity.status(HttpStatus.OK).body(versionInfo));

		mvc.perform(MockMvcRequestBuilders.get("/pws/validVersion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("true"));
	}

	@Test
	public void invalid_pws_version() throws Exception {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setApiVersion("2.65.0");

		Mockito.when(restTemplate.getForEntity("https://api.run.pivotal.io/v2/info", VersionInfo.class))
		.thenReturn(ResponseEntity.status(HttpStatus.OK).body(versionInfo));

		mvc.perform(MockMvcRequestBuilders.get("/pws/validVersion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("false"));
	}

	@Test
	public void not_valid_status_code() throws Exception {

		Mockito.when(restTemplate.getForEntity("https://api.run.pivotal.io/v2/info", VersionInfo.class))
				.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
		
		mvc.perform(MockMvcRequestBuilders.get("/pws/validVersion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	/*@Test
	public void exception_results_in_500_error() throws Exception {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setApiVersion("2.64.0");

		Mockito.when(restTemplate.getForEntity("https://api.run.pivotal.io/v2/info", VersionInfo.class))
				.thenThrow(new RuntimeException("A random exception"));
		
		mvc.perform(MockMvcRequestBuilders.get("/pws/validVersion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}*/
	
}
