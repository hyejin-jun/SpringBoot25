package org.mbc.board;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest  //  부트용 테스트
@Log4j2   // 콘솔에 log.info("")
public class DataSourceTests {  // 데이터베이스 연결 테스트용


    @Autowired  // 생성자 자동 주입
    private DataSource dataSource;  // new DateSource();
    // dataSource 객체는 app.pro에 있는 데이터베이스 정보를 활용

    @Test // import org.junit.jupiter.api.Test;
    public void  testConnectipon() throws SQLException {

        @Cleanup  // 시작할 때 청소
        Connection con = dataSource.getConnection();

        log.info("데이터베이스 연결 테스트용 객체: " + con );
        Assertions.assertNotNull(con);

    }


}
