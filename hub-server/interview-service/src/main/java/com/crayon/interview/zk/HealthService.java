package com.crayon.interview.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/7/8 16:19
 **/
public class HealthService {

    public void register() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                // 会话超时时间
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        // 创建一个临时节点，表示当前服务在线
        String path = "/services/my-service/instances/" + "ip";
        //自定义的参数
        String metadata = "{\"ip\":\"192.168.1.10\",\"port\":8080,\"role\":\"leader\",\"status\":\"active\"}";
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, metadata.getBytes(StandardCharsets.UTF_8));
    }
    public boolean isEphemeralNodeExists(CuratorFramework client, String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            return stat != null;
        } catch (KeeperException.NoNodeException e) {
            // 节点不存在
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
