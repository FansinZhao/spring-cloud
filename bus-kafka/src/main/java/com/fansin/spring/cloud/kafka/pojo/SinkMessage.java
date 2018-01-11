package com.fansin.spring.cloud.kafka.pojo;

/**
 * @author  zhaofeng
 * @date 17-6-22.
 */
public class SinkMessage {

    private String name;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SinkMessage{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
