package org.mbc.board.repository;

import org.mbc.board.domain.Board;
import org.mbc.board.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {  // 다중검색용 , BoardSearch 추가 p451


    //  @Query 쿼리메서드와 병합 -> JPQL
    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);



    // 네이티브 쿼리 진짜 쿼리문을 사용하는 기법
    @Query(value = "select now()", nativeQuery = true) // 진짜 쿼리문으로 동작 nativeQuery = true
    String getTime();

    // 이미지 처리하는 select문
    @EntityGraph(attributePaths = {"imageSet"}) // 지연로딩이지만 같이 로딩해야 하는 값!!
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImage(Long bno);

}
