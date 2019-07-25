package cn.com.siss.spring.boot.mybatis.autoconfigration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "mybatis")
public class MybatisProperties {

    private String typeAliasesPackage;

    private String mapperLocations = "classpath:mapper/*/*Mapper.xml";

    private String configLocation;
}
