package com.jaclon.learning.server1;

import com.jaclon.learning.server1.util.HttpClientUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

//    @Lbtransactional(isStart = true)
    @Transactional
    public void test() {
        demoDao.insert("server1");
        HttpClientUtils.get("http://localhost:8082/server2/test");
        //HttpClientUtils.get("http://localhost:8082/server2/test");
        int i = 100/0;
    }
}
