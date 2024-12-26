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
public class LectureResponseDTO {
    private long lectureId;
    private String lectureTitle;
    private String lecturerName;
    private LocalDateTime lectureStartDateTime;
    private LocalDateTime lectureEndDateTime;

}
