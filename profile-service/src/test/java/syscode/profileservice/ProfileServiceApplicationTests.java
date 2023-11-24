package syscode.profileservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import syscode.profileservice.entity.Student;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class ProfileServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	final String testName1 = "Pete";
	final String testName2 = "Joe";
	final String testName3 = "Mary";
	final String testEmail1 = "pete@test.com";
	final String testEmail2 = "joe@test.com";
	final String testEmail3 = "mary@test.com";

	@Test
	void studentManagementIntegrationTest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/students"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));

		mockMvc.perform(MockMvcRequestBuilders.post("/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(getStudentJson(testName1, testEmail1)))
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.post("/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(getStudentJson(testName2, testEmail2)))
				.andExpect(status().isOk());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/students"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		      	.andExpect(jsonPath("$[0].name", is(testName1)))
		      	.andExpect(jsonPath("$[0].email", is(testEmail1)))
				.andExpect(jsonPath("$[1].name", is(testName2)))
				.andExpect(jsonPath("$[1].email", is(testEmail2)))
				.andReturn();

		List<Student> studentList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

		mockMvc.perform(MockMvcRequestBuilders.put("/students/{id}", studentList.get(0).getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(getStudentJson(testName3, testEmail3)))
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.get("/students"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is(testName3)))
				.andExpect(jsonPath("$[0].email", is(testEmail3)));


		mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", studentList.get(0).getId()))
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", studentList.get(1).getId()))
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.get("/students"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	private String getStudentJson(String name, String email){
		return  "{\"name\": \"" + name + "\", \"email\": \"" + email + "\"}";
	}
}
