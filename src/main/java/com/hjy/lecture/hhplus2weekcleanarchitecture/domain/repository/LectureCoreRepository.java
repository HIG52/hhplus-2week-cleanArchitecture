package com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureCoreRepository {

    Optional<Lecture> findByIdWithLock(Long lectureId);

    List<Lecture> findLecturesStartingOnDateWithAvailableSeats(LocalDateTime lectureStartDateTime);

    List<Lecture> findLecturesAppliedByUserId(Long userId);

    Optional<LectureStudent> findByLectureId_LectureIdAndLectureId_UserId(Long lectureId, Long userId);

    LectureStudent saveLectureStudent(LectureStudent lectureStudent);

    int countByLectureId_LectureId(Long lectureId);
}
