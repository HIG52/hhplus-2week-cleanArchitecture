package com.hjy.lecture.hhplus2weekcleanarchitecture.business.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    public static final int MAX_STUDENT = 30;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    private LectureService lectureService;

    // Mock 데이터를 생성하는 정적 메서드
    private static List<Lecture> LectureList() {
        Lecture lecture1 = Lecture.createLecture(
                1L,
                "강의1",
                "홍길일",
                LocalDateTime.of(2024, 12, 29, 11, 0),
                LocalDateTime.of(2024, 12, 29, 13, 0)
        );

        Lecture lecture2 = Lecture.createLecture(
                2L,
                "강의2",
                "홍길이",
                LocalDateTime.of(2024, 12, 30, 14, 0),
                LocalDateTime.of(2024, 12, 30, 16, 0)
        );

        return List.of(lecture1, lecture2);
    }

    private static Map<Long, Integer> LectureStudentCounts() {
        Map<Long, Integer> studentCounts = new HashMap<>();
        studentCounts.put(1L, 25); // 강의1: 25명 수강
        studentCounts.put(2L, 35); // 강의2: 35명 수강 (30명 초과)
        return studentCounts;
    }

    @Test
    public void 날짜를_입력했을때_해당날짜에_해당하는_강의리스트만_반환한다() {
        // given
        LocalDateTime lectureStartDateTime = LocalDateTime.of(2024, 12, 29, 0, 0);

        List<Lecture> lectureList = LectureList();

        given(lectureRepository.findLecturesStartingOnDateWithAvailableSeats(lectureStartDateTime))
                .willReturn(lectureList);

        // when
        List<LectureResponseDTO> resultList = lectureService.getAvailableLectures(lectureStartDateTime);

        // then
        assertThat(resultList)
                .extracting("lectureId", "lectureTitle", "lecturerName", "lectureStartDateTime", "lectureEndDateTime")
                .contains(
                        tuple(1L, "강의1", "홍길일", LocalDateTime.of(2024, 12, 29, 11, 0), LocalDateTime.of(2024, 12, 29, 13, 0))
                );

    }

    @Test
    public void 수강생_수가_30명_이하인_강의만_반환한다() {
        // given
        LocalDateTime lectureStartDateTime = LocalDateTime.of(2024, 12, 29, 0, 0);

        List<Lecture> lectureList = LectureList();
        Map<Long, Integer> studentCounts = LectureStudentCounts();

        given(lectureRepository.findLecturesStartingOnDateWithAvailableSeats(lectureStartDateTime))
                .willAnswer(invocation -> {
                    // 강의 목록에서 30명 이하 조건을 필터링
                    return lectureList.stream()
                            .filter(lecture -> studentCounts.getOrDefault(lecture.getLectureId(), 0) <= 30)
                            .toList();
                });

        // when
        List<LectureResponseDTO> resultList = lectureService.getAvailableLectures(lectureStartDateTime);

        // then
        assertThat(resultList)
                .extracting("lectureId", "lectureTitle", "lecturerName", "lectureStartDateTime", "lectureEndDateTime")
                .contains(
                        tuple(1L, "강의1", "홍길일", LocalDateTime.of(2024, 12, 29, 11, 0), LocalDateTime.of(2024, 12, 29, 13, 0))
                )
                .doesNotContain(
                        tuple(2L, "강의2", "홍길이", LocalDateTime.of(2024, 12, 30, 14, 0), LocalDateTime.of(2024, 12, 30, 16, 0))
                );
    }

    @Test
    public void 입력받은lectureStartDate에_강의가_없으면_IllegalArgumentException을던진다() {
        // given
        LocalDateTime lectureStartDateTime = LocalDateTime.of(2024, 12, 29, 0, 0);

        // findByLectureIdAndUserId가 Optional로 값을 반환하도록 Mock 설정
        given(lectureRepository.findLecturesStartingOnDateWithAvailableSeats(lectureStartDateTime))
                .willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> lectureService.getAvailableLectures(lectureStartDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜에 신청가능한 특강이 존재하지 않습니다.");

        // verify (Repository 메서드가 호출되었는지 확인)
        verify(lectureRepository).findLecturesStartingOnDateWithAvailableSeats(lectureStartDateTime);
    }

}
