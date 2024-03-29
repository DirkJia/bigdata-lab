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
package com.dirkjia.partition;

import com.google.common.hash.Hashing;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Partitioner;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author heshijia
 * @since 2023/10/25
 */
public class CustomPartitioner extends Partitioner<Text, Writable> {

    @Override
    public int getPartition(Text text, Writable writable, int numReduceTasks) {
        final String key = text.toString();
        return Hashing.consistentHash(Hashing.murmur3_32().hashBytes(key.getBytes(StandardCharsets.UTF_8)), numReduceTasks);
    }
}
