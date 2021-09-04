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

import android.app.Application;

import com.sample.kaaz.inAppPurchase.billing.BillingDataSource;
import com.sample.kaaz.inAppPurchase.db.GameStateModel;

public class InAppPurchaseApplication extends Application {
    public AppContainer appContainer;

    // Container of objects shared across the whole app
    public class AppContainer {
        final GameStateModel gameStateModel = new GameStateModel(InAppPurchaseApplication.this);
        final BillingDataSource billingDataSource = BillingDataSource.getInstance(
                InAppPurchaseApplication.this,
                TrivialDriveRepository.INAPP_SKUS,
                TrivialDriveRepository.SUBSCRIPTION_SKUS,
                TrivialDriveRepository.AUTO_CONSUME_SKUS);
        final public TrivialDriveRepository trivialDriveRepository = new TrivialDriveRepository(
                billingDataSource,
                gameStateModel);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer();
    }
}
