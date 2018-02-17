package com.fansin.spring.cloud.jta.mybatis.consumer.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * The type Consumer.
 *
 * @author author
 */
public class Consumer implements Serializable{


    /**
    * 主键
    * 
    * isNullAble:0
    */
    private java.math.BigDecimal id;

    /**
    * 
    * isNullAble:1
    */
    private String name;

    /**
    * 
    * isNullAble:0,defaultVal:current_timestamp()
    */
    private java.time.LocalDateTime recv_time;

    /**
    * 
    * isNullAble:1
    */
    private Integer result;


    /**
     * Set id.
     *
     * @param id the id
     */
    public void setId(java.math.BigDecimal id){
        this.id = id;
    }

    /**
     * Get id java . math . big decimal.
     *
     * @return the java . math . big decimal
     */
    public java.math.BigDecimal getId(){
        return this.id;
    }

    /**
     * Set name.
     *
     * @param name the name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Set recv time.
     *
     * @param recv_time the recv time
     */
    public void setRecv_time(java.time.LocalDateTime recv_time){
        this.recv_time = recv_time;
    }

    /**
     * Get recv time java . time . local date time.
     *
     * @return the java . time . local date time
     */
    public java.time.LocalDateTime getRecv_time(){
        return this.recv_time;
    }

    /**
     * Set result.
     *
     * @param result the result
     */
    public void setResult(Integer result){
        this.result = result;
    }

    /**
     * Get result integer.
     *
     * @return the integer
     */
    public Integer getResult(){
        return this.result;
    }
    @Override
    public String toString() {
        return "Consumer{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                "recv_time='" + recv_time + '\'' +
                "result='" + result + '\'' +
            '}';
    }

    /**
     * Query build query builder.
     *
     * @return the query builder
     */
    public static QueryBuilder QueryBuild(){
        return new QueryBuilder();
    }

    /**
     * The type Query builder.
     */
    public static class QueryBuilder extends Consumer{
        /**
        * 需要返回的列
        */
        private Map<String,Object> fetchFields;

        /**
         * Get fetch fields map.
         *
         * @return the map
         */
        public Map<String,Object> getFetchFields(){
            return this.fetchFields;
        }

        private List<java.math.BigDecimal> idList;


        private List<String> nameList;


        private List<String> fuzzyName;

        /**
         * Get fuzzy name list.
         *
         * @return the list
         */
        public List<String> getFuzzyName(){
            return this.fuzzyName;
        }

        private List<String> rightFuzzyName;

        /**
         * Get right fuzzy name list.
         *
         * @return the list
         */
        public List<String> getRightFuzzyName(){
            return this.rightFuzzyName;
        }
        private List<java.time.LocalDateTime> recv_timeList;

        private java.time.LocalDateTime recv_timeSt;

        private java.time.LocalDateTime recv_timeEd;

        /**
         * Get recv time st java . time . local date time.
         *
         * @return the java . time . local date time
         */
        public java.time.LocalDateTime getRecv_timeSt(){
            return this.recv_timeSt;
        }

        /**
         * Get recv time ed java . time . local date time.
         *
         * @return the java . time . local date time
         */
        public java.time.LocalDateTime getRecv_timeEd(){
            return this.recv_timeEd;
        }

        private List<Integer> resultList;


        private QueryBuilder (){
            this.fetchFields = new HashMap<>();
        }


        /**
         * Id query builder.
         *
         * @param id the id
         * @return the query builder
         */
        public QueryBuilder id(java.math.BigDecimal id){
            setId(id);
            return this;
        }

        /**
         * Id list query builder.
         *
         * @param id the id
         * @return the query builder
         */
        public QueryBuilder idList(java.math.BigDecimal ... id){
            this.idList = Arrays.asList(id);
            return this;
        }

        /**
         * Id list query builder.
         *
         * @param id the id
         * @return the query builder
         */
        public QueryBuilder idList(List<java.math.BigDecimal> id){
            this.idList = id;
            return this;
        }

        /**
         * Fetch id query builder.
         *
         * @return the query builder
         */
        public QueryBuilder fetchId(){
            setFetchFields("fetchFields","id");
            return this;
        }

        /**
         * Exclude id query builder.
         *
         * @return the query builder
         */
        public QueryBuilder excludeId(){
            setFetchFields("excludeFields","id");
            return this;
        }


        /**
         * Fuzzy name query builder.
         *
         * @param fuzzyName the fuzzy name
         * @return the query builder
         */
        public QueryBuilder fuzzyName (List<String> fuzzyName){
            this.fuzzyName = fuzzyName;
            return this;
        }

        /**
         * Fuzzy name query builder.
         *
         * @param fuzzyName the fuzzy name
         * @return the query builder
         */
        public QueryBuilder fuzzyName (String ... fuzzyName){
            this.fuzzyName = Arrays.asList(fuzzyName);
            return this;
        }

        /**
         * Right fuzzy name query builder.
         *
         * @param rightFuzzyName the right fuzzy name
         * @return the query builder
         */
        public QueryBuilder rightFuzzyName (List<String> rightFuzzyName){
            this.rightFuzzyName = rightFuzzyName;
            return this;
        }

        /**
         * Right fuzzy name query builder.
         *
         * @param rightFuzzyName the right fuzzy name
         * @return the query builder
         */
        public QueryBuilder rightFuzzyName (String ... rightFuzzyName){
            this.rightFuzzyName = Arrays.asList(rightFuzzyName);
            return this;
        }

        /**
         * Name query builder.
         *
         * @param name the name
         * @return the query builder
         */
        public QueryBuilder name(String name){
            setName(name);
            return this;
        }

        /**
         * Name list query builder.
         *
         * @param name the name
         * @return the query builder
         */
        public QueryBuilder nameList(String ... name){
            this.nameList = Arrays.asList(name);
            return this;
        }

        /**
         * Name list query builder.
         *
         * @param name the name
         * @return the query builder
         */
        public QueryBuilder nameList(List<String> name){
            this.nameList = name;
            return this;
        }

        /**
         * Fetch name query builder.
         *
         * @return the query builder
         */
        public QueryBuilder fetchName(){
            setFetchFields("fetchFields","name");
            return this;
        }

        /**
         * Exclude name query builder.
         *
         * @return the query builder
         */
        public QueryBuilder excludeName(){
            setFetchFields("excludeFields","name");
            return this;
        }


        /**
         * Recv time bet ween query builder.
         *
         * @param recv_timeSt the recv time st
         * @param recv_timeEd the recv time ed
         * @return the query builder
         */
        public QueryBuilder recv_timeBetWeen(java.time.LocalDateTime recv_timeSt,java.time.LocalDateTime recv_timeEd){
            this.recv_timeSt = recv_timeSt;
            this.recv_timeEd = recv_timeEd;
            return this;
        }


        /**
         * Recv time query builder.
         *
         * @param recv_time the recv time
         * @return the query builder
         */
        public QueryBuilder recv_time(java.time.LocalDateTime recv_time){
            setRecv_time(recv_time);
            return this;
        }

        /**
         * Recv time list query builder.
         *
         * @param recv_time the recv time
         * @return the query builder
         */
        public QueryBuilder recv_timeList(java.time.LocalDateTime ... recv_time){
            this.recv_timeList = Arrays.asList(recv_time);
            return this;
        }

        /**
         * Recv time list query builder.
         *
         * @param recv_time the recv time
         * @return the query builder
         */
        public QueryBuilder recv_timeList(List<java.time.LocalDateTime> recv_time){
            this.recv_timeList = recv_time;
            return this;
        }

        /**
         * Fetch recv time query builder.
         *
         * @return the query builder
         */
        public QueryBuilder fetchRecv_time(){
            setFetchFields("fetchFields","recv_time");
            return this;
        }

        /**
         * Exclude recv time query builder.
         *
         * @return the query builder
         */
        public QueryBuilder excludeRecv_time(){
            setFetchFields("excludeFields","recv_time");
            return this;
        }


        /**
         * Result query builder.
         *
         * @param result the result
         * @return the query builder
         */
        public QueryBuilder result(Integer result){
            setResult(result);
            return this;
        }

        /**
         * Result list query builder.
         *
         * @param result the result
         * @return the query builder
         */
        public QueryBuilder resultList(Integer ... result){
            this.resultList = Arrays.asList(result);
            return this;
        }

        /**
         * Result list query builder.
         *
         * @param result the result
         * @return the query builder
         */
        public QueryBuilder resultList(List<Integer> result){
            this.resultList = result;
            return this;
        }

        /**
         * Fetch result query builder.
         *
         * @return the query builder
         */
        public QueryBuilder fetchResult(){
            setFetchFields("fetchFields","result");
            return this;
        }

        /**
         * Exclude result query builder.
         *
         * @return the query builder
         */
        public QueryBuilder excludeResult(){
            setFetchFields("excludeFields","result");
            return this;
        }

        /**
         * Fetch all query builder.
         *
         * @return the query builder
         */
        public QueryBuilder fetchAll(){
            this.fetchFields.put("AllFields",true);
            return this;
        }

        /**
         * Add field query builder.
         *
         * @param fields the fields
         * @return the query builder
         */
        public QueryBuilder addField(String ... fields){
            this.fetchFields.put("otherFields",Arrays.asList(fields));
            return this;
        }
        @SuppressWarnings("unchecked")
        private void setFetchFields(String key,String val){
            Map<String,Boolean> fields= (Map<String, Boolean>) this.fetchFields.getOrDefault(key,new HashMap<>());
            fields.put(val,true);
            this.fetchFields.putIfAbsent(key,fields);
        }

        /**
         * Build consumer.
         *
         * @return the consumer
         */
        public Consumer build(){
            return this;
        }
    }

}
