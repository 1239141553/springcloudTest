package com.huawei.service;

import com.huawei.pojo.DiscussPost;

import java.io.IOException;
import java.util.List;

public interface EsTestServer {

    /**
     * 保存Es
     * @param discussPost
     */
    void saveEs(DiscussPost discussPost);


    /**
     * 查询
     * @param keyword
     * @param start
     * @param limit
     * @return
     * @throws IOException
     */
    List<DiscussPost> search(String keyword, int start, int limit) throws IOException;
}
