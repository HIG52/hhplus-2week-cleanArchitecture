package com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.controller;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureStudentService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class LectureController {

    private final LectureStudentService lectureStudentService;

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureStudentService lectureStudentService, LectureService lectureService) {
        this.lectureStudentService = lectureStudentService;
        this.lectureService = lectureService;
    }

    @PostMapping("/api/v1/lectures/{lectureId}/apply")
    public ResponseEntity<LectureStudentResponseDTO> applyLecture(
            @PathVariable Long lectureId, @RequestBody LectureStudentRequestDTO lectureStudentRequestDTO) {
        lectureStudentRequestDTO.setLectureId(lectureId);
        return ResponseEntity.ok(lectureStudentService.applyLecture(lectureStudentRequestDTO));
    }

    @GetMapping("/api/v1/lectures/available/{lectureStartDate}")
    public ResponseEntity<List<LectureResponseDTO>> getAvailableLectures(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate lectureStartDate) {
        // 자정으로 변환하여 LocalDateTime 생성
        LocalDateTime startDateTime = lectureStartDate.atStartOfDay();
        return ResponseEntity.ok(lectureService.getAvailableLectures(startDateTime));
    }

    @GetMapping("/api/v1/users/{userId}/lectures/applied")
    public ResponseEntity<List<LectureResponseDTO>> getAvailabledLectures(
            @PathVariable Long userId) {
        return ResponseEntity.ok(lectureService.getAvailabledLectures(userId));
    }



}
