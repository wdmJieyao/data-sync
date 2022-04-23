package top.lijieyao.datasync.mq.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Description: Canal消息
 * @Author: LiJieYao
 * @Date: 2022/4/22 23:17
 */
@NoArgsConstructor
@Data
@ToString
public class CanalMessage {

    @JsonProperty("data")
    private List<DataDTO> data;

    @JsonProperty("database")
    private String database;

    @JsonProperty("es")
    private Long es;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("isDdl")
    private Boolean isDdl;

    @JsonProperty("mysqlType")
    private MysqlTypeDTO mysqlType;

    @JsonProperty("old")
    private List<OldDTO> old;

    @JsonProperty("pkNames")
    private List<String> pkNames;

    @JsonProperty("sql")
    private String sql;

    @JsonProperty("sqlType")
    private SqlTypeDTO sqlType;

    @JsonProperty("table")
    private String table;

    @JsonProperty("ts")
    private Long ts;

    @JsonProperty("type")
    private String type;

    @NoArgsConstructor
    @Data
    public static class MysqlTypeDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("original_url")
        private String originalUrl;

        @JsonProperty("short_code")
        private String shortCode;

        @JsonProperty("create_time")
        private String createTime;

        @JsonProperty("update_time")
        private String updateTime;

        @JsonProperty("status")
        private String status;
    }

    @NoArgsConstructor
    @Data
    public static class SqlTypeDTO {
        @JsonProperty("id")
        private Integer id;

        @JsonProperty("original_url")
        private Integer originalUrl;

        @JsonProperty("short_code")
        private Integer shortCode;

        @JsonProperty("create_time")
        private Integer createTime;

        @JsonProperty("update_time")
        private Integer updateTime;

        @JsonProperty("status")
        private Integer status;
    }

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("original_url")
        private String originalUrl;

        @JsonProperty("short_code")
        private String shortCode;

        @JsonProperty("create_time")
        private String createTime;

        @JsonProperty("update_time")
        private String updateTime;

        @JsonProperty("status")
        private String status;
    }

    @NoArgsConstructor
    @Data
    public static class OldDTO {
        @JsonProperty("create_time")
        private String createTime;

        @JsonProperty("update_time")
        private String updateTime;
    }

}
