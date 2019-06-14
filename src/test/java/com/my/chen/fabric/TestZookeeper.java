package com.my.chen.fabric;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenwei
 * @version 1.0
 * @date 2018/10/12
 * @description
 */
public class TestZookeeper implements Watcher {


    private ZooKeeper zkClient;

    // 分布式锁持久化节点名称
    private static String LOCK_PERSIST = "/DIS_LOCK";

    // 临时节点前缀
    private static String LOCK_ELEPHANT_PREFIX = LOCK_PERSIST + "/dis_";

    // zk连接的ip
    private String ips = "localhost:2181";

    // session过期时间
    private static int sessionTimeout = 300000;

    // 主线程等待连接建立好后才启动
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    // 当前线程创建临时节点后返回的路径
    private String selfPath;
    // 等锁路径
    private String waitPath;

    private String lockName;

    private CountDownLatch latch;


    public static void main(String[] args) throws Exception {
        TestZookeeper zk = new TestZookeeper();
        zk.createRootNode(LOCK_PERSIST, "根节点");

        System.out.println("hsh");

        zk.createElephantNode("abcd");

        System.out.println("dd");
    }

    public boolean createElephantNode(String data) {
        try {
            selfPath = zkClient.create(LOCK_ELEPHANT_PREFIX, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(lockName + " 创建临时节点路径" + selfPath+"|内容:"+data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void unDLock() {
        System.out.println(lockName + "删除本节点：" + selfPath);
        try {
            zkClient.delete(selfPath, -1);
            selfPath = null;
            releaseConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void releaseConnection() {
        if (this.zkClient != null) {
            try {
                this.zkClient.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(lockName + "释放连接");
    }


    // 创建根节点,根节点不需要进行watch
    private boolean createRootNode(String path, String data) {
        try {
            createConnection();
            if (zkClient.exists(path, false) == null) {
                String retPath = zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                System.out.println("创建根节点:path" + retPath + "content" + data);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void createConnection() throws IOException, InterruptedException {
        if (zkClient == null) {
            zkClient = new ZooKeeper(ips, sessionTimeout, this);
            connectedSemaphore.await();
        }
    }


    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent == null) {
            return;
        }

        Event.EventType eventType = watchedEvent.getType();
        Event.KeeperState state = watchedEvent.getState();

        if (Event.KeeperState.SyncConnected == state) {
            if (Event.EventType.None == eventType) {
                System.out.println("正在启动连接服务器");
                connectedSemaphore.countDown();
            } else if (Event.KeeperState.Disconnected == state) {
                System.out.println("与ZK服务器断开连接");
            } else if (Event.KeeperState.Expired == state) {
                System.out.println("会话失效");
            }
        }
    }
}

