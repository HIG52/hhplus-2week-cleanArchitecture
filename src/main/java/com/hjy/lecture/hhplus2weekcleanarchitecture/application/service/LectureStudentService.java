package com.hjy.lecture.hhplus2weekcleanarchitecture.application.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureStudentRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LectureStudentService {

    private final LectureStudentRepository lectureStudentRepository;

    public static final int MAX_STUDENTS = 30;

    @Autowired
    public LectureStudentService(LectureStudentRepository lectureStudentRepository) {
        this.lectureStudentRepository = lectureStudentRepository;
    }

    public LectureStudentResponseDTO applyLecture(LectureStudentRequestDTO lectureStudentRequestDTO) {
        LectureStudent lectureStudent = LectureStudent.createLectureStudent(
                lectureStudentRequestDTO.getLectureId(),
                lectureStudentRequestDTO.getUserId()
        );

        int currentStudentCount = lectureStudentRepository.countByLectureId_LectureId(lectureStudent.getLectureId().getLectureId());

        if (currentStudentCount >= MAX_STUDENTS) {
            throw new IllegalStateException("해당 강의는 정원이 초과되었습니다.");
        }

        Optional<LectureStudent> findLectureStudent = lectureStudentRepository.findByLectureId_LectureIdAndLectureId_UserId(
                lectureStudent.getLectureId().getLectureId(),
                lectureStudent.getLectureId().getUserId()
        );

        if (findLectureStudent.isPresent()) {
            throw new IllegalArgumentException("이미 수강신청한 강의입니다.");
        }

        lectureStudentRepository.save(lectureStudent);

        LectureStudentResponseDTO lectureStudentResponseDTO = new LectureStudentResponseDTO();
        lectureStudentResponseDTO.setLectureId(lectureStudent.getLectureId().getLectureId());
        lectureStudentResponseDTO.setUserId(lectureStudent.getLectureId().getUserId());
        return lectureStudentResponseDTO;
    }

}
