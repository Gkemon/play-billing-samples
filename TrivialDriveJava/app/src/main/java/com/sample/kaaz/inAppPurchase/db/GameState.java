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

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
    A very simple key-value store for integers built using Room, since we're storing just a tiny
    bit of data.
 */
@Entity
public class GameState {
    public GameState(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @PrimaryKey
    @NonNull
    public String key;
    public Integer value;
}
