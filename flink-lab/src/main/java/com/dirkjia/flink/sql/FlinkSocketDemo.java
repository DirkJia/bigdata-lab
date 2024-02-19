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

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author heshijia
 * @since 2023/11/14
 */
public class FlinkSocketDemo {


    private static final String PATH = FlinkSqlJsonDemo.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) throws Exception {

        // 定义一个配置 import org.apache.flink.configuration.Configuration;包下
        Configuration configuration = new Configuration();

        // 指定本地WEB-UI端口号
        configuration.setInteger(RestOptions.PORT, 8082);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);
        env.enableCheckpointing(10000);


        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 9996);


        //第一个参数就一个名字，第二个参数用来表示事件时间
        SingleOutputStreamOperator<Tuple2<String, Long>> initData = socketTextStream.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                String[] s = value.split(" ");
                //假设我们在控制台输入的参数是a 15s,那么我们要15*1000才能得到时间戳的毫秒时间
                return Tuple2.of(s[0], Long.parseLong(s[1]) * 1000L);
            }
        });

        //设置水位线
        SingleOutputStreamOperator<Tuple2<String, Long>> watermarks = initData.assignTimestampsAndWatermarks(
                // 针对乱序流插入水位线，延迟时间设置为 0s
                WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                            @Override
                            public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                                //指定事件时间
                                return element.f1;
                            }
                        })
        );


        //在普通的datastream的api搞不定的时候就可以使用它了
        //KeyedProcessFunction只有在keyBy才能使用
        watermarks.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new AllWindowFunction<Tuple2<String, Long>, String, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow window, Iterable<Tuple2<String, Long>> values, Collector<String> out) throws Exception {
                        for (Tuple2<String, Long> value : values) {
                            out.collect(value.f0);
                        }
                    }
                }).print();

        env.execute();
    }

}
