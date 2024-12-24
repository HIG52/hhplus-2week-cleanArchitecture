package com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable                //2번
@AllArgsConstructor		//전체생성자
@NoArgsConstructor        //기본 생성자
@EqualsAndHashCode        //equals, hashCode
@Data
public class LectureStudentId implements Serializable { //1번

    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
