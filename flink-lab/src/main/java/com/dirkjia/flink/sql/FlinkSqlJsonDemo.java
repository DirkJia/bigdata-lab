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

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author heshijia
 * @since 2023/11/6
 */
public class FlinkSqlJsonDemo {


    private static final String PATH = FlinkSqlJsonDemo.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) {

        // 定义一个配置 import org.apache.flink.configuration.Configuration;包下
        Configuration configuration = new Configuration();

        // 指定本地WEB-UI端口号
        configuration.setInteger(RestOptions.PORT, 8082);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
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
            "  name STRING,\n" +
            "  nu INT\n" +
            " ) WITH (\n" +
            "'connector' = 'filesystem',\n" +
            "  'path' = '%s',\n" +
            "  'format' = 'json'\n" +
            ")",PATH+"user");

    public static final String CREATE_PRINT = "CREATE TABLE print_table (\n" +
            "  id INT,\n" +
            "  nu INT,\n" +
            "  name STRING\n" +
            " ) WITH (\n" +
            "  'connector' = 'print'" +
            "  )";

    public static final String JOIN_SELF = "insert into print_table select t1.id,t2.nu,t2.name from source_table t1 left join source_table t2 on t1.id=t2.nu";



}
