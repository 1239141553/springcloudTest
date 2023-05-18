package com.huawei;

import com.alibaba.fastjson.JSONObject;
import com.huawei.mapper.DiscussPostRepository;
import com.huawei.pojo.DiscussPost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = TestEs.class)
public class TestDemo{
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private DiscussPostRepository postRepository;

    @Test
    public void  test1(){
        System.out.println("666");
    }

    /**
     * 保存或修改数据
     */
    @Test
    public void save(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent("112");
        discussPost.setCommentCount(123);
        discussPost.setCreateTime(new Date());
        discussPost.setStatus(1);
        discussPost.setTitle("EsTest");
        postRepository.save(discussPost);
    }

    /**
     * 查询
     * @param keyword
     * @param start
     * @param limit
     * @return
     * @throws IOException
     */
    public List<DiscussPost> search(String keyword, int start, int limit) throws IOException {
        //构建查询
        SearchRequest request = new SearchRequest("discusspost");
        //构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(keyword,"title","content"))//多字段匹配
                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))//根据类型排序
                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))//根据得分排序
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))//根据时间排序
                .from(start).size(limit)
                .highlighter(new HighlightBuilder().field("title").preTags("<em style='color:red'>").postTags("</em>").requireFieldMatch(false))//高亮字段用em标签包裹，且关闭多个高亮
                .highlighter(new HighlightBuilder().field("content").preTags("<em style='color:red'>").postTags("</em>").requireFieldMatch(false));
        request.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<DiscussPost> list=new ArrayList<>();
        for (SearchHit hit : hits) {
            //获取高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //解析高亮字段
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            HighlightField title = highlightFields.get("title");
            if (title != null) {
                Text[] fragments = title.getFragments();
                String newTitle="";
                newTitle+=title;
                sourceAsMap.put("title",newTitle);
            }
            HighlightField content = highlightFields.get("content");
            if (content != null) {
                Text[] fragments = content.getFragments();
                String newContent="";
                newContent+=content;
                sourceAsMap.put("content",newContent);
            }
            list.add(JSONObject.parseObject(hit.getSourceAsString(),DiscussPost.class));
        }
        return list;
    }

}
