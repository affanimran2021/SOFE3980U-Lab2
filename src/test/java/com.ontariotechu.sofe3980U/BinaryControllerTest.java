package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.runner.RunWith;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

import static org.hamcrest.Matchers.containsString;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getDefault() throws Exception {
        this.mvc.perform(get("/"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", ""))
			.andExpect(model().attribute("operand1Focused", false));
    }
	
    @Test
    public void getParameter() throws Exception {
        this.mvc.perform(get("/").param("operand1","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", "111"))
			.andExpect(model().attribute("operand1Focused", true));
    }

	@Test
    public void postParameter() throws Exception {
        this.mvc.perform(post("/").param("operand1","111").param("operator","+").param("operand2","111"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
			.andExpect(model().attribute("result", "1110"))
			.andExpect(model().attribute("operand1", "111"));
    }

    // --- 3 NEW TESTS FOR EXISTING WEB APPLICATION (ADDITION) ---

    @Test
    public void postParameterAddZero() throws Exception {
        this.mvc.perform(post("/").param("operand1","1010").param("operator","+").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1010"));
    }

    @Test
    public void postParameterAddDifferentLengths() throws Exception {
        this.mvc.perform(post("/").param("operand1","11").param("operator","+").param("operand2","1011"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1110"));
    }

    @Test
    public void postParameterAddLargeNumbers() throws Exception {
        this.mvc.perform(post("/").param("operand1","1111").param("operator","+").param("operand2","1111"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "11110"));
    }

    // --- TESTS FOR NEWLY IMPLEMENTED OPERATIONS (*, &, |) ---

    // Multiplication Tests
    @Test
    public void postParameterMultiplyZero() throws Exception {
        this.mvc.perform(post("/").param("operand1","1101").param("operator","*").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    @Test
    public void postParameterMultiplyByOne() throws Exception {
        this.mvc.perform(post("/").param("operand1","1010").param("operator","*").param("operand2","1"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1010"));
    }

    @Test
    public void postParameterMultiplyStandard() throws Exception {
        // 3 * 3 = 9 (11 * 11 = 1001)
        this.mvc.perform(post("/").param("operand1","11").param("operator","*").param("operand2","11"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1001"));
    }

    // Bitwise AND Tests
    @Test
    public void postParameterAndZero() throws Exception {
        this.mvc.perform(post("/").param("operand1","1111").param("operator","&").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "0"));
    }

    @Test
    public void postParameterAndMatching() throws Exception {
        this.mvc.perform(post("/").param("operand1","101").param("operator","&").param("operand2","101"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "101"));
    }

    @Test
    public void postParameterAndDifferentLengths() throws Exception {
        // 1010 & 11 = 0010 (2)
        this.mvc.perform(post("/").param("operand1","1010").param("operator","&").param("operand2","11"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "10"));
    }

    // Bitwise OR Tests
    @Test
    public void postParameterOrZero() throws Exception {
        this.mvc.perform(post("/").param("operand1","1001").param("operator","|").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1001"));
    }

    @Test
    public void postParameterOrMatching() throws Exception {
        this.mvc.perform(post("/").param("operand1","1100").param("operator","|").param("operand2","1100"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1100"));
    }

    @Test
    public void postParameterOrDifferentLengths() throws Exception {
        // 1000 | 111 = 1111
        this.mvc.perform(post("/").param("operand1","1000").param("operator","|").param("operand2","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
            .andExpect(model().attribute("result", "1111"));
    }
}