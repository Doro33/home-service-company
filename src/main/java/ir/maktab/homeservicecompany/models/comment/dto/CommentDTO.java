package ir.maktab.homeservicecompany.models.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDTO {
    private Long clientId;
    private Long requestId;
    private Long rating;
    private String description;
}
