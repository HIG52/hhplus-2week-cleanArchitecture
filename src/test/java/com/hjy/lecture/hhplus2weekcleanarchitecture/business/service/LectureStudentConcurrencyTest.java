package com.hjy.lecture.hhplus2weekcleanarchitecture.business.service;

import com.hjy.lecture.hhplus2weekcleanarchitecture.application.service.LectureStudentService;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository.LectureStudentRepository;
import com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto.LectureStudentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:test-data.sql") // 테스트 데이터 삽입 스크립트
public class LectureStudentConcurrencyTest {

    @Autowired
    private LectureStudentService lectureStudentService;

    @Autowired
    private LectureStudentRepository lectureStudentRepository;

    @Test
    public void 한유저가_동시에_신청하였을경우_최초1회만_저장된다() throws InterruptedException {
        // given
        long lectureId = 1L;
        long userId = 1L;
        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        LectureStudentRequestDTO requestDTO = new LectureStudentRequestDTO();
        requestDTO.setLectureId(lectureId);
        requestDTO.setUserId(userId);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lectureStudentService.applyLecture(requestDTO);
                } catch (Exception ignored) {
                    // 중복 요청이나 정원 초과 예외를 무시
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 종료될 때까지 대기
        executorService.shutdown();

        // then
        int actualCount = lectureStudentRepository.countByLectureId_LectureId(lectureId);
        assertThat(actualCount).isEqualTo(1); // 단 하나의 신청만 저장되어야 함
    }

    @Test
    public void 서로_다른_40명의_유저가_동시에_신청할_경우_선착순_30명만_저장된다() throws InterruptedException {
        // given
        long lectureId = 1L;
        int threadCount = 40;
        int maxStudents = 30;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1; // 각 요청마다 다른 사용자 ID 설정
            executorService.submit(() -> {
                try {
                    LectureStudentRequestDTO requestDTO = new LectureStudentRequestDTO();
                    requestDTO.setLectureId(lectureId);
                    requestDTO.setUserId(userId);

                    lectureStudentService.applyLecture(requestDTO);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 종료될 때까지 대기
        executorService.shutdown();

        // then
        int actualCount = lectureStudentRepository.countByLectureId_LectureId(lectureId);
        assertThat(actualCount).isEqualTo(maxStudents); // 최대 30명의 사용자만 저장
        assertThat(successCount.get()).isEqualTo(maxStudents); // 성공한 요청 수
        assertThat(failCount.get()).isEqualTo(threadCount - maxStudents); // 실패한 요청 수
    }

}
