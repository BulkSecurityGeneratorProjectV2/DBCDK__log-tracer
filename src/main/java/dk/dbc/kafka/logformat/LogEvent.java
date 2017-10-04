/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.md
 */

package dk.dbc.kafka.logformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.event.Level;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEvent {
    private OffsetDateTime timestamp;
    private String host;
    @JsonProperty("sys_env")
    private String env;
    @JsonProperty("sys_team")
    private String team;
    @JsonProperty("sys_appid")
    private String appID;
    @JsonProperty("sys_taskid")
    private String taskId;
    @JsonProperty("sys_type")
    private String type;
    @JsonProperty("sys_json")
    private Boolean json;
    @JsonProperty("@timestamp")
    private OffsetDateTime kafkaTimestamp;

    private Level level;
    private String message;
    private byte[] raw;

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public OffsetDateTime getKafkaTimestamp() {
        return kafkaTimestamp;
    }

    public void setKafkaTimestamp(OffsetDateTime kafkaTimestamp) {
        this.kafkaTimestamp = kafkaTimestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isJson() {
        return json == null ? false : json;
    }

    public void setJson(Boolean json) {
        this.json = json;
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "timestamp=" + timestamp +
                ", kafkaTimestamp=" + kafkaTimestamp +
                ", host='" + host + '\'' +
                ", env='" + env + '\'' +
                ", team='" + team + '\'' +
                ", appID='" + appID + '\'' +
                ", taskId='" + taskId + '\'' +
                ", type='" + type + '\'' +
                ", json=" + json +
                ", level=" + level +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogEvent logEvent = (LogEvent) o;

        if (timestamp != null ? !timestamp.equals(logEvent.timestamp) : logEvent.timestamp != null) {
            return false;
        }
        if (host != null ? !host.equals(logEvent.host) : logEvent.host != null) {
            return false;
        }
        if (env != null ? !env.equals(logEvent.env) : logEvent.env != null) {
            return false;
        }
        if (team != null ? !team.equals(logEvent.team) : logEvent.team != null) {
            return false;
        }
        if (appID != null ? !appID.equals(logEvent.appID) : logEvent.appID != null) {
            return false;
        }
        if (taskId != null ? !taskId.equals(logEvent.taskId) : logEvent.taskId != null) {
            return false;
        }
        if (type != null ? !type.equals(logEvent.type) : logEvent.type != null) {
            return false;
        }
        if (json != null ? !json.equals(logEvent.json) : logEvent.json != null) {
            return false;
        }
        if (level != logEvent.level) {
            return false;
        }
        return message != null ? message.equals(logEvent.message) : logEvent.message == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (env != null ? env.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (appID != null ? appID.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (json != null ? json.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
