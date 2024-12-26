package com.hjy.lecture.hhplus2weekcleanarchitecture.application.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.validator.LectureStudentValidator;
import com.hjy.lecture.hhplus2weekcleanarchitecture.config.LectureConfig;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository.LectureCoreRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureStudentService {

    private final LectureCoreRepository lectureCoreRepository;
    private final LectureStudentValidator lectureStudentValidator;
    private final LectureConfig lectureConfig;


    @Autowired
    public LectureStudentService(LectureCoreRepository lectureCoreRepository, LectureStudentValidator lectureStudentValidator, LectureConfig lectureConfig) {
        this.lectureCoreRepository = lectureCoreRepository;
        this.lectureStudentValidator = lectureStudentValidator;
        this.lectureConfig = lectureConfig;
    }

    @Transactional
    public LectureStudentResponseDTO applyLecture(LectureStudentRequestDTO lectureStudentRequestDTO) {

        LectureStudent lectureStudent = LectureStudent.createLectureStudent(
                lectureStudentRequestDTO.getLectureId(),
                lectureStudentRequestDTO.getUserId()
        );

        Lecture lecture = lectureCoreRepository.findByIdWithLock(lectureStudentRequestDTO.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

        lectureStudentValidator.validateLectureCapacity(lecture.getLectureId(), lectureConfig.getMaxStudents());
        lectureStudentValidator.validateDuplicateApplication(lectureStudent);

        try {
            LectureStudent resultLectureStudent = lectureCoreRepository.saveLectureStudent(lectureStudent);

            LectureStudentResponseDTO lectureStudentResponseDTO = new LectureStudentResponseDTO();
            lectureStudentResponseDTO.setLectureId(resultLectureStudent.getLectureId().getLectureId());
            lectureStudentResponseDTO.setUserId(resultLectureStudent.getLectureId().getUserId());
            return lectureStudentResponseDTO;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 등록된 수강신청 정보입니다.", e);
        }
    }

}
