package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
@WebMvcTest(BinaryAPIController.class)
public class BinaryAPIControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void add() throws Exception {
        this.mvc.perform(get("/add").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("10001"));
    }
	
    @Test
    public void add2() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1","111").param("operand2","1010"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(111))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1010))
			.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10001))
			.andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("add"));
    }

    // --- 3 NEW TESTS FOR EXISTING API (ADDITION) ---

    @Test
    public void add3() throws Exception {
        // Testing addition with zero
        this.mvc.perform(get("/add").param("operand1","1011").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(content().string("1011"));
    }

    @Test
    public void add4() throws Exception {
        // Testing addition of large identical numbers via JSON
        this.mvc.perform(get("/add_json").param("operand1","1111").param("operand2","1111"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(1111))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(1111))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(11110))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("add"));
    }

    @Test
    public void add5() throws Exception {
        // Testing addition of different length numbers
        this.mvc.perform(get("/add").param("operand1","11").param("operand2","11100"))
            .andExpect(status().isOk())
            .andExpect(content().string("11111"));
    }

    // --- TESTS FOR NEWLY IMPLEMENTED OPERATIONS (*, &, |) ---

    // MULTIPLICATION TESTS
    @Test
    public void multiply() throws Exception {
        // 3 * 3 = 9 (11 * 11 = 1001)
        this.mvc.perform(get("/multiply").param("operand1","11").param("operand2","11"))
            .andExpect(status().isOk())
            .andExpect(content().string("1001"));
    }

    @Test
    public void multiplyJson() throws Exception {
        // Multiply by 0
        this.mvc.perform(get("/multiply_json").param("operand1","1010").param("operand2","0"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(1010))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("multiply"));
    }

    // BITWISE AND TESTS
    @Test
    public void and() throws Exception {
        // 1010 & 1100 = 1000
        this.mvc.perform(get("/and").param("operand1","1010").param("operand2","1100"))
            .andExpect(status().isOk())
            .andExpect(content().string("1000"));
    }

    @Test
    public void andJson() throws Exception {
        // 111 & 101 = 101
        this.mvc.perform(get("/and_json").param("operand1","111").param("operand2","101"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(111))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(101))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(101))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("and"));
    }

    // BITWISE OR TESTS
    @Test
    public void or() throws Exception {
        // 1010 | 0101 = 1111
        this.mvc.perform(get("/or").param("operand1","1010").param("operand2","101"))
            .andExpect(status().isOk())
            .andExpect(content().string("1111"));
    }

    @Test
    public void orJson() throws Exception {
        // 1001 | 100 = 1101
        this.mvc.perform(get("/or_json").param("operand1","1001").param("operand2","100"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand1").value(1001))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operand2").value(100))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1101))
            .andExpect(MockMvcResultMatchers.jsonPath("$.operator").value("or"));
    }
}