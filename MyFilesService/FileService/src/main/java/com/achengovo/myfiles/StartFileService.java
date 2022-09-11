package com.achengovo.myfiles;

import com.achengovo.lightning.server.StartServer;
import com.achengovo.myfiles.service.FileService;
import com.achengovo.myfiles.service.UserService;
import com.achengovo.myfiles.serviceImpl.FileServiceImpl;
import com.achengovo.myfiles.serviceImpl.UserServiceImpl;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StartFileService {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        if (args != null && args.length > 0) {
            //获取程序运行的根目录
            String rootPath = System.getProperty("user.dir");
            //从参数中获取配置文件路径并组装成完整路径
            String configFilePath = rootPath + args[0];
            // 使用InPutStream流读取properties文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFilePath));
            properties.load(bufferedReader);

        }
        Map<String, Object> serviceInstances = new HashMap<>();
        serviceInstances.put(FileService.class.getName(), new FileServiceImpl());
        Instance instance = new Instance();
        instance.setWeight((Double.parseDouble(properties.getProperty("service.weight","0.1"))));
        instance.setIp(properties.getProperty("service.ip","127.0.0.1"));
        instance.setPort(Integer.parseInt(properties.getProperty("service.port","7001")));
        instance.setServiceName(FileService.class.getName());
        StartServer startServer =
                new StartServer(serviceInstances,
                        properties.getProperty("naming.url", "127.0.0.1:8848"),
                        instance,
                        properties.getProperty("service.group", "DEFAULT_GROUP"));
        startServer.start();
    }
}
