package com.cocktailmasters.backend.vote;

import com.cocktailmasters.backend.vote.controller.VoteController;
import com.cocktailmasters.backend.vote.controller.dto.CreateVoteItemRequest;
import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.service.VoteService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@AutoConfigureWebMvc
public class voteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    VoteService voteService;

    @Test
    @DisplayName("투표 생성 테스트")
    void createVoteTest() throws Exception {
        List<CreateVoteItemRequest> createVoteItemRequests = new ArrayList<>();
        createVoteItemRequests.add(CreateVoteItemRequest.builder()
                .title("항목 제목")
                .imageUrl("항목 이미지 URL")
                .iframeLink("아이프레임 링크")
                .build());
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        CreateVoteRequest createVoteRequest = CreateVoteRequest.builder()
                .voteTitle("투표 제목")
                .voteContent("투표 내용")
                .voteImageUrl("투표 이미지 URL")
                .voteThumbnailUrl("투표 썸네일 URL")
                .voteEndDate(LocalDateTime.now().plusDays(3))
                .isPublic(false)
                .isAnonymous(false)
                .voteItems(createVoteItemRequests)
                .tags(tags)
                .build();

        given(voteService.createVote(1L, createVoteRequest)).willReturn(true);

        Gson gson = new Gson();
        String content = gson.toJson(createVoteRequest);
        mockMvc.perform(post("/api/v1/votes")
                        .content(String.valueOf(createVoteRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
