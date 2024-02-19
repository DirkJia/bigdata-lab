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
package com.dirkjia.mr;


import com.dirkjia.format.CustomOutputFormat;

import org.apache.commons.lang3.RandomUtils;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.SUCCESSFUL_JOB_OUTPUT_DIR_MARKER;


/**
 * @author heshijia
 * @since 2023/10/8
 */
public class WordCountTask {

    private static final String PATH = WordCountTask.class.getClassLoader().getResource("data").getPath() + Path.SEPARATOR;

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Path inputPath = new Path(System.getProperty("wd.input.path",PATH + "/input/"));
        Path outputPath = new Path(System.getProperty("wd.out.paht",PATH + "/output/"+ RandomUtils.nextInt(1,100)));

        //添加conf
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "local");

        Job job = Job.getInstance(conf, "wordcount");

        // 设置主类
        job.setJarByClass(WordCountTask.class);

        // 设置map
        job.setMapperClass(WordCountMapper.class);

        // 设置reduce
        job.setReducerClass(WordCountReducer.class);

        job.setCombinerClass(WordCountReducer.class);

        //设置map输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置reduce输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //设置输入输出路径
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setOutputFormatClass(CustomOutputFormat.class);
//        job.setOutputFormatClass(TextOutputFormat.class);

        //取消_SUCCESS标记
        job.getConfiguration().setBoolean(SUCCESSFUL_JOB_OUTPUT_DIR_MARKER, false);

        // 根据path找到这个文件
        FileSystem fileSystem = outputPath.getFileSystem(conf);
        if(fileSystem.exists(outputPath)) {
            // true的意思是，就算output有东西，也一带删除
            fileSystem.delete(outputPath, true);
        }

        //输入根目录，可以循环获取目录下文件，默认只获取当前目录下的文件
        FileInputFormat.setInputDirRecursive(job, true);

        // 等待任务执行完成
        boolean b = job.waitForCompletion(true);

        if(!b){
            System.out.println("wordcount task fail!");
        }
    }

}
