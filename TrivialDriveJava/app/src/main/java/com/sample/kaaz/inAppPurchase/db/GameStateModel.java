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

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameStateModel {
    final GameStateDao gameStateDao;
    final ExecutorService queryExecutor = Executors.newSingleThreadExecutor();
    final static private String GAS_LEVEL = "gas";
    final LiveData<Integer> gasTankLevel;

    public GameStateModel(@NonNull Application application) {
        // This creates our DB and populates our game state database with the initial state of
        // a full tank
        GameStateDatabase db = Room.databaseBuilder(application,
                GameStateDatabase.class, "GameState.db")
                .createFromAsset("database/initialgamestate.db")
                .build();
        gameStateDao = db.gameStateDao();
        gasTankLevel = gameStateDao.observe(GAS_LEVEL);
    }

    public void decrementGas(int minLevel) {
        queryExecutor.submit(() -> {
            gameStateDao.decrement(GAS_LEVEL, minLevel);
        });
    }

    public void incrementGas(int maxLevel) {
        queryExecutor.submit(() -> {
            gameStateDao.increment(GAS_LEVEL, maxLevel);
        });
    }

    public LiveData<Integer> gasTankLevel() {
        return gasTankLevel;
    }

    public Integer getCurrentGasTankLevel() {
        return gameStateDao.get(GAS_LEVEL);
    }
}
