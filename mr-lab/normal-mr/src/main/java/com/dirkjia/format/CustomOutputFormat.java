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
package com.dirkjia.format;

import com.dirkjia.mr.WordCountTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * @author heshijia
 * @since 2023/10/25
 */
public class CustomOutputFormat extends FileOutputFormat<Text, Writable> {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    static {
        NUMBER_FORMAT.setMinimumIntegerDigits(5);
        NUMBER_FORMAT.setGroupingUsed(false);
    }

    @Override
    public RecordWriter<Text, Writable> getRecordWriter(TaskAttemptContext job) throws IOException {
        String fileName = "test";
        Path workPath = getTaskOutputPath(job);
        return getLineRecordWriter(job, workPath, fileName);
    }

    private Path getTaskOutputPath(TaskAttemptContext conf) throws IOException {
        Path workPath = null;
        OutputCommitter committer = super.getOutputCommitter(conf);

        //这个work path 是一个临时目录
        if (committer instanceof FileOutputCommitter) {
            workPath = ((FileOutputCommitter) committer).getWorkPath();
        } else {
            Path outputPath = getOutputPath(conf);
            if (outputPath == null) {
                throw new IOException("Undefined job output-path");
            }
            workPath = outputPath;
        }
        return workPath;
    }

    public RecordWriter<Text, Writable> getLineRecordWriter(TaskAttemptContext job, Path workPath, String fileName) throws IOException {
        Configuration conf = job.getConfiguration();
        String extension = null;
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            extension = fileName.substring(index);
            fileName = fileName.substring(0, index);
        }
        int id = job.getTaskAttemptID().getTaskID().getId();
        fileName = fileName + "-" + NUMBER_FORMAT.format(id);

        Path file;
        if (StringUtils.isBlank(extension)) {
            file = new Path(workPath, fileName);
        } else {
            file = new Path(workPath, fileName + extension);
        }

        FileSystem fs = file.getFileSystem(conf);
        DataOutputStream outputStream;
        if (fs.exists(file)) {
            //输出文件存在的情况下，使用append，正常情况下不会有这种情况
            //当且仅当使员工partition时，使用了lru
            outputStream = fs.append(file);
        }else {
            outputStream  = fs.create(file, false);
        }
        return new LineRecordWriter<>(outputStream);
    }

    @Override
    public synchronized OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException {
        return super.getOutputCommitter(context);
    }

}
