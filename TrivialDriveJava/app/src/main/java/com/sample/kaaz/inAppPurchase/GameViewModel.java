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
package com.sample.kaaz.inAppPurchase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/*
    This is used for any business logic, as well as to echo LiveData from the BillingRepository.
 */
public class GameViewModel extends ViewModel {
    static final String TAG = GameViewModel.class.getSimpleName();
    private final TrivialDriveRepository tdr;

    public GameViewModel(@NonNull TrivialDriveRepository trivialDriveRepository) {
        super();
        tdr = trivialDriveRepository;
    }

    public void drive() {
        tdr.drive();
    }

    /*
        We can drive if we have at least one unit of gas.
     */
    public LiveData<Boolean> canDrive() {
        LiveData<Integer> gasUnitsRemaining = getGasUnitsRemaining();
        return Transformations.map(gasUnitsRemaining, gasUnits -> gasUnits > 0);
    }

    public LiveData<Boolean> isPremium() {
        return tdr.isPurchased(TrivialDriveRepository.SKU_PREMIUM);
    }

    public LiveData<Integer> getGasUnitsRemaining() {
        return tdr.gasTankLevel();
    }

    public void sendMessage(int message) {
        tdr.sendMessage(message);
    }

    public static class GameViewModelFactory implements
            ViewModelProvider.Factory {
        private final TrivialDriveRepository trivialDriveRepository;

        public GameViewModelFactory(TrivialDriveRepository tdr) {
            trivialDriveRepository = tdr;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(GameViewModel.class)) {
                return (T) new GameViewModel(trivialDriveRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
