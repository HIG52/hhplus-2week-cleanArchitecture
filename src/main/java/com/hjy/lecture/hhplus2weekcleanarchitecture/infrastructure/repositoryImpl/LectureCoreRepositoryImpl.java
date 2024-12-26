package com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repositoryImpl;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository.LectureCoreRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.JpaLectureRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.JpaLectureStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LectureCoreRepositoryImpl implements LectureCoreRepository {

    private final JpaLectureRepository jpaLectureRepository;
    private final JpaLectureStudentRepository jpaLectureStudentRepository;

    @Autowired
    public LectureCoreRepositoryImpl(JpaLectureRepository jpaLectureRepository, JpaLectureStudentRepository jpaLectureStudentRepository) {
        this.jpaLectureRepository = jpaLectureRepository;
        this.jpaLectureStudentRepository = jpaLectureStudentRepository;
    }

    @Override
    public Optional<Lecture> findByIdWithLock(Long lectureId) {
        return jpaLectureRepository.findByIdWithLock(lectureId);
    }

    @Override
    public List<Lecture> findLecturesStartingOnDateWithAvailableSeats(LocalDateTime lectureStartDateTime) {
        return jpaLectureRepository.findLecturesStartingOnDateWithAvailableSeats(lectureStartDateTime);
    }

    @Override
    public List<Lecture> findLecturesAppliedByUserId(Long userId) {
        return jpaLectureRepository.findLecturesAppliedByUserId(userId);
    }

    @Override
    public Optional<LectureStudent> findByLectureId_LectureIdAndLectureId_UserId(Long lectureId, Long userId) {
        return jpaLectureStudentRepository.findByLectureId_LectureIdAndLectureId_UserId(lectureId, userId);
    }

    @Override
    public LectureStudent saveLectureStudent(LectureStudent lectureStudent) {
        return jpaLectureStudentRepository.save(lectureStudent);
    }

    @Override
    public int countByLectureId_LectureId(Long lectureId) {
        return jpaLectureStudentRepository.countByLectureId_LectureId(lectureId);
    }

}
