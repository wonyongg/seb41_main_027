package main027.server.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import main027.server.domain.audit.BaseTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PlaceDto extends BaseTime {

    @Data
    @AllArgsConstructor
    public static class PlacePostDto {
        @NotNull
        private Long memberId;
        @NotBlank
        private String name;
        @NotBlank
        private String address;
        @NotBlank
        private String description;
    }

    @Data
    @AllArgsConstructor
    public static class PlacePatchDto {
                @NotBlank
        private String description;

    }

    @Data
    @AllArgsConstructor
    public static class PlaceResponseDto {
        private Long placeId;
        private String name;
        private String address;
        private String description;
        private String category;
        private Long likeCount;
        private Long latitude;
        private Long longitude;
    }
}