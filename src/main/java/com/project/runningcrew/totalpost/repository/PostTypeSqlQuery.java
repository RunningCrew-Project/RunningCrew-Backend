package com.project.runningcrew.totalpost.repository;

public class PostTypeSqlQuery {

    public final static String GET_KEYWORD = "select * from (" +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from boards as b " +
            "inner join members as m on m.member_id = b.member_id " +
            "inner join users as u on m.user_id = u.user_id " +
            "where b.title like :keyword or b.detail like :keyword " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from running_notices as r " +
            "inner join members as m on m.member_id = r.member_id " +
            "inner join users as u on m.user_id = u.user_id " +
            "where r.title like :keyword or r.detail like :keyword) e " +
            "order by created_date desc " +
            "limit :size offset :number";

    public final static String GET_MEMBER = "select * from (" +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from boards as b " +
            "inner join members as m on m.member_id = b.member_id " +
            "inner join users as u on m.user_id = u.user_id " +
            "where b.member_id = :memberId " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from running_notices as r " +
            "inner join members as m on m.member_id = r.member_id " +
            "inner join users as u on m.user_id = u.user_id " +
            "where r.member_id = :memberId) e " +
            "order by created_date desc " +
            "limit :size offset :number";

}
