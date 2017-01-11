package com.baoyun.base.config.client.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;

public class ZookeeperServerStart {
    /**
     * clientPort=3000
     */
    public static final int serverPort = 3000;

    static ZooKeeperServerMain zkServer;

    public void startZookeeperServer(String[] args) {

        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper("127.0.0.1:" + "1001", 1000 * 3, new Watcher() {

                @Override
                public void process(WatchedEvent event) {
                    // TODO Auto-generated method stub

                }
            });
            System.out.println(" start judge \t zookeeper:" + zookeeper);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (zookeeper == null || !zookeeper.getState().isAlive() || !zookeeper.getState().isConnected()) {

            InputStream inputStream = ZookeeperServerStart.class.getResourceAsStream("/zoo.cfg");

            Properties properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                QuorumPeerConfig quorumPeerConfig = new QuorumPeerConfig();
                quorumPeerConfig.parseProperties(properties);

                final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();

                final ServerConfig serverConfig = new ServerConfig();
                serverConfig.readFrom(quorumPeerConfig);
                // zkServer.runFromConfig(serverConfig);
                zkServer.runFromConfig(serverConfig);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ConfigException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class MainThread extends Thread {
        final File confFile;
        final TestZKSMain main;
        final File tmpDir;

        public MainThread(int clientPort) throws IOException {
            super("Standalone server with clientPort:" + clientPort);
            // tmpDir = ClientBase.createTmpDir();
            tmpDir = new File("/opt/zookeeper");
            confFile = new File(tmpDir, "zoo.cfg");
            if (confFile.getParentFile().exists()) {
                delete(confFile.getParentFile());
            }
            confFile.getParentFile().mkdirs();

            if (confFile.exists()) {
                confFile.delete();
            }
            confFile.createNewFile();

            FileWriter fwriter = new FileWriter(confFile);
            fwriter.write("tickTime=2000\n");
            fwriter.write("initLimit=10\n");
            fwriter.write("syncLimit=5\n");

            File dataDir = new File(tmpDir, "data");
            if (!dataDir.mkdir()) {
                System.out.println(" \t 创建测试目录失败" + dataDir);
            }

            // Convert windows path to UNIX to avoid problems with "\"
            String dir = dataDir.toString();
            String osname = java.lang.System.getProperty("os.name");
            if (osname.toLowerCase().contains("windows")) {
                dir = dir.replace('\\', '/');
            }
            fwriter.write("dataDir=" + dir + "\n");

            fwriter.write("clientPort=" + clientPort + "\n");
            fwriter.flush();
            fwriter.close();

            main = new TestZKSMain();
        }

        public boolean deleteFile(File file) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File childrenFile : fileArray) {
                    boolean childrenDelete = childrenFile.delete();
                    if (!childrenDelete) {
                        System.out.println(" \t delete fail: " + childrenFile);
                    }
                }
            }

            return file.delete();
        }

        public void run() {
            String args[] = new String[1];
            args[0] = confFile.toString();
            try {
                main.initializeAndRun(args);
            } catch (Exception e) {
                // test will still fail even though we just log/ignore
                System.out.println("unexpected exception in run" + e.getMessage());
            }
        }

        public void shutdown() throws IOException {
            main.shutdown();
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (confFile.getParentFile().exists()) {
                delete(confFile.getParentFile());
            }
        }

        void deleteDirs() throws IOException {
            delete(tmpDir);
        }

        void delete(File f) throws IOException {
            if (f.isDirectory()) {
                for (File c : f.listFiles())
                    delete(c);
            }
            if (!f.delete())
                // double check for the file existence
                if (f.exists()) {
                    throw new IOException("Failed to delete file: " + f);
                }
        }
    }

    public static class TestZKSMain extends ZooKeeperServerMain {
        public void shutdown() {
            super.shutdown();
        }

        public void initializeAndRun(String[] args) throws ConfigException, IOException {
            super.initializeAndRun(args);
        }
    }

    public static void main(String[] args) {
        new ZookeeperServerStart().startZookeeperServer(args);
    }

}
