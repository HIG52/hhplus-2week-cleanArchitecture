package com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.controller;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureStudentService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureRequestDTO;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LectureController {

    private final LectureStudentService lectureStudentService;

    @Autowired
    public LectureController(LectureStudentService lectureStudentService) {
        this.lectureStudentService = lectureStudentService;
    }

    @PostMapping("/api/v1/lectures/{lectureId}/apply")
    public ResponseEntity<LectureResponseDTO> applyLecture(@PathVariable Long lectureId, @RequestBody LectureRequestDTO lectureRequestDTO) {
        lectureRequestDTO.setLectureId(lectureId);
        return ResponseEntity.ok(lectureStudentService.applyLecture(lectureRequestDTO));
    }

}
