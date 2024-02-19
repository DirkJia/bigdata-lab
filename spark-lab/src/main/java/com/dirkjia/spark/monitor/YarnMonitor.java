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
package com.dirkjia.spark.monitor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author heshijia
 * @since 2023/11/24
 */
@Slf4j
public class YarnMonitor {


    private ApplicationId applicationId;

    public YarnMonitor(String appId) {
        log.info("==================================");
        log.info("应用ID是:" + appId);
        log.info("==================================");

        this.applicationId = ConverterUtils.toApplicationId(appId);

    }


    public YarnApplicationState getState(ApplicationAttemptId applicationId) throws IOException, YarnException {
        try(YarnClient client = YarnClient.createYarnClient()){
            Configuration conf = new Configuration();
            client.init(conf);
            client.start();
            YarnApplicationState yarnApplicationState = null;
            final ApplicationAttemptReport applicationAttemptReport = client.getApplicationAttemptReport(applicationId);
            final YarnApplicationAttemptState yarnApplicationAttemptState = applicationAttemptReport.getYarnApplicationAttemptState();
            return yarnApplicationState;
        } catch (YarnException e) {
            log.error("err: {}",e);
            return null;
        }
    }




}
