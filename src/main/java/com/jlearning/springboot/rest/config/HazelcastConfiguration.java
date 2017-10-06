package com.jlearning.springboot.rest.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;

/**
 * Configuration class for Hazelcast Cluster
 * @file HazelcastConfiguration.java
 * @author dwagle
 * @date Oct 1, 2017
 * @time 2:49:29 PM
 */
@Configuration
@PropertySource("classpath:/hazelcast-config.properties")
//@PropertySource("file:/incomm/dds-cache-cluster/config/hazelcast-config.properties")
public class HazelcastConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(HazelcastConfiguration.class);
	
	@Autowired
	Environment env;
	
	@Bean
	public Config config(){
		logger.info("Setting up Hazelcast Configuration !!!");
		
		Config config = new Config();
		
		// Instance Name
		config.setInstanceName(env.getProperty("instance.name"));
		
		// Group Config
		config.setGroupConfig(groupConfig());
		
		// Network Config (TCP-IP/Multicast)
		config.setNetworkConfig(networkConfig());
		
		/**
		 * Data Cache Configurations
		 */
		// Map for merchant specific signing keys
		config.addMapConfig(mapConfig("userSigningKeys"));
		
		// Map for merchant authentication credentials
		config.addMapConfig(mapConfig("userAuthCredentials"));
		
		// Map for jwt properties
		config.addMapConfig(mapConfig("jwtData"));
		
		return config;
		
	}
	
	public GroupConfig groupConfig(){
		GroupConfig config = new GroupConfig();		
		config.setName(env.getProperty("cluster.group.name"));
		config.setPassword(env.getProperty("cluster.group.password"));	
		return config;
	}
	
	public NetworkConfig networkConfig(){
		NetworkConfig config = new NetworkConfig();
		
		config.setPort(Integer.valueOf(env.getProperty("network.config.port")));
		config.setPortAutoIncrement(Boolean.valueOf(env.getProperty("network.config.autoincrement")));
		config.setPortCount(Integer.valueOf(env.getProperty("network.config.portcount")));
		
		JoinConfig join = new JoinConfig();
		
		MulticastConfig multicast = new MulticastConfig();
		multicast.setEnabled(Boolean.valueOf(env.getProperty("multicast.config.enabled")));
		
		TcpIpConfig tcpIp = new TcpIpConfig();
		tcpIp.setEnabled(Boolean.valueOf(env.getProperty("tcpip.config.enabled")));
		
		List<String> memberList = new ArrayList<String>();
		memberList.addAll(Arrays.asList(env.getProperty("tcpip.config.memberlist").split(",")));
		tcpIp.setMembers(memberList);
		
		join.setMulticastConfig(multicast);
		join.setTcpIpConfig(tcpIp);
		
		config.setJoin(join);
		
		return config;
	}
	
	public MapConfig mapConfig(String mapSuffix){
		MapConfig mapConfig = new MapConfig();
		mapConfig.setName(env.getProperty("map.name."+mapSuffix))
				  .setEvictionPolicy(EvictionPolicy.NONE)
				  .setInMemoryFormat(InMemoryFormat.BINARY)		
				  .setTimeToLiveSeconds(Integer.valueOf(null == env.getProperty("map.ttl."+mapSuffix) ? env.getProperty("map.ttl.generic") : env.getProperty("map.ttl."+mapSuffix)))
				  .setMaxIdleSeconds(Integer.valueOf(null == env.getProperty("map.maxIdleSec."+mapSuffix) ? env.getProperty("map.maxIdleSec.generic") : env.getProperty("map.maxIdleSec."+mapSuffix)))
				  .setBackupCount(Integer.valueOf(null == env.getProperty("map.backupCount."+mapSuffix) ? env.getProperty("map.backupCount.generic") : env.getProperty("map.backupCount."+mapSuffix)))
				  .setAsyncBackupCount(Integer.valueOf(null == env.getProperty("map.asyncBackupCount."+mapSuffix) ? env.getProperty("map.asyncBackupCount.generic") : env.getProperty("map.asyncBackupCount."+mapSuffix)))
				  .setReadBackupData(Boolean.valueOf(null == env.getProperty("map.readBackupData."+mapSuffix) ? env.getProperty("map.readBackupData.generic") : env.getProperty("map.readBackupData."+mapSuffix)));
		return mapConfig;
	}
}
