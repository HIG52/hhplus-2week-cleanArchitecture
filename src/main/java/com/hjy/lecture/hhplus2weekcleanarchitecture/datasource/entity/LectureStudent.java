package com.hjy.lecture.hhplus2weekcleanarchitecture.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "lecture_student")
public class LectureStudent { // 강의 수강 신청 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_student_id")
    private Long lectureStudentId;

    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    // JPA에서 사용하는 기본 생성자
    protected LectureStudent() {

    }

    // 도메인 로직을 위한 생성자
    private LectureStudent(Long lectureId, Long userId, LocalDateTime appliedAt) {
        this.lectureId = lectureId;
        this.userId = userId;
        this.appliedAt = appliedAt;
        this.createdAt = LocalDateTime.now();
    }

    public static LectureStudent createLectureStudent(Long lectureId, Long userId) {
        // 현재 시간을 appliedAt으로 설정
        LocalDateTime now = LocalDateTime.now();
        return new LectureStudent(lectureId, userId, now);
    }



}
