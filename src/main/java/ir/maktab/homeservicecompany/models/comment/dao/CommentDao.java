package ir.maktab.homeservicecompany.models.comment.dao;

import ir.maktab.homeservicecompany.models.comment.entity.Comment;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends JpaRepository<Comment,Long> {
    Boolean existsByRequest(Request request);
}