package ir.maktab.homeservicecompany.models.comment.dto;

import lombok.Getter;

@Getter
public class CommentDTO {
    private Long clientId;
    private Long requestId;
    private Long rating;
    private String description;
}
