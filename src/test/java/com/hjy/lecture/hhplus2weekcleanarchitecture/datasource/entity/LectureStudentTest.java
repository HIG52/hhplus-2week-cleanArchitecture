package com.hjy.lecture.hhplus2weekcleanarchitecture.datasource.entity;

import com.hjy.lecture.hhplus2weekcleanarchitecture.business.entity.LectureStudent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LectureStudentTest {

    @Test
    public void lectureId와_userId를_입력하면_lectureStudent를_반환한다(){
        //given
        long lectureId = 1L;
        long userId = 1L;

        //when
        LectureStudent lectureStudent = LectureStudent.createLectureStudent(lectureId, userId);

        //then
        assertThat(lectureStudent).isNotNull();
        assertThat(lectureStudent.getLectureId().getLectureId()).isEqualTo(lectureId);
        assertThat(lectureStudent.getLectureId().getUserId()).isEqualTo(userId);
    }

}
