package com.project.runningcrew.totalpost.repository;

public class PostTypeSqlQuery {

    public final static String FIND_ALL = "select * from ( " +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from boards as b " +
            "inner join members as m on m.member_id = b.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where m.crew_id = :crewId " +
            "and b.board_id not in " +
            "(select b2.board_id from boards as b2 " +
            "inner join blocked_info as bi on bi.blocked_member_id = b2.member_id and bi.deleted = false " +
            "where bi.member_id = :memberId) " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from running_notices as r " +
            "inner join members as m on m.member_id = r.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where m.crew_id = :crewId " +
            "and r.running_notice_id not in " +
            "(select r2.running_notice_id from running_notices as r2 " +
            "inner join blocked_info as bi on bi.blocked_member_id = r2.member_id and bi.deleted = false " +
            "where bi.member_id = :memberId) " +
            ") e " +
            "order by created_date desc " +
            "limit :size offset :number";

    public final static String FIND_ALL_BY_KEYWORD = "select * from (" +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from boards as b " +
            "inner join members as m on m.member_id = b.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where m.crew_id = :crewId and (b.title like :keyword or b.detail like :keyword) " +
            "and b.board_id not in " +
            "(select b2.board_id from boards as b2 " +
            "inner join blocked_info as bi on bi.blocked_member_id = b2.member_id and bi.deleted = false " +
            "where bi.member_id = :memberId) " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from running_notices as r " +
            "inner join members as m on m.member_id = r.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where m.crew_id = :crewId and (r.title like :keyword or r.detail like :keyword) " +
            "and r.running_notice_id not in " +
            "(select r2.running_notice_id from running_notices as r2 " +
            "inner join blocked_info as bi on bi.blocked_member_id = r2.member_id and bi.deleted = false " +
            "where bi.member_id = :memberId) " +
            ") e " +
            "order by created_date desc " +
            "limit :size offset :number";

    public final static String FIND_ALL_BY_MEMBER = "select * from (" +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from boards as b " +
            "inner join members as m on m.member_id = b.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where b.member_id = :memberId " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from running_notices as r " +
            "inner join members as m on m.member_id = r.member_id and m.deleted = false " +
            "inner join users as u on m.user_id = u.user_id and u.deleted = false " +
            "where r.member_id = :memberId) e " +
            "order by created_date desc " +
            "limit :size offset :number";


    public final static String FIND_ALL_BY_COMMENT_OF_MEMBER = "select * from (" +
            "select b.board_id as id, b.created_date as created_date, b.title as title, " +
            "u.nickname as nickname, 'board' as post_type " +
            "from comments as c " +
            "inner join members as m1 on m1.member_id = c.member_id and m1.deleted = false " +
            "inner join boards as b on b.board_id = c.board_id and b.deleted = false " +
            "inner join members as m2 on m2.member_id = b.member_id and m2.deleted = false " +
            "inner join users as u on u.user_id = m2.user_id and u.deleted = false " +
            "where c.member_id = :memberId " +
            "union " +
            "select r.running_notice_id as id, r.created_date as created_date, r.title as title, " +
            "u.nickname as nickname, 'runningNotice' as post_type " +
            "from comments as c " +
            "inner join members as m1 on m1.member_id = c.member_id and m1.deleted = false " +
            "inner join running_notices as r on r.running_notice_id = c.running_notice_id and r.deleted = false " +
            "inner join members as m2 on m2.member_id = r.member_id and m2.deleted = false " +
            "inner join users as u on u.user_id = m2.user_id and u.deleted = false " +
            "where c.member_id = :memberId) e " +
            "order by created_date desc " +
            "limit :size offset :number";

}
