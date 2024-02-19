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
package com.dirkjia.spark;


import com.dirkjia.spark.listener.CustomSparkListener;
import org.apache.flink.core.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * @author heshijia
 * @since 2023/11/24
 */
public class SparkDemo {

    private static final String PATH = SparkDemo.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) {
        String readme = PATH + "/in/test";
        SparkConf conf = new SparkConf().setAppName("this's first spark app");
        conf.setMaster("local[1]");

        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.sc().addSparkListener(new CustomSparkListener());
        // 从指定的文件中读取数据到RDD
        JavaRDD<String> logData = sc.textFile(readme).cache();

        // 过滤包含h的字符串，然后在获取数量
        long num = logData.filter((Function<String, Boolean>) s -> s.contains("h")).count();

        System.out.println("the count of word a is " + num);

    }

}
