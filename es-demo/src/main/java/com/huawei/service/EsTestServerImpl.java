package com.huawei.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsTestServerImpl implements EsTestServer{
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private DiscussPostRepository postRepository;

    @Override
    public void saveEs(DiscussPost discussPost) {
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
    @Override
    public List<DiscussPost> search(String keyword, int start, int limit) throws IOException {
        //构建查询
        SearchRequest request = new SearchRequest("discusspost");
        //构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"title","content"))//多字段匹配
//            .fetchSource("title", null)//过滤门店编码为null数据
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

//    /**
//     * 快照查询
//     * @param reqVO
//     * @return
//     */
//    public List<String> baseProductShopNotExistEsSearch(ReqBaseProductShopPageEsQueryVO reqVO) {
//        try {
//            SearchRequest searchRequest = new SearchRequest(EsOperateTables.BASE_PRODUCT_SHOP_SAP.getAlias());
//            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//            sourceBuilder.fetchSource("shop_code", null);//过滤门店编码为null数据
//            Collection<String> ids = reqVO.getIds();
//            if (CollectionUtils.isNotEmpty(ids)) {
//                ids=ids.stream().distinct().collect(Collectors.toList());
//                sourceBuilder.query(QueryBuilders.termsQuery(EsUtils.ID, ids));//根据ID查询
//            }else{
//                throw new RRException("请求参数不能为空");
//            }
//            sourceBuilder.timeout(TimeValue.timeValueMinutes(2L));
//            sourceBuilder.size(new Long(ConstantsUtils.QueryPartams.MAX_RESULT_COUNT).intValue());//分页量
//            sourceBuilder.sort(EsUtils.ID, SortOrder.DESC);//排序
//            searchRequest.source(sourceBuilder);
//            //scroll 分页搜索
//            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
//            searchRequest.scroll(scroll);
//            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            String scrollId = searchResponse.getScrollId();
//            SearchHit[] searchHits = searchResponse.getHits().getHits();
//            List<String> mapList = Lists.newArrayList();
//            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
//            while (true) {
//                if(StringUtils.isNotBlank(scrollId)){
//                    clearScrollRequest.addScrollId(scrollId);
//                }
//                if(searchHits == null || searchHits.length == 0){
//                    break;
//                }
//                Arrays.stream(searchHits).forEach(hit -> mapList.add(hit.getId()));
//                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
//                scroll = new Scroll(TimeValue.timeValueMinutes(1L));
//                scrollRequest.scroll(scroll);
//                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
//                scrollId = searchResponse.getScrollId();
//                searchHits = searchResponse.getHits().getHits();
//            }
//            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
//            boolean succeeded = clearScrollResponse.isSucceeded();
//            if(!succeeded){
//                throw new RRException("清除es游标失败");
//            }
//            if(CollectionUtils.isNotEmpty(mapList)){
//                return Lists.newArrayList(CollectionUtils.disjunction(ids,mapList));
//            }else{
//                return Lists.newArrayList(ids);
//            }
//
//        } catch (Exception e) {
//
//        }
//    }

}
