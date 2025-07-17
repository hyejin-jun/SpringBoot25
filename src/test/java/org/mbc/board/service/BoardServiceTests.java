package org.mbc.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info("등록용 테스트 서비스 실행 중..................");
        log.info(boardService.getClass().getName());  // 객체 생성용 테스트

        BoardDTO boardDTO = BoardDTO.builder()
                .title("서비스에서 만든 제목")
                .content("서비스에서 만든 내용")
                .writer("서비스님")
                .build();  // 세터 대신 @Builder

        Long bno = boardService.register(boardDTO);

        log.info("테스트 결과 bno: " + bno);

    /*    insert
                into
        board
                (content, moddate, regdate, title, writer)
        values
                (?, ?, ?, ?, ?)
        2025-07-17T16:14:04.284+09:00  INFO 2692 --- [board] [    Test worker] o.mbc.board.service.BoardServiceTests    : 테스트 결과 bno: 101*/


    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("101이 바뀐드아아아아")
                .content("바꾼드아아아아아")
                .build();
        boardService.modify(boardDTO);  // 프론트에서 객체가 넘어가 수정되었는지 테스트

    }

    @Test
    public void testDelect(){
            long bno = 101L;
        boardService.remove(bno);  // 프론트에서 객체가 넘어가 수정되었는지 테스트

    }
}

