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
 * @since 2023/11/6
 */
public class FlinkSqlParquetDemo {



    private static final String PATH = FlinkSqlJsonDemo.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(10000);
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, settings);

        tableEnvironment.executeSql(CREATE_FILE_IN);
        tableEnvironment.executeSql(CREATE_PRINT);
        tableEnvironment.executeSql(JOIN_SELF);
    }

    public static final String CREATE_FILE_IN = String.format("CREATE TABLE source_table (\n" +
            "  id INT,\n" +
            "  tinyint_field bigint,\n" +
            "  bit_field STRING,\n" +
            "  smallint_field bigint,\n" +
            "  mediumint_field STRING,\n" +
            "  year_field STRING,\n" +
            "  int_field int,\n" +
            "  bigint_field bigint,\n" +
            "  datetime_field bigint,\n" +
            "  timestamp_field STRING,\n" +
            "  float_field String,\n" +
            "  double_field String,\n" +
            "  decimal_field double,\n" +
            "  date_field STRING,\n" +
            "  blob_field STRING,\n" +
            "  pt String\n" +
            " ) WITH (\n" +
            "'connector' = 'filesystem',\n" +
            "  'path' = '%s',\n" +
            "  'format' = 'parquet'\n" +
            ")",PATH+"tp");

    public static final String CREATE_PRINT = "CREATE TABLE print_table (\n" +
            "  id INT,\n" +
            "  tinyint_field bigint,\n" +
            "  bit_field STRING,\n" +
            "  date_field STRING,\n" +
            "  double_field STRING,\n" +
            "  float_field String,\n" +
            "  datetime_field bigint,\n" +
            "  decimal_field double\n" +

            " ) WITH (\n" +
            "  'connector' = 'print'" +
            "  )";

    public static final String JOIN_SELF = "insert into print_table select id,tinyint_field,bit_field,date_field,double_field,float_field,datetime_field,decimal_field from source_table ";



}
