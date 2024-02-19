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
package com.dirkjia;

/**
 * @author heshijia
 * @since 2023/12/25
 */
public class CodeTest {


    public static void main(String[] args) {
        String version1 = "V01.R01";
        String version2 = "V1.R01.C001";
        CodeTest codeTest = new CodeTest();
        final int res = codeTest.compareVersions(version1, version2);
        System.out.println(res);
    }


    /**
     *
     * 示例1：
     * 输入：version1 = "V01.R01", version2 = "V1.R01.C001"
     * 输出：-1
     * 示例2：
     * 输入：version1 = "V01.R00", version2 = "V01.R00.C000"
     * 输出：0
     * 解释：version1 没有指定下标为 2 的修订号，即视为 "0"
     * 示例3：
     * 输入：version1 = "V01.R01", version2 = "V01.R00.C010"
     * 输出：1
     *
     * @return
     */
    public int compareVersions(String v1,String v2){
        String[] v1OrigineArr = v1.split("\\.");
        String[] v2OrigineArr = v2.split("\\.");
        //补充完整版本格式
        String[] v1Arr = supplyVersion(v1OrigineArr);
        String[] v2Arr = supplyVersion(v2OrigineArr);
        //比较V
        if(Integer.parseInt(v1Arr[0].substring(1))==Integer.parseInt(v2Arr[0].substring(1))){
            //比较R
            if(Integer.parseInt(v1Arr[1].substring(1))==Integer.parseInt(v2Arr[1].substring(1))){
                //比较C
                return Integer.compare(Integer.parseInt(v1Arr[2].substring(1)),Integer.parseInt(v2Arr[2].substring(1)));
            } else {
                return Integer.compare(Integer.parseInt(v1Arr[1].substring(1)),Integer.parseInt(v2Arr[1].substring(1)));
            }
        } else {
            return Integer.compare(Integer.parseInt(v1Arr[0].substring(1)),Integer.parseInt(v2Arr[0].substring(1)));
        }
    }

    private String[] supplyVersion(String[] versionArr){
        if(versionArr.length==0){
            String[] resArr = new String[3];
            resArr[0]="V00";
            resArr[1]="R00";
            resArr[2]="C00";
            return resArr;
        } else if(versionArr.length==1){
            String[] resArr = new String[3];
            resArr[0]=versionArr[0];
            resArr[1]="R00";
            resArr[2]="C00";
            return resArr;
        } else if(versionArr.length==2){
            if(versionArr[1].startsWith("R")){
                String[] resArr = new String[3];
                resArr[0]=versionArr[0];
                resArr[1]=versionArr[1];
                resArr[2]="C00";
                return resArr;
            } else if(versionArr[1].startsWith("C")){
                String[] resArr = new String[3];
                resArr[0]=versionArr[0];
                resArr[1]="R00";
                resArr[2]=versionArr[1];
                return resArr;
            }
        }
        return versionArr;
    }
}
