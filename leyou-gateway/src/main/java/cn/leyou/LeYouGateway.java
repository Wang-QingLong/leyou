package cn.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 16:53
 * @description:
 */
@EnableDiscoveryClient  //开启客户端发现功能
@EnableZuulProxy   //开启zuul网关
@SpringBootApplication
public class LeYouGateway {
    public static void main(String[] args) {
        SpringApplication.run(LeYouGateway.class);
    }
}
