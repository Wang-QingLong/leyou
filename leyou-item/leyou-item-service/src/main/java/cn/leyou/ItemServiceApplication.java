package cn.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 17:32
 * @description:  提供服务
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.leyou.mapper")
public class ItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class);
    }
}
