package com.hjy.lecture.hhplus2weekcleanarchitecture;

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
        assertThat(lectureStudent.getLectureId()).isEqualTo(lectureId);
        assertThat(lectureStudent.getUserId()).isEqualTo(userId);
    }

}
