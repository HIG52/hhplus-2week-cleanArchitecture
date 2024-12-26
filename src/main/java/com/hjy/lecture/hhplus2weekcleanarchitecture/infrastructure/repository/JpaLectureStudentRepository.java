package com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaLectureStudentRepository extends JpaRepository<LectureStudent, LectureStudentId> {

    Optional<LectureStudent> findByLectureId_LectureIdAndLectureId_UserId(Long lectureId, Long userId);

    int countByLectureId_LectureId(Long lectureId);
}
