package com.baoyun.base.config.client.util;

import static org.apache.zookeeper.client.FourLetterWordMain.send4LetterWord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZKDatabase;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.quorum.QuorumPeer;
import org.junit.Assert;

@Slf4j
public class LocalZookeeperServer {
	public static final long CONNECTION_TIMEOUT = 30000;
	public static ZooKeeperServer zks = null;
	public static ServerCnxnFactory factory = null;
	public static String hostPort = null;
	public static int port = 0;

	public static void main(String[] args) throws IOException,
			InterruptedException {
		start();

		stop();
	}

	public static void start() throws IOException, InterruptedException {
		// port = (new Random().nextInt(10000) + 30000);
	    port = 3000;
		hostPort = "127.0.0.1:" + port;

		createNewServerInstance(100);
		
		startServerInstance();
	}
	
	public static void stop(){
		shutdownServerInstance();
	}

	public static void createNewServerInstance(int maxCnxns) throws IOException, InterruptedException {
		final int port = getPort(hostPort);
//		log.info("CREATING server instance 127.0.0.1:{}", port);
		factory = ServerCnxnFactory.createFactory(port, maxCnxns);
	}

	public static void startServerInstance() throws IOException,
			InterruptedException {
		final int port = getPort(hostPort);
//		log.info("STARTING server instance 127.0.0.1:{}", port);
		
		File dataDir = new File("temp/zookeeper/data/");
		if(dataDir.exists()){
			//System.out.println();
			deleteDir(dataDir);
		}

		zks = new ZooKeeperServer(dataDir, dataDir, 3000);
		factory.startup(zks);

//		log.info("STARTING server instance 127.0.0.1:{} success", port);
		Assert.assertTrue("waiting for server up",
				waitForServerUp("127.0.0.1:" + port, CONNECTION_TIMEOUT));
	}
	private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                	//System.out.println(dir.getPath());
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	private static int getPort(String hostPort) {
		String[] split = hostPort.split(":");
		String portstr = split[split.length - 1];
		String[] pc = portstr.split("/");
		if (pc.length > 1) {
			portstr = pc[0];
		}
		return Integer.parseInt(portstr);
	}

	public static boolean waitForServerUp(String hp, long timeout) {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				// if there are multiple hostports, just take the first one
				HostPort hpobj = parseHostPortList(hp).get(0);
				String result = send4LetterWord(hpobj.host, hpobj.port, "stat");
				if (result.startsWith("Zookeeper version:")
						&& !result.contains("READ-ONLY")) {
					return true;
				}
			} catch (IOException e) {
				// ignore as this is expected
//				log.info("server " + hp + " not up " + e);
			}

			if (System.currentTimeMillis() > start + timeout) {
				break;
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// ignore
			}
		}
		return false;
	}

	public static boolean waitForServerDown(String hp, long timeout) {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				HostPort hpobj = parseHostPortList(hp).get(0);
				send4LetterWord(hpobj.host, hpobj.port, "stat");
			} catch (IOException e) {
				return true;
			}

			if (System.currentTimeMillis() > start + timeout) {
				break;
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// ignore
			}
		}
		return false;
	}

	public static boolean waitForServerState(QuorumPeer qp, int timeout,
			String serverState) {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// ignore
			}
			if (qp.getServerState().equals(serverState))
				return true;
			if (System.currentTimeMillis() > start + timeout) {
				return false;
			}
		}
	}

	static void shutdownServerInstance() {
		if (factory != null) {
			ZKDatabase zkDb = null;
			{
				if (zks != null) {
					zkDb = zks.getZKDatabase();
				}
			}
			factory.shutdown();
			try {
				if (zkDb != null) {
					zkDb.close();
				}
			} catch (IOException ie) {
//				log.warn("Error closing logs ", ie);
			}
			final int PORT = getPort(hostPort);

			Assert.assertTrue("waiting for server down", waitForServerDown("127.0.0.1:" + PORT, CONNECTION_TIMEOUT));
		}
	}

	public static class HostPort {
		String host;
		int port;

		public HostPort(String host, int port) {
			this.host = host;
			this.port = port;
		}
	}

	public static List<HostPort> parseHostPortList(String hplist) {
		ArrayList<HostPort> alist = new ArrayList<HostPort>();
		for (String hp : hplist.split(",")) {
			int idx = hp.lastIndexOf(':');
			String host = hp.substring(0, idx);
			int port;
			try {
				port = Integer.parseInt(hp.substring(idx + 1));
			} catch (RuntimeException e) {
				throw new RuntimeException("Problem parsing " + hp
						+ e.toString());
			}
			alist.add(new HostPort(host, port));
		}
		return alist;
	}
}
