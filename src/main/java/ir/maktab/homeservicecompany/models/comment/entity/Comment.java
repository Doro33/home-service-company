package ir.maktab.homeservicecompany.models.comment.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@NotNull
public class Comment extends BaseEntity {
    @OneToOne
    private Request request;
    private Long rating;
    @Nullable
    private String description;

    public Comment(Request request, Long rating, String description) {
        this.request = request;
        setRating(rating);
        this.description = description;
    }

    public static CommentBuilder builder(Request request,Long rating) {
        return new CommentBuilder(request,rating);
    }

    public void setRating(Long rating) {
        if (rating < 1 || 5 < rating)
            throw new IllegalArgumentException("rating must be an int between 1 -5");
        this.rating = rating;
    }

    public static class CommentBuilder {
        private final Request request;
        private Long rating;
        private String description;

        CommentBuilder(Request request, Long rating) {
            this.request=request;
            setRating(rating);
        }

        private void setRating(Long rating) {
            if (rating < 1 || 5 < rating)
                throw new IllegalArgumentException("rating must be an int between 1 -5");
            this.rating = rating;
        }

        public CommentBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Comment build() {
            return new Comment(request, rating, description);
        }

        public String toString() {
            return "Comment.CommentBuilder(request=" + this.request + ", rating=" + this.rating + ", description=" + this.description + ")";
        }
    }
}
