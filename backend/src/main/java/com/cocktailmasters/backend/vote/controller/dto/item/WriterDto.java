package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterDto {

    private Long id;
    private String nickname;
    private String badgeImageUrl;

    public static WriterDto createWriterDto(User writer) {
        return WriterDto.builder()
                .id(writer.getId())
                .nickname(writer.getNickname())
                .badgeImageUrl(writer.getEquippedBadgeImageUrl())
                .build();
    }
}
