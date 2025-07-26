package com.crayon.interview.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * 使用zk的curator框架的leaderLatch实现选举的问题，使用一个服务。
 * @Description
 * @Author Administrator
 * @Date 2025/7/8 16:05
 **/
public class LeaderLatchService implements AutoCloseable{

    // 当前节点ID
    private final String nodeId;
    private final CuratorFramework client;
    private final LeaderLatch leaderLatch;

    // Zookeeper 地址
    private static final String ZK_ADDRESS = "localhost:2181";
    // 服务路径
    private static final String LEADER_PATH = "/services/your-service-name/leader";

    public LeaderLatchService(String nodeId) {
        this.nodeId = nodeId;
        this.client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new ExponentialBackoffRetry(1000, 3)
        );
        this.leaderLatch = new LeaderLatch(client, LEADER_PATH, nodeId);
    }

    public void start() throws Exception {
        client.start();
        leaderLatch.start();

        // 等待最多10秒确认领导权
        boolean isLeader = leaderLatch.await(10, TimeUnit.SECONDS);

        if (isLeader) {
            System.out.println("👑 节点 [" + nodeId + "] 成为主节点");
//            NodeRoleMonitor.onBecameLeader();
        } else {
            System.out.println("💤 节点 [" + nodeId + "] 是从节点");
//            NodeRoleMonitor.onBecameFollower();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            leaderLatch.close();
        } catch (Exception e) {
            throw new IOException("关闭 LeaderLatch 出错", e);
        } finally {
            client.close();
        }
    }

    public boolean isLeader() {
        return leaderLatch.hasLeadership();
    }

    public static void main(String[] args) {
        // 可通过 -Dnode.id=xxx 设置
        String nodeId = "node-" + System.getProperty("node.id", "default");

        try (LeaderLatchService service = new LeaderLatchService(nodeId)) {
            service.start();

            // 模拟持续运行
            while (true) {
                if (service.isLeader()) {
                    // 执行主节点任务，如定时任务、数据同步等
                    System.out.println("主节点正在执行任务...");
                } else {
                    System.out.println("从节点空闲中...");
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
