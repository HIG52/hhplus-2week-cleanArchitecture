package com.hjy.lecture.hhplus2weekcleanarchitecture.application.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureStudentRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LectureStudentService {

    private final LectureStudentRepository lectureStudentRepository;
    private final LectureRepository lectureRepository;

    public static final int MAX_STUDENTS = 30;

    @Autowired
    public LectureStudentService(LectureStudentRepository lectureStudentRepository, LectureRepository lectureRepository) {
        this.lectureStudentRepository = lectureStudentRepository;
        this.lectureRepository = lectureRepository;
    }

    @Transactional
    public LectureStudentResponseDTO applyLecture(LectureStudentRequestDTO lectureStudentRequestDTO) {
        LectureStudent lectureStudent = LectureStudent.createLectureStudent(
                lectureStudentRequestDTO.getLectureId(),
                lectureStudentRequestDTO.getUserId()
        );
        // 비관적 락을 사용하여 강의 데이터를 잠금
        Lecture lecture = lectureRepository.findByIdWithLock(lectureStudentRequestDTO.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

        int currentStudentCount = lectureStudentRepository.countByLectureId_LectureId(lecture.getLectureId());

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

        try {
            lectureStudentRepository.save(lectureStudent);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 등록된 수강신청 정보입니다.", e);
        }

        LectureStudentResponseDTO lectureStudentResponseDTO = new LectureStudentResponseDTO();
        lectureStudentResponseDTO.setLectureId(lectureStudent.getLectureId().getLectureId());
        lectureStudentResponseDTO.setUserId(lectureStudent.getLectureId().getUserId());
        return lectureStudentResponseDTO;
    }

}
