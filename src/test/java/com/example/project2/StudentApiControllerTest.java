package com.example.project2;

import com.example.project2.controller.StudentApiController;
import com.example.project2.model.StudentModel;
import com.example.project2.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StudentApiController.class)
public class StudentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStudent() throws Exception {
        StudentModel student = new StudentModel();
        student.setId(1L);
        student.setName("Иван Иванов");
        student.setEmail("ivan@example.com");

        when(studentService.createStudent(any(StudentModel.class))).thenReturn(student);

        mockMvc.perform(post("/v1/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Иван Иванов\",\"email\":\"ivan@example.com\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"));
    }
}
