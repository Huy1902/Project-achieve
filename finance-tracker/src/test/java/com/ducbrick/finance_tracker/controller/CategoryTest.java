package com.ducbrick.finance_tracker.controller;

import com.ducbrick.finance_tracker.FinanceTrackerApplication;
import com.ducbrick.finance_tracker.controllers.CategoryController;
import com.ducbrick.finance_tracker.services.CategoryFinderService;
import com.ducbrick.finance_tracker.services.CategoryPersistenceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@ContextConfiguration(classes = FinanceTrackerApplication.class)
public class CategoryTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private CategoryFinderService categoryFinderService;

  @MockBean
  private CategoryPersistenceService categoryPersistenceService;


  @Test
  @WithMockUser
  void addCategoryFormTest() throws Exception {
    this.mvc.perform(MockMvcRequestBuilders.get("/category/add"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("category"))
            .andExpect(content().string(Matchers.containsString("<h1 class=\"text-center\">Add category</h1>")));
  }

  @Test
  @WithMockUser
  void addNewCategorySuccessTest() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/category/add").param("name", "black")
                    .param("color", "#000000").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/transaction/add"));
  }



}
