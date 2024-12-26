package com.hjy.lecture.hhplus2weekcleanarchitecture.application.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository.LectureCoreRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LectureService {

    private final LectureCoreRepository lectureCoreRepository;

    @Autowired
    public LectureService(LectureCoreRepository lectureCoreRepository) {
        this.lectureCoreRepository = lectureCoreRepository;
    }

    public List<LectureResponseDTO> getAvailableLectures(LocalDateTime lectureStartDate) {

        List<Lecture> lectureList = lectureCoreRepository.findLecturesStartingOnDateWithAvailableSeats(lectureStartDate);

        if (lectureList.isEmpty()) {
            throw new IllegalArgumentException("해당 날짜에 신청가능한 특강이 존재하지 않습니다.");
        }

        return lectureList.stream()
                .map(lecture -> new LectureResponseDTO(
                        lecture.getLectureId(),
                        lecture.getLectureTitle(),
                        lecture.getLecturerName(),
                        lecture.getLectureStartDateTime(),
                        lecture.getLectureEndDateTime()
                ))
                .toList();
    }

    public List<LectureResponseDTO> getAvailabledLectures(Long userId) {

            List<Lecture> lectureList = lectureCoreRepository.findLecturesAppliedByUserId(userId);

            if (lectureList.isEmpty()) {
                throw new IllegalArgumentException("신청한 특강이 존재하지 않습니다.");
            }

            return lectureList.stream()
                    .map(lecture -> new LectureResponseDTO(
                            lecture.getLectureId(),
                            lecture.getLectureTitle(),
                            lecture.getLecturerName(),
                            lecture.getLectureStartDateTime(),
                            lecture.getLectureEndDateTime()
                    ))
                    .toList();
    }
}
