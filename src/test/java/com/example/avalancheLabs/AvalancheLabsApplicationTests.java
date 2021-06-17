package com.example.avalancheLabs;

import com.example.avalancheLabs.controllers.APIController;
import com.example.avalancheLabs.models.Data;
import com.example.avalancheLabs.models.DataCached;
import com.example.avalancheLabs.models.DataInTO;
import com.example.avalancheLabs.repositories.DataCacheRepository;
import com.example.avalancheLabs.repositories.DataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AvalancheLabsApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(AvalancheLabsApplicationTests.class);
	private static final String API_ROOT = "http://localhost:8080/api";

	@Autowired
	DataRepository dataRepository;

	@Autowired
	DataCacheRepository dataCacheRepository;

	@Autowired
	private MockMvc mockMvc;

	@AfterEach
	public void afterEach() {
		dataRepository.deleteAll();
		dataCacheRepository.deleteAll();
	}

	@Test
	public void saveDataTest() throws Exception {

		DataInTO data = new DataInTO(
						43894,
						"Kaunas",
						"2018-04-21 08:32:50"
					);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(data);
		String URL = API_ROOT + "/migration/ocr";
		this.mockMvc.perform(post(URL)
				.contentType(APPLICATION_JSON_VALUE)
				.content(jsonString))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("data saved!")));

		this.mockMvc.perform(post(URL)
				.contentType(APPLICATION_JSON_VALUE)
				.content(jsonString))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void getAllTest() throws Exception {
		create(5356, "testing", LocalDateTime.now());
		create(5765, "Paris", LocalDateTime.now());
		create(8640, "Kaunas", LocalDateTime.now());

		String URL = API_ROOT + "/all";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(3)));

		dataRepository.deleteAll();

		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(0)));
	}

	@Test
	public void getAllFromCachedTest() throws Exception {
		createInCache(5356, "testing", LocalDateTime.now());
		createInCache( 5765, "Paris", LocalDateTime.now());

		String URL = API_ROOT + "/cached/all";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(2)));

		dataCacheRepository.deleteAll();

		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(0)));
	}

	@Test
	public void getSingleFromCache() throws Exception {
		int foreignId = 4653;
		createInCache(foreignId, "testing", LocalDateTime.now());

		String URL = API_ROOT + "/cached/details/" + foreignId;

		Iterator<DataCached> list = dataCacheRepository.findAll().iterator();

		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.foreign_id").value(foreignId));

		URL = API_ROOT + "/cached/details/" + 676876;
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());


		URL = API_ROOT + "/cached/details/";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());

	}

	@Test
	public void flushCacheTest() throws Exception {
		String URL = API_ROOT + "/cached/flush";

		createInCache(5356, "testing", LocalDateTime.now());
		createInCache(5765, "Paris", LocalDateTime.now());

		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());

		Iterator<DataCached> list = dataCacheRepository.findAll().iterator();

		assertFalse(list.hasNext());
	}

	@Test
	public void posiblySameTest() throws Exception {

		String URL = API_ROOT + "/cached/possibly-same";
		String word = "tes2";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE).queryParam("word", word))
				.andExpect(status().isOk())
				.andExpect(content().string("false"));

		createInCache(5356, "test", LocalDateTime.now());
		createInCache(5765, "barking", LocalDateTime.now());

		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE).queryParam("word", word))
				.andExpect(status().isOk())
				.andExpect(content().string("true"));

		word = "testas";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE).queryParam("word", word))
				.andExpect(status().isOk())
				.andExpect(content().string("false"));

		word = "tes";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE).queryParam("word", word))
				.andExpect(status().isOk())
				.andExpect(content().string("false"));

		word = "@arkin7";
		mockMvc.perform(get(URL).contentType(APPLICATION_JSON_VALUE).queryParam("word", word))
				.andExpect(status().isOk())
				.andExpect(content().string("true"));
	}

	private void create(int foreign_id, String word, LocalDateTime created) {
		dataRepository.save(
				new Data(
						foreign_id,
						word,
						created
				)
		);
	}
	private void createInCache(int foreignId, String word, LocalDateTime created) {
		dataCacheRepository.save(
				new DataCached(
						foreignId,
						word,
						created
				)
		);
	}


}
