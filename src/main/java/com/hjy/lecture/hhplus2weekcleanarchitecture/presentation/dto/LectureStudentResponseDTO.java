package com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureStudentResponseDTO {
    private long lectureId;
    private long userId;

}
