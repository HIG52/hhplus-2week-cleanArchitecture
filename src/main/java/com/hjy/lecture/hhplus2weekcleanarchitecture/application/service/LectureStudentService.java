package com.hjy.lecture.hhplus2weekcleanarchitecture.application.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.repository.LectureStudentRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
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

    public LectureResponseDTO applyLecture(LectureRequestDTO lectureRequestDTO) {
        LectureStudent lectureStudent = LectureStudent.createLectureStudent(
                lectureRequestDTO.getLectureId(),
                lectureRequestDTO.getUserId()
        );

        long currentStudentCount = lectureStudentRepository.countByLectureId_LectureId(lectureStudent.getLectureId().getLectureId());

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

        LectureResponseDTO lectureResponseDTO = new LectureResponseDTO();
        lectureResponseDTO.setLectureId(lectureStudent.getLectureId().getLectureId());
        lectureResponseDTO.setUserId(lectureStudent.getLectureId().getUserId());
        return lectureResponseDTO;
    }

}