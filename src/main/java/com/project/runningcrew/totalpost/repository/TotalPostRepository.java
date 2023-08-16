package com.project.runningcrew.totalpost.repository;

import com.project.runningcrew.comment.entity.Comment;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.totalpost.entity.PostType;
import com.project.runningcrew.totalpost.entity.TotalPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TotalPostRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TotalPost mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TotalPost(
                rs.getLong("id"),
                rs.getTimestamp("created_date").toLocalDateTime(),
                rs.getString("title"),
                rs.getString("nickname"),
                PostType.getPostType(rs.getString("post_type")));
    }


    /**
     * 모든 게시글 또는 런닝공지를 페이징하여 반환한다.
     *
     * @param crew 크루 정보
     * @param pageable 페이징 정보
     * @param member 정보를 조회할 멤버정보
     * @return 모든 게시글 또는 런닝공지
     */
    public Slice<TotalPost> getTotalPost(Crew crew, Member member, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Map<String, Object> params = Map.of(
                "crewId", crew.getId(),
                "memberId", member.getId(),
                "number", page * size,
                "size", size + 1);

        List<TotalPost> content = namedParameterJdbcTemplate
                .query(PostTypeSqlQuery.FIND_ALL, params, this::mapRow);
        boolean hasNext = content.size() == size + 1;
        if (hasNext) {
            content = content.subList(0, size);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * keyword 가 title 이나 detail 에 포함된 모든 게시글 또는 런닝공지를 페이징하여 반환한다.
     *
     * @param crew 크루 정보
     * @param keyword 검색어
     * @param member 정보를 조회할 멤버 정보
     * @param pageable 페이징 정보
     * @return keyword 가 title 이나 detail 에 포함된 모든 게시글 또는 런닝공지
     */
    public Slice<TotalPost> getTotalPostByKeyword(Crew crew, String keyword, Member member, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        String wrappedKeyword = "%" + keyword + "%";
        Map<String, Object> params = Map.of(
                "crewId", crew.getId(),
                "memberId", member.getId(),
                "keyword", wrappedKeyword,
                "number", page * size,
                "size", size + 1);

        List<TotalPost> content = namedParameterJdbcTemplate
                .query(PostTypeSqlQuery.FIND_ALL_BY_KEYWORD, params, this::mapRow);
        boolean hasNext = content.size() == size + 1;
        if (hasNext) {
            content = content.subList(0, size);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    /**
     * member 가 작성한 모든 게시글 또는 런닝공지를 페이징하여 반환한다.
     *
     * @param member   글을 작성한 Member
     * @param pageable 페이징 정보
     * @return memberId 에 해당하는 member 가 작성한 모든 게시글 또는 런닝공지
     */
    public Slice<TotalPost> getTotalPostByMember(Member member, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Map<String, Object> params = Map.of(
                "memberId", member.getId(),
                "number", page * size,
                "size", size + 1);

        List<TotalPost> content = namedParameterJdbcTemplate
                .query(PostTypeSqlQuery.FIND_ALL_BY_MEMBER, params, this::mapRow);
        boolean hasNext = content.size() == size + 1;
        if (hasNext) {
            content = content.subList(0, size);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }


    /**
     * member 가 작성한 댓글이 있는 모든 게시글 또는 런닝공지를 페이징하여 반환한다.
     *
     * @param member 댓글을 작성한 Member
     * @param pageable 페이징 정보
     * @return memberId 에 해당하는 member 가 작성한 댓글이 있는 모든 게시글 또는 런닝공지
     */
    public Slice<TotalPost> getTotalPostByCommentOfMember(Member member, Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Map<String, Object> params = Map.of(
                "memberId", member.getId(),
                "number", page * size,
                "size", size + 1);

        List<TotalPost> content = namedParameterJdbcTemplate
                .query(PostTypeSqlQuery.FIND_ALL_BY_COMMENT_OF_MEMBER, params, this::mapRow);

        boolean hasNext = content.size() == size + 1;
        if(hasNext) {
            content = content.subList(0, size);
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

}
