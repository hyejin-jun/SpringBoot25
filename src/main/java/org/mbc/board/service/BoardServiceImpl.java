package org.mbc.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Board;
import org.mbc.board.dto.BoardDTO;
import org.mbc.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service  // 스프링에게 서비스 계층임을 알림
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{
    private final ModelMapper modelMapper; // 엔티티와 DTO를 변환
    private  final BoardRepository boardRepository;  // jpa용 클래스 (CRUD, 페이징, 정렬, 다중 검색)

    @Override
    public Long register(BoardDTO boardDTO) {  // 조원이 실행 코드 생성
        // 폼에서 넘어온 DTO가 데이터베이스에 기록되어야 함
        Board board = modelMapper.map(boardDTO, Board.class);  // 엔티티가 DTO로 변환
        Long bno = boardRepository.save(board).getBno();
        //                    insert into board ~~~~~ ->  bno로 받음
        return bno;  // 프론트에 게시물 저장 후 번호가 전달


    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        // select * from board where bno = bno / Optional null이 나와도 예외 처리 하지 않음
        Board board = result.orElseThrow();  // 정상값이 나오면 엔티티로 받음
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // 모델 메퍼를 이용하여 엔티티로 나온 보드를 dto로 변환
        return boardDTO;  // 프론트에 dto를 보냄
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // select * from board where bno = bno >> 엔티티로 나옴
        Board board = result.orElseThrow();  // 성공시(null 아님) 결과를 엔티티로 저장
        board.change(boardDTO.getTitle(), boardDTO.getContent()); // 제목과 내용이 수정
        boardRepository.save(board); // 데이터베이스에 Pk가 있으면 업데이트, 없으면 보류
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);  // delete from board where bno = bno
    }
}
