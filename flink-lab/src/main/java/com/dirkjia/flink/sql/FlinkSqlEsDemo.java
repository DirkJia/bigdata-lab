/*
 * Copyright (c) 2022 Terminus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dirkjia.flink.sql;

import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author heshijia
 * @since 2023/11/7
 */
public class FlinkSqlEsDemo {

    private static final String PATH = FlinkSqlEsDemo.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(10000);
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);

        tableEnvironment.executeSql(CREATE_MYSQL_SOURCE);
        tableEnvironment.executeSql(CREATE_ES_SINK);
        tableEnvironment.executeSql(INSERT_SQL);
    }



    public static final String CREATE_MYSQL_SOURCE = "CREATE TABLE student_source (" +
            " id BIGINT ," +
            " name STRING, " +
            " password STRING," +
            " age INT )" +
            " WITH  ( " +
            "'connector.type' = 'jdbc'," +
            "'connector.url' = 'jdbc:mysql://127.0.0.1:3306/demo'," +
            "'connector.driver' = 'com.mysql.cj.jdbc.Driver'," +
            "'connector.table' = 'student'," +
            " 'connector.username' = 'root'," +
            " 'connector.password' = '123456'" +
            " )";

    public static final String CREATE_ES_SINK = "CREATE TABLE student_sink (" +
            " id BIGINT ," +
            " name STRING, " +
            " password STRING," +
            " age INT )" +
            " WITH  ( " +
            "'connector' = 'elasticsearch-7'," +
            "'hosts' = 'http://localhost:9200'," +
            "'index' = 'student_sink', " +
            "'format'='json'," +
            "'sink.bulk-flush.max-actions'='10')";

    public static final String INSERT_SQL = "insert into student_sink select * from student_source ";



}
