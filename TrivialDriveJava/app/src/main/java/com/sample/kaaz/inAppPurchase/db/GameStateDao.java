/*
 * Copyright (C) 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.kaaz.inAppPurchase.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface GameStateDao {
    @Query("SELECT `value` FROM GameState WHERE `key` = :key LIMIT 1")
    LiveData<Integer> observe(String key);

    @Query("SELECT `value` FROM GameState WHERE `key` = :key LIMIT 1")
    Integer get(String key);

    @Query("REPLACE INTO GameState VALUES(:key,:value)")
    void put(String key, int value);

    @Query("UPDATE GameState SET `value`=`value`-1 WHERE `key`=:key AND `value` > :minValue")
    int decrement(String key, int minValue);

    @Query("UPDATE GameState SET `value`=`value`+1 WHERE `key`=:key AND `value` < :maxValue")
    int increment(String key, int maxValue);
}
