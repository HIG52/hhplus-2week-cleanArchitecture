package com.hjy.lecture.hhplus2weekcleanarchitecture.application.validator;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository.LectureCoreRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LectureStudentValidator {

    private final LectureCoreRepository lectureCoreRepository;

    public LectureStudentValidator(LectureCoreRepository lectureCoreRepository) {
        this.lectureCoreRepository = lectureCoreRepository;
    }

    public void validateLectureCapacity(Long lectureId, int maxStudents) {
        int currentStudentCount = lectureCoreRepository.countByLectureId_LectureId(lectureId);
        if (currentStudentCount >= maxStudents) {
            throw new IllegalStateException("해당 강의는 정원이 초과되었습니다.");
        }
    }

    public void validateDuplicateApplication(LectureStudent lectureStudent) {
        Optional<LectureStudent> findLectureStudent = lectureCoreRepository.findByLectureId_LectureIdAndLectureId_UserId(
                lectureStudent.getLectureId().getLectureId(),
                lectureStudent.getLectureId().getUserId()
        );

        if (findLectureStudent.isPresent()) {
            throw new IllegalArgumentException("이미 수강신청한 강의입니다.");
        }
    }

}
