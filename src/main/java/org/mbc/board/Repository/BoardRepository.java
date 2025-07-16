package org.mbc.board.Repository;

import org.mbc.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // extends JpaRepository <엔티티클래스, PK 타입>
    // JpaRepository: jpa에서 미리 만들어 놓은 인터페이스로 crud와 페이지 처리, 정렬 등이 존재
}
