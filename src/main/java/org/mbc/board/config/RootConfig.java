package org.mbc.board.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // 환경설정이라고 스프링에게 알려 줘야 함
public class RootConfig {

    @Bean  // 환경설정용 객체로 지정
    public ModelMapper getMapper(){  // implementation 'org.modelmapper:modelmapper:3.1.0'
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;  // 엔티티를 DTO로 변환하게 환경설정

    }
}
