package com.huawei.utils;

import com.alibaba.fastjson.JSONObject;
import com.huawei.config.EsClient;
import com.huawei.pojo.EsPage;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ElasticSearchUtil {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public static boolean createIndex(String index) {
        if (!isIndexExist(index)) {
            logger.info("Index is not exits!");
        }
        CreateIndexResponse indexresponse = null;
        try {
            indexresponse = EsClient.getTransportClient().admin().indices().prepareCreate(index).execute()
                    .actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("执行建立成功？" + indexresponse.isAcknowledged());

        return indexresponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index) {
        if (!isIndexExist(index)) {
            logger.info("Index is not exits!");
        }
        AcknowledgedResponse dResponse = EsClient.getTransportClient().admin().indices().prepareDelete(index).execute().actionGet();
        if (dResponse.isAcknowledged()) {
            logger.info("delete index " + index + "  successfully!");
        } else {
            logger.info("Fail to delete index " + index);
        }
        return dResponse.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public static boolean isIndexExist(String index) {
        IndicesExistsResponse inExistsResponse = EsClient.getTransportClient().admin().indices().exists(new IndicesExistsRequest
                (index)).actionGet();
        if (inExistsResponse.isExists()) {
            logger.info("Index [" + index + "] is exist!");
        } else {
            logger.info("Index [" + index + "] is not exist!");
        }
        return inExistsResponse.isExists();
    }

    /**
     * 数据添加，正定ID
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type, String id) {

        IndexResponse response = EsClient.getTransportClient().prepareIndex(index, type, id).setSource(jsonObject).get();

        logger.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());

        return response.getId();
    }

    /**
     * 数据添加
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type) {
        return addData(jsonObject, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public static void deleteDataById(String index, String type, String id) {

        DeleteResponse response = EsClient.getTransportClient().prepareDelete(index, type, id).execute().actionGet();

        logger.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
    }

    /**
     * 通过ID 更新数据
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static void updateDataById(JSONObject jsonObject, String index, String type, String id) {

        UpdateRequest updateRequest = new UpdateRequest();

        updateRequest.index(index).type(type).id(id).doc(jsonObject);

        EsClient.getTransportClient().update(updateRequest);

    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {

        GetRequestBuilder getRequestBuilder = EsClient.getTransportClient().prepareGet(index, type, id);

        if (StringUtils.isNotEmpty(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }

        GetResponse getResponse = getRequestBuilder.execute().actionGet();

        return getResponse.getSource();
    }

    /**
     * 使用分词查询不分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @param operatorTag    查询条件最外层的关系(true与,false或)
     * @param parmStr        过滤条件
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, Integer size,long startTime,
                                                           long endTime, String fields, String logType, String sortField, String highlightField, boolean
                                                                   operatorTag, String... parmStr) {
        SearchRequestBuilder searchRequestBuilder = EsClient.getTransportClient().prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("createDate").format("epoch_millis").from(startTime).to(endTime)
                    .includeLower(true).includeUpper(false));
        }
        //设置查询日志类型
        boolQuery.must(QueryBuilders.matchQuery("logType", logType));
        // 查询字段 与关系
        for (int i = 0, len = parmStr.length; i < len; i++) {
            BoolQueryBuilder tempBoolQuery = QueryBuilders.boolQuery();
            if (parmStr[i].contains("&&")){
                String[] tempStr =parmStr[i].split("&&");
                for(int j=0,len1=tempStr.length;j<len1;j++)
                {
                    String[] ss = tempStr[j].split("=");
                    //分词查询
                    tempBoolQuery.must(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                }
                if (operatorTag) {
                    //如果为真则最外层是与关系
                    boolQuery.must(tempBoolQuery);
                } else {
                    boolQuery.should(tempBoolQuery);
                }
            } else {
                if (parmStr[i].contains(",,")) {
                    String[] tempStr =parmStr[i].split(",,");
                    for(int j=0,len1=tempStr.length;j<len1;j++)
                    {
                        String[] ss = tempStr[j].split("=");
                        //分词查询
                        tempBoolQuery.should(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                    }
                    if (operatorTag) {
                        //如果为真则最外层是与关系
                        boolQuery.must(tempBoolQuery);
                    } else {
                        boolQuery.should(tempBoolQuery);
                    }
                } else {
                    if(StringUtils.isNotEmpty(parmStr[i])) {
                        String[] ss = parmStr[i].split("=");
                        if (operatorTag) {
                            //如果为真则最外层是与关系
                            boolQuery.must(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                        } else {
                            boolQuery.should(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        searchRequestBuilder.setQuery(boolQuery);
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        searchRequestBuilder.setFetchSource(true);

        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }
        if (size != null && size > 0) {
            searchRequestBuilder.setSize(size);
        }
        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        logger.info("\n{}", searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().getTotalHits().value;
        long length = searchResponse.getHits().getHits().length;
        logger.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;
    }

    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param currentPage    当前页
     * @param pageSize       每页显示条数
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @param operatorTag    外层逻辑与true 或false
     * @param parmStr        内层逻辑与&& 或||
     * @return
     */
    public static EsPage searchDataPage(String index, String type, int currentPage, int pageSize, long startTime,
                                        long endTime, String fields, String logType, String sortField, String highlightField, boolean
                                                operatorTag, String... parmStr) {
        SearchRequestBuilder searchRequestBuilder = EsClient.getTransportClient().prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("createDate").format("epoch_millis").from(startTime).to(endTime)
                    .includeLower(true).includeUpper(true));
        }
        //设置查询日志类型
        boolQuery.must(QueryBuilders.matchQuery("logType", logType));
        // 查询字段 与关系
        for (int i = 0, len = parmStr.length; i < len; i++) {
            BoolQueryBuilder tempBoolQuery = QueryBuilders.boolQuery();
            if (parmStr[i].contains("&&")) {
                String[] tempStr =parmStr[i].split("&&");
                for(int j=0,len1=tempStr.length;j<len1;j++)
                {
                    String[] ss = tempStr[j].split("=");
                    //分词查询
                    tempBoolQuery.must(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                }
                if (operatorTag) {
                    //如果为真则最外层是与关系
                    boolQuery.must(tempBoolQuery);
                } else {
                    boolQuery.should(tempBoolQuery);
                }
            } else {
                if (parmStr[i].contains(",,")) {
                    String[] tempStr =parmStr[i].split(",,");
                    for(int j=0,len1=tempStr.length;j<len1;j++)
                    {
                        String[] ss = tempStr[j].split("=");
                        //分词查询
                        tempBoolQuery.should(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                    }
                    if (operatorTag) {
                        //如果为真则最外层是与关系
                        boolQuery.must(tempBoolQuery);
                    } else {
                        boolQuery.should(tempBoolQuery);
                    }
                } else {
                    if(StringUtils.isNotEmpty(parmStr[i])) {
                        String[] ss = parmStr[i].split("=");
                        if (operatorTag) {
                            //如果为真则最外层是与关系
                            boolQuery.must(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                        } else {
                            boolQuery.should(QueryBuilders.matchPhraseQuery(ss[0], ss[1]));
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀
            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        searchRequestBuilder.setQuery(boolQuery);
        // 分页应用
        int num = (currentPage - 1) * pageSize;
        searchRequestBuilder.setFrom(num).setSize(pageSize);
        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);
        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        logger.info("\n{}", searchRequestBuilder);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        long totalHits = searchResponse.getHits().getTotalHits().value;
        long length = searchResponse.getHits().getHits().length;
        logger.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);

            return new EsPage(currentPage, pageSize, (int) totalHits, sourceList);
        }
        return null;
    }

    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    public static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("_id", searchHit.getId());

            if (StringUtils.isNotEmpty(highlightField)) {

                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }

        return sourceList;
    }
    /**
     * 批量新增数据
     *
     * @param index    索引名称
     * @param type     索引类型
     * @param dataList 需要新增的数据
     */
    public static void insertBatch(String index, String type, List<Map<String, Object>> dataList,int batchSize) {
        BulkRequestBuilder bulkRequest = EsClient.getTransportClient().prepareBulk();
        for (int i = 0; i < dataList.size(); i++) {
            bulkRequest.add(EsClient.getTransportClient().prepareIndex(index, type, String.valueOf(dataList.get(i).get("id"))).setSource(dataList.get(i)));
            // 每5000条提交一次
            if ((i + 1) % batchSize == 0) {
                BulkResponse bulkItemResponses = bulkRequest.execute().actionGet();
                bulkRequest = EsClient.getTransportClient().prepareBulk();
                logger.info("已保存: {} 条,执行时间：{} ", batchSize, bulkItemResponses.getTook());
            }
        }
        if(dataList.size()%batchSize !=0){
            BulkResponse bulkItemResponses = bulkRequest.execute().actionGet();
            logger.info("保存: {}条,执行时间：{} ", dataList.size() % batchSize, bulkItemResponses.getTook());
        }
    }
}
