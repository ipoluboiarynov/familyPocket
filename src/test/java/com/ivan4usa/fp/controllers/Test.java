package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.services.AccountTypeService;
import com.ivan4usa.fp.services.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
//@WithMockUser(username="ivan",roles={"ADMIN"})
@WithMockUser
class Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountTypeService service;

    @MockBean
    private UserService userService;
//    @Autowired
//    private JWTAuthenticationFilter filter;
//
//    @Autowired
//    private JWTTokenProvider jwtTokenProvider;
//
//    @MockBean
//    private AccountTypeRepository repository;

//    @BeforeEach
//    void setUp() {
////        given(repository.findAll()).willReturn(null);
//
//    }

    @org.junit.jupiter.api.Test
    void findAll() {
        System.out.println("HI");
//        String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";
//        Mockito.when(
//                service.findAll(Mockito.anyLong())).thenReturn(null);

//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/account-type/all")
//                .accept(MediaType.APPLICATION_JSON).content("1")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        System.out.println(result.getResponse());
//        String expected = "{id:Course1,name:Spring,description:10Steps}";
//
//        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}
//
//        JSONAssert.assertEquals(expected, result.getResponse()
//                .getContentAsString(), false);
    }
//
//    @Test
//    void findById() {
//    }
//
//    @Test
//    void add() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}
