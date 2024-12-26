package com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Table(name = "lecture")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    @Column(name = "lecture_title", nullable = false)
    private String lectureTitle;

    @Column(name = "lecturer_name", nullable = false)
    private String lecturerName;

    @Column(name = "lecture_start_datetime", nullable = false)
    private LocalDateTime lectureStartDateTime;

    @Column(name = "lecture_end_datetime", nullable = false)
    private LocalDateTime lectureEndDateTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    // JPA에서 사용하는 기본 생성자
    protected Lecture() {

    }

    // 도메인 로직을 위한 생성자
    private Lecture(Long lectureId, String lecture_title, String lecturer_name, LocalDateTime lecture_start_date_time, LocalDateTime lecture_end_date_time) {
        this.lectureId = lectureId;
        this.lectureTitle = lecture_title;
        this.lecturerName = lecturer_name;
        this.lectureStartDateTime = lecture_start_date_time;
        this.lectureEndDateTime = lecture_end_date_time;
    }

    public static Lecture createLecture(Long lectureId, String lectureTitle, String lecturerName, LocalDateTime lectureStartDateTime, LocalDateTime lectureEndDateTime) {
        return new Lecture(lectureId, lectureTitle, lecturerName, lectureStartDateTime, lectureEndDateTime);
    }
}
