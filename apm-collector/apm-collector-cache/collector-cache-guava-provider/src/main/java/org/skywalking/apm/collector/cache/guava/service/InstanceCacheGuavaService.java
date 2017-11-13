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

package org.skywalking.apm.collector.cache.guava.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.skywalking.apm.collector.cache.service.InstanceCacheService;
import org.skywalking.apm.collector.core.util.Const;
import org.skywalking.apm.collector.storage.dao.IInstanceCacheDAO;
import org.skywalking.apm.collector.storage.service.DAOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class InstanceCacheGuavaService implements InstanceCacheService {

    private final Logger logger = LoggerFactory.getLogger(InstanceCacheGuavaService.class);

    private final Cache<Integer, Integer> integerCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(5000).build();

    private final Cache<String, Integer> stringCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(5000).build();

    private final DAOService daoService;

    public InstanceCacheGuavaService(DAOService daoService) {
        this.daoService = daoService;
    }

    public int get(int applicationInstanceId) {
        IInstanceCacheDAO dao = (IInstanceCacheDAO)daoService.get(IInstanceCacheDAO.class);

        int applicationId = 0;
        try {
            applicationId = integerCache.get(applicationInstanceId, () -> dao.getApplicationId(applicationInstanceId));
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        if (applicationId == 0) {
            applicationId = dao.getApplicationId(applicationInstanceId);
            if (applicationId != 0) {
                integerCache.put(applicationInstanceId, applicationId);
            }
        }
        return applicationId;
    }

    @Override public int getInstanceId(int applicationId, String agentUUID) {
        IInstanceCacheDAO dao = (IInstanceCacheDAO)daoService.get(IInstanceCacheDAO.class);
        String key = applicationId + Const.ID_SPLIT + agentUUID;

        int instanceId = 0;
        try {
            instanceId = stringCache.get(key, () -> dao.getInstanceId(applicationId, agentUUID));
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        if (instanceId == 0) {
            instanceId = dao.getInstanceId(applicationId, agentUUID);
            if (applicationId != 0) {
                stringCache.put(key, instanceId);
            }
        }
        return instanceId;
    }
}