package com.hjy.lecture.hhplus2weekcleanarchitecture.business.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureStudentService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.datasource.repository.LectureStudentRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.business.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureStudentServiceTest {

    @Mock
    private LectureStudentRepository lectureStudentRepository;

    @InjectMocks
    private LectureStudentService lectureStudentService;

    @Test
    public void applyLecture_lectureId와_userId를_입력받으면_LectureResponseDTO를_반환한다(){
        // given
        long lectureId = 1L;
        long userId = 1L;
        LectureStudent lectureStudent = LectureStudent.createLectureStudent(lectureId, userId);

        given(lectureStudentRepository.save(any(LectureStudent.class))).willReturn(lectureStudent);

        // when
        LectureResponseDTO lectureResponseDTO = lectureStudentService.applyLecture(new LectureRequestDTO(lectureId, userId));

        // then
        assertThat(lectureResponseDTO.getLectureId()).isEqualTo(lectureId);
        assertThat(lectureResponseDTO.getUserId()).isEqualTo(userId);

    }

    @Test
    public void applyLecture_lectureId와_userId를_입력받으면_이미수강신청한강의일때_IllegalArgumentException을던진다() {
        // given
        long lectureId = 1L;
        long userId = 1L;

        LectureRequestDTO lectureRequestDTO = new LectureRequestDTO();
        lectureRequestDTO.setLectureId(lectureId);
        lectureRequestDTO.setUserId(userId);

        LectureStudent existingLectureStudent = LectureStudent.createLectureStudent(lectureId, userId);

        // findByLectureIdAndUserId가 Optional로 값을 반환하도록 Mock 설정
        given(lectureStudentRepository.findByLectureId_LectureIdAndLectureId_UserId(lectureId, userId))
                .willReturn(Optional.of(existingLectureStudent));

        // when & then
        assertThatThrownBy(() -> lectureStudentService.applyLecture(lectureRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 수강신청한 강의입니다.");

        // verify (Repository 메서드가 호출되었는지 확인)
        verify(lectureStudentRepository).findByLectureId_LectureIdAndLectureId_UserId(lectureId, userId);
    }

    @Test
    public void applyLecture_lectureId와_userId를_입력받으면_강의_하나에_대하여_30명이_됐을경우_IllegalArgumentException을던진다() {
        // given
        long lectureId = 1L;
        long userId = 1L;

        LectureRequestDTO lectureRequestDTO = new LectureRequestDTO();
        lectureRequestDTO.setLectureId(lectureId);
        lectureRequestDTO.setUserId(userId);

        // findByLectureIdAndUserId가 Optional로 값을 반환하도록 Mock 설정
        given(lectureStudentRepository.countByLectureId_LectureId(lectureId))
                .willReturn(30L);

        // when & then
        assertThatThrownBy(() -> lectureStudentService.applyLecture(lectureRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 강의는 정원이 초과되었습니다.");

        // verify (Repository 메서드가 호출되었는지 확인)
        verify(lectureStudentRepository).countByLectureId_LectureId(lectureId);
    }
}
