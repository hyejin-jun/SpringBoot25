package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity  // 데이터베이스 관련 객체임을 선언
@Table(name="tbl_memo")  // 데이터베이스 테이블명 선어
@ToString
@Getter
@Builder  // 빌더 패턴 사용 member.setName() > member.name()
@AllArgsConstructor  // 모든 필드값을 이용하여 생성자 생성
@NoArgsConstructor  // 기본 생성자
public class Memo {
    
    @Id // PK임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // GenerationType.IDENTITY: PK를 자동으로 생성하고자 함 (키 생성 전)
    // 만일 연결되는 데이터베이스가 오라클이면 번호를 위한 별도의 테이블 생성 (시퀀스 객체)
    // mysql이나 mariaDB면 auto increment를 기본으로 사용해서 새로운 레코드가 기록될 때 다른 번호 줌
    // 제너레이션타입.AUTO: jpa가 알아서 생성 방식 결정
    // 제너레이션타입.SEQUNCE: 데이터베이스의 시퀀스를 이용하여 키 생성
    // 제너레이션타입.TABLE: 키 생성 전용 테이블 생성 후 키 생성
    
    private Long mno;
    
    @Column(length=200, nullable = false)  // 200 글자에 notnull 효과
    private String memoText;
    
    
    
}
