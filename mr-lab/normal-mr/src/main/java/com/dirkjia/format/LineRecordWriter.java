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

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author heshijia
 * @since 2023/10/25
 */
public class LineRecordWriter <K, V> extends RecordWriter<K, V> {

    public final byte[] NEWLINE = System.lineSeparator().getBytes(StandardCharsets.UTF_8);

    private DataOutputStream out;

    public LineRecordWriter(DataOutputStream out) {
        this.out = out;
    }

    private void writeObject(Object o)
            throws IOException {
        if (o instanceof Text) {
            Text to = (Text) o;
            out.write(to.getBytes(), 0, to.getLength());
        } else if (o instanceof Writable) {
            //添加对Writable的支持
            ((Writable) o).write(out);
        } else {
            out.write(o.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public synchronized void write(K key, V value)
            throws IOException {
        boolean nullKey = key == null || key instanceof NullWritable;
        boolean nullValue = value == null || value instanceof NullWritable;
        if (nullKey && nullValue) {
            return;
        }
        //删除了key value分隔符
        if (!nullValue) {
            writeObject(value);
        }
        out.write(NEWLINE);

    }

    @Override
    public synchronized void close(TaskAttemptContext context)
            throws IOException {
        out.close();
    }
}