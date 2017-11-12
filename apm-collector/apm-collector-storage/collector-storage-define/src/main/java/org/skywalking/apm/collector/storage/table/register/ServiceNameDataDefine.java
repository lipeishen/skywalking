/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.storage.table.register;

import org.skywalking.apm.collector.core.data.Data;
import org.skywalking.apm.collector.storage.base.define.Attribute;
import org.skywalking.apm.collector.storage.base.define.AttributeType;
import org.skywalking.apm.collector.storage.base.define.DataDefine;
import org.skywalking.apm.collector.storage.base.define.operator.CoverOperation;
import org.skywalking.apm.collector.storage.base.define.operator.NonOperation;

/**
 * @author peng-yongsheng
 */
public class ServiceNameDataDefine extends DataDefine {

    @Override protected int initialCapacity() {
        return 4;
    }

    @Override protected void attributeDefine() {
        addAttribute(0, new Attribute(ServiceNameTable.COLUMN_ID, AttributeType.STRING, new NonOperation()));
        addAttribute(1, new Attribute(ServiceNameTable.COLUMN_SERVICE_NAME, AttributeType.STRING, new CoverOperation()));
        addAttribute(2, new Attribute(ServiceNameTable.COLUMN_APPLICATION_ID, AttributeType.INTEGER, new CoverOperation()));
        addAttribute(3, new Attribute(ServiceNameTable.COLUMN_SERVICE_ID, AttributeType.INTEGER, new CoverOperation()));
    }

    public enum ServiceName {
        INSTANCE;

        public String getId(Data data) {
            return data.getDataString(0);
        }

        public String getServiceName(Data data) {
            return data.getDataString(1);
        }

        public int getApplicationId(Data data) {
            return data.getDataInteger(0);
        }

        public int getServiceId(Data data) {
            return data.getDataInteger(1);
        }
    }
}