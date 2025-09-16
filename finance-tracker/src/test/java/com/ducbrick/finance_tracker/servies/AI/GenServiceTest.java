package com.ducbrick.finance_tracker.servies.AI;


import com.ducbrick.finance_tracker.services.chat.GenClient;
import com.ducbrick.finance_tracker.services.chat.GenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenServiceTest{

    @Mock
    private GenClient genClient;

    @InjectMocks
    private GenService genService;

    @Test
    public void testGenService() {
        String question = "How much money I user last year?";
        String expectedResponse = "I use 1000 dollars";
        when(genClient.getResponse(question)).thenReturn("I use 1000 dollars");
        String response = genService.getResponse(question);
        assertEquals(expectedResponse, response);
    }
}
