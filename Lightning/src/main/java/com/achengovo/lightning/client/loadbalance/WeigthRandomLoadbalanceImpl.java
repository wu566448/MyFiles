package com.achengovo.lightning.client.loadbalance;

import com.achengovo.lightning.client.Client;
import org.slf4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * 按权重的随机负载均衡算法
 */
public class WeigthRandomLoadbalanceImpl implements LoadBalance {
    Logger log=org.slf4j.LoggerFactory.getLogger(WeigthRandomLoadbalanceImpl.class);
    @Override
    public Client select(List<Client> clients) {
        int weightSum = 0;
        for (Client client : clients) {
            if (client.isAvailable()) {
                weightSum += client.getWeight();
            }
        }
        if(clients.size()==0){
            log.error("没有可用的服务");
        }
        int random = new Random().nextInt(weightSum);
        int weight = 0;
        for (Client client : clients) {
            if (client.isAvailable()) {
                weight += client.getWeight();
                if (weight > random) {
                    return client;
                }
            }

        }
        return null;
    }
}
