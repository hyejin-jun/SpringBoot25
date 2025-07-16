package org.mbc.board.repository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.Repository.BoardRepository;
import org.mbc.board.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest  // 메서드용 테스트
@Log4j2  // 로그용
public class BoardRepositoryTests {  // 영속성 계층에 테스트용

    @Autowired  // 생성자 자동 주입
    private BoardRepository boardRepository;

    @Test
    public void testInsert() { // 데이터베이스 데이터 주입 테스트 코드
        IntStream.rangeClosed(1, 100).forEach(i -> {  // i 변수에 1~99까지 100 개의 정수를 반복하여 생성
                    Board board=Board.builder()  // @Builder 사용
            .title("제목: " + i)
            .content("내용: " +i)
            .writer("유저: " +(i%10))
            .build();
           log.info((board));
            
            Board result=boardRepository.save(board);
            // .save 메서드는 jpa에서 상속한 메서드로 값을 저장
            // 이미 갓이 있으면 update를 진행

            log.info("게시물 번호 출력: " + result.getBno() + "게시물의 제목" + result.getTitle());
            
                }  // forEach 종료
        );  // IntStream 종료
    }  // testInsert() 종료
    @Test
    public void testSelect() {
        Long bno=100L;  // 100 번의 게시물 확인
        
        Optional<Board> result = boardRepository.findById(bno);  // 널값이 나올 경우를 대비한 객체
        //                                      .findById(bno): select * from board where bno = bno;
      /*  Hibernate:
        select
        b1_0.bno,
                b1_0.content,
                b1_0.maddate,
                b1_0.regdate,
                b1_0.title,
                b1_0.writer
        from
        board b1_0
        where
        b1_0.bno=?*/

        Board board=result.orElseThrow();  // 값이 있으면 넣어라
        
        log.info(bno + "가 데이터베이스에 존재합니다");
        log.info(board);  // Board(bno=100, title= 제목...100. content=내용...100, writer=user0)
    }  // testSelect() 종료
    
    @Test
    public void testUpdate() {

        Long bno = 99L;  // 100 번의 게시물 가지고 와서 수정 후 테스트

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();  // 값이 있으면 넣어라
        board.change("수정 테스트 제목" , "수정 테스트 내용");  // 제목과 내용만 수정할 수 있는 메서드
        boardRepository.save(board);  // .save(): PK 없으면 insert, PK 있으면 update
/*        Hibernate:
        update
                board
        set
        content=?,
        maddate=?,
        title=?,
        writer=?
        where
        bno=?*/
    }


    @Test
    public void testDelete() {
        Long bno = 2L;

        boardRepository.deleteById(bno);
/*        Hibernate:
        delete
                from
        board
                where
        bno=?*/
    }

    @Test
    public void testPaging() {
        // 1 page order by bno desc
        // .findAll(): 모든 리스트를 출력하는 메서드 (select * from board)
        // 전체 리스트에 페이징과 정렬 기법도 추가하기

        Pageable pageable = PageRequest.of(0, 10, Sort.by ("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);
        // 1 장의 종이에 board 객체가 가지고 있는 결과는 result에 담김
        // page 클래스는 다음 페이지 존재 여부, 이전 페이지 존재 여부, 전체 페이지 개수 등등 계산

/*        board b1_0
        order by
        b1_0.bno desc (bno 기준으로 내림차순 정렬)
        limit
                ?, ? (시작 번호, 페이지당 데이터 개수) 
          select
        count(b1_0.bno) (보드 전체 리스트 수를 알아옴)
         from
        board b1_0*/

        log.info("전체 게시물 수: " + result.getTotalElements());  // 198
        log.info("전체 페이지 수: " + result.getTotalPages());  // 20
        log.info("현재 페이지 번호: " + result.getNumber());  // 0
        log.info("페이지당 데이터 개수: " + result.getSize());  // 10
        log.info("다음 페이지 여부: " + result.hasNext());  // true
        log.info("시작 페이지 여부: " + result.isFirst());  // true

        // 콘솔에 결과 출력
        List<Board> boardList = result.getContent();  // 페이징된 내용을 가지고 와라
        boardList.forEach(board -> log.info(board));  
        // forEach는 인덱스를 사용하지 않고 앞에서부터 객체 리턴
        // board -> log.info(board): 람다식 1 개의 명령어가 있을 때 활용
    }

}  // 클래스 종료
